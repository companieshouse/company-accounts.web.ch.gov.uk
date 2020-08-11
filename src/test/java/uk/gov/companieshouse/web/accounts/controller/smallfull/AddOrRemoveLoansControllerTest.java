package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddOrRemoveLoansControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession httpSession;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private NavigatorService navigatorService;
    
    @Mock
    private ApiClient apiClient;
    
    @Mock
    private ApiClientService apiClientService;
    
    @Mock
    private SmallFullService smallFullService;
    
    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private List<ValidationError> validationErrors;
    
    @InjectMocks
    private AddOrRemoveLoansController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADD_OR_REMOVE_LOAN_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/note/add-or-remove-loans";

    private static final String ADD_OR_REMOVE_LOANS_VIEW = "smallfull/addOrRemoveLoans";

    private static final String ADD_OR_REMOVE_LOANS_MODEL_ATTR = "addOrRemoveLoans";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ERROR_VIEW = "error";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get add or remove loans view - success path")
    void getRequestSuccess() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(loanService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(new Loan[0]);

        this.mockMvc.perform(get(ADD_OR_REMOVE_LOAN_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_LOANS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_LOANS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove loans view - service exception")
    void getRequestServiceException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(loanService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(ADD_OR_REMOVE_LOAN_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post submit - throws service exception")
    void postSubmitRequestThrowsServiceException() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADD_OR_REMOVE_LOAN_PATH + "?submit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post add loan - success")
    void postLoanAddRequestSuccess() throws Exception {

        when(loanService.createLoan(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoanToAdd.class)))
                .thenReturn(validationErrors);

        this.mockMvc.perform(post(ADD_OR_REMOVE_LOAN_PATH + "?add")
                .param("loanToAdd.directorName", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + ADD_OR_REMOVE_LOAN_PATH));
    }

    @Test
    @DisplayName("Post add loan - throws service exception")
    void postLoanAddRequestThrowsServiceException() throws Exception {

        when(loanService.createLoan(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(LoanToAdd.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADD_OR_REMOVE_LOAN_PATH + "?add")
                .param("loanToAdd.directorName", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedLoansToDirectors()).thenReturn(true);
        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedLoansToDirectors()).thenReturn(false);
        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}