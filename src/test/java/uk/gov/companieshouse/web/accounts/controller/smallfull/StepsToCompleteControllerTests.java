package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StepsToCompleteControllerTests {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CompanyAccountsService companyAccountsService;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private StatementsService statementsService;

    @Mock
    private NavigatorService navigatorService;

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

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get steps to complete view success path")
    void getRequestSuccess() throws Exception {

        when(navigatorService.getPreviousControllerPath(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(STEPS_TO_COMPLETE_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post steps to complete success path")
    void postRequestSuccess() throws Exception {

        when(transactionService.createTransaction(COMPANY_NUMBER)).thenReturn(TRANSACTION_ID);

        when(companyAccountsService.createCompanyAccounts(TRANSACTION_ID)).thenReturn(COMPANY_ACCOUNTS_ID);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        doNothing().when(smallFullService).createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        doNothing().when(statementsService).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(transactionService, times(1)).createTransaction(COMPANY_NUMBER);

        verify(companyAccountsService, times(1)).createCompanyAccounts(TRANSACTION_ID);

        verify(smallFullService, times(1)).createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(statementsService, times(1)).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
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

        doNothing().when(smallFullService).createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        doThrow(ServiceException.class)
                .when(statementsService).createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(post(STEPS_TO_COMPLETE_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
