package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StepsToCompleteControllerTests {
    @Captor
    private ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CompanyAccountsService companyAccountsService;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private CompanyAccountsApi companyAccountsApi;

    @Mock
    private CompanyAccountsLinks companyAccountsLinks;

    @Mock
    private SmallFullApi smallFull;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private StatementsService statementsService;

    @Mock
    private Statements statements;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private CompanyService companyService;

    @Mock
    private CompanyProfileApi companyProfile;

    @Mock
    private CurrentPeriodService currentPeriodService;

    @Mock
    private PreviousPeriodService previousPeriodService;

    @InjectMocks
    private StepsToCompleteController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String STEPS_TO_COMPLETE_PATH = "/company/" + COMPANY_NUMBER +
            "/small-full/steps-to-complete";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String STEPS_TO_COMPLETE_VIEW = "smallfull/stepsToComplete";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String SMALL_FULL_LINK = "smallFullLink";

    private static final String CURRENT_PERIOD_LINK = "currentPeriodLink";

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get steps to complete view success path")
    void getRequestSuccess() throws Exception {
        when(navigatorService.getPreviousControllerPath(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(STEPS_TO_COMPLETE_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post steps to complete success path - first year filer")
    void postRequestSuccessFirstYearFiler() throws Exception {
        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        when(companyAccountsService.createCompanyAccounts(TRANSACTION_ID)).thenReturn(COMPANY_ACCOUNTS_ID);

        when(companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(companyAccountsApi);

        when(companyAccountsApi.getLinks()).thenReturn(companyAccountsLinks);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        doNothing().when(statementsService).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFull);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(false);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(transactionService, times(1)).createTransaction(COMPANY_NUMBER);

        verify(companyAccountsService, times(1)).createCompanyAccounts(TRANSACTION_ID);

        verify(statementsService, times(1)).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(currentPeriodService).submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                CurrentPeriodApi.class), anyList());

        verify(previousPeriodService, never()).submitPreviousPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                PreviousPeriodApi.class), anyList());
    }

    @Test
    @DisplayName("Post steps to complete success path - multi year filer")
    void postRequestSuccessMultiYearFiler() throws Exception {
        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        when(companyAccountsService.createCompanyAccounts(TRANSACTION_ID)).thenReturn(COMPANY_ACCOUNTS_ID);

        when(companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(companyAccountsApi);

        when(companyAccountsApi.getLinks()).thenReturn(companyAccountsLinks);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        doNothing().when(statementsService).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFull);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfile);

        when(companyService.isMultiYearFiler(companyProfile)).thenReturn(true);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(transactionService, times(1)).createTransaction(COMPANY_NUMBER);

        verify(companyAccountsService, times(1)).createCompanyAccounts(TRANSACTION_ID);

        verify(statementsService, times(1)).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(currentPeriodService).submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                CurrentPeriodApi.class), anyList());

        verify(previousPeriodService).submitPreviousPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                PreviousPeriodApi.class), anyList());
    }

    @Test
    @DisplayName("Post steps to complete success path with pre-existing resources")
    void postRequestSuccessPreExistingResources() throws Exception {
        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        when(companyAccountsService.createCompanyAccounts(TRANSACTION_ID)).thenReturn(COMPANY_ACCOUNTS_ID);

        when(companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(companyAccountsApi);

        when(companyAccountsApi.getLinks()).thenReturn(companyAccountsLinks);

        when(companyAccountsLinks.getSmallFullAccounts()).thenReturn(SMALL_FULL_LINK);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFull);

        when(smallFull.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getCurrentPeriod()).thenReturn(CURRENT_PERIOD_LINK);

        when(statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(statements);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(transactionService, times(1)).createTransaction(COMPANY_NUMBER);

        verify(companyAccountsService, times(1)).createCompanyAccounts(TRANSACTION_ID);

        verify(smallFullService, never()).createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(statementsService, never()).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(currentPeriodService, never()).submitCurrentPeriod(eq(apiClient), eq(smallFull), eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                CurrentPeriodApi.class), anyList());
    }

    @Test
    @DisplayName("Post steps to complete failure path for transaction service")
    void postRequestTransactionServiceFailure() throws Exception {
        doThrow(ServiceException.class)
                .when(transactionService).createTransaction(anyString());

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post steps to complete failure path for create company accounts resource")
    void postRequestCompanyAccountsServiceCreateCompanyAccountsFailure() throws Exception {
        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        doThrow(ServiceException.class)
                .when(companyAccountsService).createCompanyAccounts(TRANSACTION_ID);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post steps to complete failure path for create small full accounts resource")
    void postRequestCompanyAccountsServiceCreateSmallFullAccountsFailure() throws Exception {
        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        when(companyAccountsService.createCompanyAccounts(TRANSACTION_ID)).thenReturn(COMPANY_ACCOUNTS_ID);

        when(companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(companyAccountsApi);

        when(companyAccountsApi.getLinks()).thenReturn(companyAccountsLinks);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        doThrow(ServiceException.class)
                .when(smallFullService).createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post steps to complete failure path for create statements resource")
    void postRequestStatementsServiceCreateStatementsFailure() throws Exception {
        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        when(companyAccountsService.createCompanyAccounts(TRANSACTION_ID)).thenReturn(COMPANY_ACCOUNTS_ID);

        when(companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(companyAccountsApi);

        when(companyAccountsApi.getLinks()).thenReturn(companyAccountsLinks);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        doNothing().when(smallFullService).createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        when(statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        doThrow(ServiceException.class)
                .when(statementsService).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
