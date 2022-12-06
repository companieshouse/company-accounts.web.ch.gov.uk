package uk.gov.companieshouse.web.accounts.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ModelAndView;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.token.TokenManager;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyAccountsDataStateInterceptorTests {

    private static final String STATE_REQUEST_ATTRIBUTE = "companyAccountsDataState";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String CRITERIA_URI = "/company/" + COMPANY_NUMBER + "/small-full/criteria";

    private static final String BALANCE_SHEET_URI = "/company/" + COMPANY_NUMBER +
                                                    "/transaction/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full/criteria";
    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private HttpSession mockSession;

    @Mock
    private ModelAndView mockModelAndView;

    @Mock
    private TokenManager mockTokenManager;

    @InjectMocks
    private CompanyAccountsDataStateInterceptor companyAccountsDataStateInterceptor;

    @Test
    @DisplayName("State interceptor preHandle - request uri doesn't contain a company accounts id")
    void preHandleRequestPathDoesNotContainCompanyAccountsId() throws Exception {

        when(mockRequest.getRequestURI()).thenReturn(CRITERIA_URI);

        companyAccountsDataStateInterceptor.preHandle(mockRequest, mockResponse, new Object());

        // Ensure that the token manager is never called and the request session is never manipulated
        verify(mockTokenManager, never()).decodeJWT(anyString(), any());
        verify(mockRequest, never()).getSession();
    }

    @Test
    @DisplayName("State interceptor preHandle - token manager exception")
    void preHandleTokenManagerException() throws Exception {

        when(mockRequest.getRequestURI()).thenReturn(BALANCE_SHEET_URI);
        when(mockRequest.getSession()).thenReturn(mockSession);

        companyAccountsDataStateInterceptor.preHandle(mockRequest, mockResponse, new Object());

        verify(mockSession, times(1)).setAttribute(eq(STATE_REQUEST_ATTRIBUTE), any(CompanyAccountsDataState.class));
    }

    @Test
    @DisplayName("State interceptor postHandle - request uri doesn't contain a company accounts id")
    void postHandleRequestPathDoesNotContainCompanyAccountsId() throws Exception {

        when(mockRequest.getRequestURI()).thenReturn(CRITERIA_URI);

        companyAccountsDataStateInterceptor
                .postHandle(mockRequest, mockResponse, new Object(), mockModelAndView);

        // Ensure that the token manager is never called and the request session is never manipulated
        verify(mockTokenManager, never()).decodeJWT(anyString(), any());
        verify(mockRequest, never()).getSession();
    }
}