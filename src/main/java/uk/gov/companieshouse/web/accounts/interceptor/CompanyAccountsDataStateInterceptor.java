package uk.gov.companieshouse.web.accounts.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import uk.gov.companieshouse.web.accounts.model.state.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataStates;
import uk.gov.companieshouse.web.accounts.token.TokenManager;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CompanyAccountsDataStateInterceptor extends HandlerInterceptorAdapter {

    private static final String STATE_COOKIE_NAME = "__CAS";

    private static final String STATE_REQUEST_ATTRIBUTE = "companyAccountsDataState";

    private static final Pattern COMPANY_ACCOUNTS_REGEX = Pattern.compile("/company-accounts/([^/]*)");

    private static final Pattern SMALL_FULL_APPROVAL_REGEX = Pattern.compile("/small-full/approval$");

    //Used to expire the cookie 10 years in the future, therefore it's "never expiring"
    private static final Integer COOKIE_EXPIRY = 60 * 60 * 24 * 365 * 10;

    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String companyAccountsId = getCompanyAccountsIdFromRequest(request);

        // Only execute for requests for which a company accounts id exists
        if (companyAccountsId != null) {

            // Retrieve the state cookie
            Cookie stateCookie = getStateCookie(request);

            CompanyAccountsDataState companyAccountsDataState;

            if (stateCookie != null) {

                try {
                    // Fetch the States object from the JWT
                    CompanyAccountsDataStates companyAccountsDataStates =
                            tokenManager.decodeJWT(stateCookie.getValue(), CompanyAccountsDataStates.class);

                    // Get the state for the current company accounts id
                    companyAccountsDataState = companyAccountsDataStates.getCompanyAccountsDataStateMap().get(companyAccountsId);

                    if (companyAccountsDataState == null) {
                        companyAccountsDataState = createNewState();
                    }
                } catch (SignatureException e) {

                    companyAccountsDataState = createNewState();
                }
            } else {
                companyAccountsDataState = createNewState();
            }

            // Set the state as an attribute on the request session
            request.getSession().setAttribute(STATE_REQUEST_ATTRIBUTE, companyAccountsDataState);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {

        String companyAccountsId = getCompanyAccountsIdFromRequest(request);

        // Only execute for requests for which a company accounts id exists
        if (companyAccountsId != null) {

            // Retrieve the state cookie
            Cookie stateCookie = getStateCookie(request);

            CompanyAccountsDataStates companyAccountsDataStates;

            if (stateCookie == null) {

                // Create a new cookie if one doesn't already exist
                stateCookie = new Cookie(STATE_COOKIE_NAME, "");

                // Instantiate the states object
                companyAccountsDataStates = new CompanyAccountsDataStates();
            } else {

                try {
                    // Fetch the States object from the JWT
                    companyAccountsDataStates = tokenManager.decodeJWT(stateCookie.getValue(), CompanyAccountsDataStates.class);
                } catch (SignatureException e) {

                    companyAccountsDataStates = new CompanyAccountsDataStates();
                }

            }

            // Get the state from the request session
            CompanyAccountsDataState companyAccountsDataState = (CompanyAccountsDataState) request.getSession().getAttribute(STATE_REQUEST_ATTRIBUTE);

            if (isAccountsApprovalSubmission(request)) {
                // Remove the state for this company accounts id - it's not needed any more
                companyAccountsDataStates.getCompanyAccountsDataStateMap().remove(companyAccountsId);
            } else {
                // Remove the oldest 'state' if more than 5 are present to prevent bloating the JWT
                if (companyAccountsDataStates.getCompanyAccountsDataStateMap().size() >= 5) {
                    removeOldestState(companyAccountsDataStates);
                }
                // Update / insert the state object on the state map, using the company accounts id as the key
                companyAccountsDataStates
                        .getCompanyAccountsDataStateMap().put(companyAccountsId, companyAccountsDataState);
            }

            try {
                // Convert the states object back to a JWT and set it on the cookie
                String token = tokenManager.createJWT(companyAccountsDataStates);
                stateCookie.setValue(token);
                stateCookie.setPath("/");
                stateCookie.setMaxAge(COOKIE_EXPIRY);

                // Add the cookie to the response to save it
                response.addCookie(stateCookie);

            } catch (JsonProcessingException e) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    private Cookie getStateCookie(HttpServletRequest request) {

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equalsIgnoreCase(STATE_COOKIE_NAME))
                .findFirst()
                .orElse(null);
    }

    private CompanyAccountsDataState createNewState() {

        CompanyAccountsDataState companyAccountsDataState = new CompanyAccountsDataState();

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        companyAccountsDataState.setAccountingPolicies(accountingPolicies);

        return companyAccountsDataState;
    }

    private String getCompanyAccountsIdFromRequest(HttpServletRequest request) {

        Matcher matcher = COMPANY_ACCOUNTS_REGEX.matcher(request.getRequestURI());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean isAccountsApprovalSubmission(HttpServletRequest request) {

        Matcher matcher = SMALL_FULL_APPROVAL_REGEX.matcher(request.getRequestURI());
        return matcher.find() && request.getMethod().equalsIgnoreCase("POST");
    }

    /**
     * Removes the oldest state from the {@link CompanyAccountsDataStates} map to prevent bloating the JWT
     * @param companyAccountsDataStates The {@link CompanyAccountsDataStates} object from which to remove the oldest state
     */
    private void removeOldestState(CompanyAccountsDataStates companyAccountsDataStates) {

        companyAccountsDataStates.getCompanyAccountsDataStateMap().entrySet()
                .stream()
                .sorted((c1, c2) -> c1.getValue().getCreated().compareTo(c2.getValue().getCreated()))
                .findFirst()
                .ifPresent(oldestState -> companyAccountsDataStates.getCompanyAccountsDataStateMap().remove(oldestState.getKey()));
    }
}
