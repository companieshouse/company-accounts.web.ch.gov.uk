package uk.gov.companieshouse.web.accounts.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import uk.gov.companieshouse.web.accounts.model.state.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.state.State;
import uk.gov.companieshouse.web.accounts.model.state.States;
import uk.gov.companieshouse.web.accounts.token.TokenManager;

@Component
public class StateInterceptor extends HandlerInterceptorAdapter {

    private static final String STATE_COOKIE_NAME = "__CAS";

    private static final String STATE_REQUEST_ATTRIBUTE = "companyAccountsState";

    private static final Pattern COMPANY_ACCOUNTS_REGEX = Pattern.compile("/company-accounts/([^/]*)");

    private static final Pattern APPROVAL_REGEX = Pattern.compile("/approval$");

    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String companyAccountsId = getCompanyAccountsIdFromRequest(request);

        // Only execute for requests for which a company accounts id exists
        if (companyAccountsId != null) {

            // Retrieve the state cookie
            Cookie stateCookie = getStateCookie(request);

            State state;

            if (stateCookie != null) {

                try {
                    // Fetch the States object from the JWT
                    States states = tokenManager.decodeJWT(stateCookie.getValue(), States.class);

                    // Get the state for the current company accounts id
                    state = states.getCompanyAccountsStates().get(companyAccountsId);

                    if (state == null) {
                        state = createNewState();
                    }
                } catch (SignatureException e) {

                    state = createNewState();
                }
            } else {
                state = createNewState();
            }

            // Set the state as an attribute on the request session
            request.getSession().setAttribute(STATE_REQUEST_ATTRIBUTE, state);
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

            States states;

            if (stateCookie == null) {

                // Create a new cookie if one doesn't already exist
                stateCookie = new Cookie(STATE_COOKIE_NAME, "");

                // Instantiate the states object
                states = new States();
            } else {

                try {
                    // Fetch the States object from the JWT
                    states = tokenManager.decodeJWT(stateCookie.getValue(), States.class);
                } catch (SignatureException e) {

                    states = new States();
                }

            }

            // Get the state from the request session
            State state = (State) request.getSession().getAttribute(STATE_REQUEST_ATTRIBUTE);

            if (isApprovalSubmission(request)) {
                // Remove the state for this company accounts id - it's not needed any more
                states.getCompanyAccountsStates().remove(companyAccountsId);
            } else {
                // Remove the oldest 'state' if more than 5 are present to prevent bloating the JWT
                if (states.getCompanyAccountsStates().size() >= 5) {
                    removeOldestState(states);
                }
                // Update / insert the state object on the state map, using the company accounts id as the key
                states.getCompanyAccountsStates().put(companyAccountsId, state);
            }

            try {
                // Convert the states object back to a JWT and set it on the cookie
                String token = tokenManager.createJWT(states);
                stateCookie.setValue(token);
                stateCookie.setPath("/");

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

    private State createNewState() {

        State state = new State();

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        state.setAccountingPolicies(accountingPolicies);

        return state;
    }

    private String getCompanyAccountsIdFromRequest(HttpServletRequest request) {

        Matcher matcher = COMPANY_ACCOUNTS_REGEX.matcher(request.getRequestURI());
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private boolean isApprovalSubmission(HttpServletRequest request) {

        Matcher matcher = APPROVAL_REGEX.matcher(request.getRequestURI());
        return matcher.find() && request.getMethod().equalsIgnoreCase("POST");
    }

    private void removeOldestState(States states) {

        states.getCompanyAccountsStates().entrySet()
                .stream()
                .sorted((c1, c2) -> c1.getValue().getCreated().compareTo(c2.getValue().getCreated()))
                .findFirst()
                .ifPresent(oldestState -> states.getCompanyAccountsStates().remove(oldestState.getKey()));
    }
}
