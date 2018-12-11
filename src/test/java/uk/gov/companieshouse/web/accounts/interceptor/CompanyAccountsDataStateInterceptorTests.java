package uk.gov.companieshouse.web.accounts.interceptor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataStates;
import uk.gov.companieshouse.web.accounts.token.TokenManager;

@ExtendWith(MockitoExtension.class)
public class CompanyAccountsDataStateInterceptorTests {

    private static final String STATE_COOKIE_NAME = "__CAS";

    private static final String STATE_COOKIE_VALUE = "stateCookieValue";

    private static final String STATE_REQUEST_ATTRIBUTE = "companyAccountsDataState";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String CRITERIA_URI = "/company/" + COMPANY_NUMBER + "/small-full/criteria";

    private static final String BALANCE_SHEET_URI = "/company/" + COMPANY_NUMBER +
                                                    "/transaction/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full/criteria";

    private static final String APPROVAL_URI = "/company/" + COMPANY_NUMBER +
                                                "/transaction/" + TRANSACTION_ID +
                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                "/small-full/approval";

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private HttpSession session;

    @Mock
    private ModelAndView modelAndView;

    @Mock
    private CompanyAccountsDataStates companyAccountsDataStates;

    @Mock
    private Map<String, CompanyAccountsDataState> companyAccountsStates;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private TokenManager tokenManager;

    @InjectMocks
    private CompanyAccountsDataStateInterceptor companyAccountsDataStateInterceptor;

    @Test
    @DisplayName("State interceptor preHandle - request uri doesn't contain a company accounts id")
    void preHandleRequestPathDoesNotContainCompanyAccountsId() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(CRITERIA_URI);

        companyAccountsDataStateInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        // Ensure that the token manager is never called and the request session is never manipulated
        verify(tokenManager, never()).decodeJWT(anyString(), any());
        verify(httpServletRequest, never()).getSession();
    }

    @Test
    @DisplayName("State interceptor preHandle - no state cookie")
    void preHandleStateCookieDoesNotExist() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);
        when(httpServletRequest.getSession()).thenReturn(session);

        Cookie[] cookies = new Cookie[]{};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        companyAccountsDataStateInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        verify(tokenManager, never()).decodeJWT(anyString(), any());
        verify(session, times(1)).setAttribute(eq(STATE_REQUEST_ATTRIBUTE), any(CompanyAccountsDataState.class));
    }

    @Test
    @DisplayName("State interceptor preHandle - token manager exception")
    void preHandleTokenManagerException() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);
        when(httpServletRequest.getSession()).thenReturn(session);

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenThrow(SignatureException.class);

        companyAccountsDataStateInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);
        verify(session, times(1)).setAttribute(eq(STATE_REQUEST_ATTRIBUTE), any(CompanyAccountsDataState.class));
    }

    @Test
    @DisplayName("State interceptor preHandle - state doesn't exist in map")
    void preHandleStateDoesNotExistInMap() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);
        when(httpServletRequest.getSession()).thenReturn(session);

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenReturn(
                companyAccountsDataStates);
        when(companyAccountsDataStates.getCompanyAccountsDataStatesMap()).thenReturn(companyAccountsStates);
        when(companyAccountsStates.get(COMPANY_ACCOUNTS_ID)).thenReturn(null);

        companyAccountsDataStateInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);
        verify(companyAccountsStates, times(1)).get(COMPANY_ACCOUNTS_ID);
        verify(session, times(1)).setAttribute(eq(STATE_REQUEST_ATTRIBUTE), any(CompanyAccountsDataState.class));
    }

    @Test
    @DisplayName("State interceptor preHandle - state already exists")
    void preHandleStateAlreadyExists() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);
        when(httpServletRequest.getSession()).thenReturn(session);

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenReturn(
                companyAccountsDataStates);
        when(companyAccountsDataStates.getCompanyAccountsDataStatesMap()).thenReturn(companyAccountsStates);
        when(companyAccountsStates.get(COMPANY_ACCOUNTS_ID)).thenReturn(companyAccountsDataState);

        companyAccountsDataStateInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object());

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);
        verify(companyAccountsStates, times(1)).get(COMPANY_ACCOUNTS_ID);
        verify(session, times(1)).setAttribute(STATE_REQUEST_ATTRIBUTE, companyAccountsDataState);
    }

    @Test
    @DisplayName("State interceptor postHandle - request uri doesn't contain a company accounts id")
    void postHandleRequestPathDoesNotContainCompanyAccountsId() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(CRITERIA_URI);

        companyAccountsDataStateInterceptor
                .postHandle(httpServletRequest, httpServletResponse, new Object(), modelAndView);

        // Ensure that the token manager is never called and the request session is never manipulated
        verify(tokenManager, never()).decodeJWT(anyString(), any());
        verify(httpServletRequest, never()).getSession();
    }

    @Test
    @DisplayName("State interceptor postHandle - no state cookie")
    void postHandleStateCookieDoesNotExist() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);

        Cookie[] cookies = new Cookie[]{};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute(STATE_REQUEST_ATTRIBUTE)).thenReturn(companyAccountsDataState);

        when(tokenManager.createJWT(any(CompanyAccountsDataStates.class))).thenReturn(STATE_COOKIE_VALUE);

        doNothing().when(httpServletResponse).addCookie(any(Cookie.class));

        companyAccountsDataStateInterceptor
                .postHandle(httpServletRequest, httpServletResponse, new Object(), modelAndView);

        verify(tokenManager, never()).decodeJWT(anyString(), any());

        verify(tokenManager, times(1)).createJWT(any(CompanyAccountsDataStates.class));

        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("State interceptor postHandle - token manager signature exception")
    void postHandleTokenManagerSignatureException() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenThrow(SignatureException.class);

        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute(STATE_REQUEST_ATTRIBUTE)).thenReturn(companyAccountsDataState);

        when(tokenManager.createJWT(any(CompanyAccountsDataStates.class))).thenReturn(STATE_COOKIE_VALUE);

        doNothing().when(httpServletResponse).addCookie(any(Cookie.class));

        companyAccountsDataStateInterceptor
                .postHandle(httpServletRequest, httpServletResponse, new Object(), modelAndView);

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);

        verify(tokenManager, times(1)).createJWT(any(CompanyAccountsDataStates.class));

        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("State interceptor postHandle - approval GET")
    void postHandleGetApproval() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(APPROVAL_URI);
        when(httpServletRequest.getMethod()).thenReturn("GET");

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenReturn(
                companyAccountsDataStates);

        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute(STATE_REQUEST_ATTRIBUTE)).thenReturn(companyAccountsDataState);

        when(companyAccountsDataStates.getCompanyAccountsDataStatesMap()).thenReturn(companyAccountsStates);
        when(companyAccountsStates.size()).thenReturn(1);

        when(tokenManager.createJWT(any(CompanyAccountsDataStates.class))).thenReturn(STATE_COOKIE_VALUE);

        doNothing().when(httpServletResponse).addCookie(any(Cookie.class));

        companyAccountsDataStateInterceptor
                .postHandle(httpServletRequest, httpServletResponse, new Object(), modelAndView);

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);

        verify(companyAccountsStates, times(1)).put(COMPANY_ACCOUNTS_ID, companyAccountsDataState);

        verify(tokenManager, times(1)).createJWT(any(CompanyAccountsDataStates.class));

        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("State interceptor postHandle - approval POST")
    void postHandlePostApproval() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(APPROVAL_URI);
        when(httpServletRequest.getMethod()).thenReturn("POST");

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenReturn(
                companyAccountsDataStates);

        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute(STATE_REQUEST_ATTRIBUTE)).thenReturn(companyAccountsDataState);

        when(companyAccountsDataStates.getCompanyAccountsDataStatesMap()).thenReturn(companyAccountsStates);

        when(tokenManager.createJWT(any(CompanyAccountsDataStates.class))).thenReturn(STATE_COOKIE_VALUE);

        doNothing().when(httpServletResponse).addCookie(any(Cookie.class));

        companyAccountsDataStateInterceptor
                .postHandle(httpServletRequest, httpServletResponse, new Object(), modelAndView);

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);

        verify(companyAccountsStates, times(1)).remove(COMPANY_ACCOUNTS_ID);

        verify(tokenManager, times(1)).createJWT(any(CompanyAccountsDataStates.class));

        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));
    }

    @Test
    @DisplayName("State interceptor postHandle - more than 5 states exist in the JWT")
    void postHandleMoreThanFiveStatesExist() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenReturn(
                companyAccountsDataStates);

        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute(STATE_REQUEST_ATTRIBUTE)).thenReturn(companyAccountsDataState);

        // Build a state map with keys numbered 1 to 5
        Map<String, CompanyAccountsDataState> companyAccountsStates = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            companyAccountsStates.put(Integer.toString(i+1), new CompanyAccountsDataState());
        }
        when(companyAccountsDataStates.getCompanyAccountsDataStatesMap()).thenReturn(companyAccountsStates);

        when(tokenManager.createJWT(any(CompanyAccountsDataStates.class))).thenReturn(STATE_COOKIE_VALUE);

        doNothing().when(httpServletResponse).addCookie(any(Cookie.class));

        companyAccountsDataStateInterceptor
                .postHandle(httpServletRequest, httpServletResponse, new Object(), modelAndView);

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);

        verify(tokenManager, times(1)).createJWT(any(CompanyAccountsDataStates.class));

        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));

        // Assert that the entry of the map with key '1' was removed (as it was the oldest entry)
        assertFalse(companyAccountsStates.containsKey("1"));
        // And assert that it was replaced with a new entry using the company accounts id as the key
        assertTrue(companyAccountsStates.containsKey(COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("State interceptor postHandle - token manager json processing exception")
    void postHandleTokenManagerJsonProcessingException() throws Exception {

        when(httpServletRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);

        Cookie[] cookies = new Cookie[]{createStateCookie()};
        when(httpServletRequest.getCookies()).thenReturn(cookies);

        when(tokenManager.decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class)).thenReturn(
                companyAccountsDataStates);

        when(httpServletRequest.getSession()).thenReturn(session);
        when(session.getAttribute(STATE_REQUEST_ATTRIBUTE)).thenReturn(companyAccountsDataState);

        when(tokenManager.createJWT(any(CompanyAccountsDataStates.class))).thenThrow(JsonProcessingException.class);

        doNothing().when(httpServletResponse).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

        companyAccountsDataStateInterceptor
                .postHandle(httpServletRequest, httpServletResponse, new Object(), modelAndView);

        verify(tokenManager, times(1)).decodeJWT(STATE_COOKIE_VALUE, CompanyAccountsDataStates.class);

        verify(tokenManager, times(1)).createJWT(any(CompanyAccountsDataStates.class));

        verify(httpServletResponse, never()).addCookie(any(Cookie.class));

        verify(httpServletResponse, times(1)).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private Cookie createStateCookie() {

        return new Cookie(STATE_COOKIE_NAME, STATE_COOKIE_VALUE);
    }
}