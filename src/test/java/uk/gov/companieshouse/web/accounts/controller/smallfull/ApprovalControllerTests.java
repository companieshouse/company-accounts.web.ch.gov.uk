package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.request.TransactionsGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class ApprovalControllerTests {

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String APPROVAL_VIEW = "smallfull/approval";

    private static final String APPROVAL_PATH = "/company/" + COMPANY_NUMBER +
                                                "/transaction/" + TRANSACTION_ID +
                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                "/small-full/approval";

    private static final String SUBMITTED_ACCOUNTS_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/approved-accounts";

    private static final String CONFIRMATION_VIEW = "/transaction/" + TRANSACTION_ID +
                                                    "/confirmation";

    private static final String BACK_PAGE_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String APPROVAL_MODEL_ATTR = "approval";

    private static final String TRANSACTION_ID_MODEL_ATTR = "transaction_id";

    private static final String COMPANY_ACCOUNTS_ID_MODEL_ATTR = "company_accounts_id";

    private static final String IS_PAYABLE_TRANSACTION_ATTR = "isPayableTransaction";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String SUMMARY_FALSE_PARAMETER = "?summary=false";

    private static final String PAYMENT_WEB_ENDPOINT = "/paymentWebEndpoint";

    private static final String DIRECTOR_NAME = "directorName";

    private static final String NAME = "name";

    private MockMvc mockMvc;

    @Mock
    private ApprovalService approvalService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private DirectorService directorService;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private TransactionsResourceHandler transactionsResourceHandler;

    @Mock
    private TransactionsGet transactionsGet;

    @Mock
    private ApiResponse<Transaction> responseWithData;

    @Mock
    private Transaction transaction;

    @InjectMocks
    private ApprovalController approvalController;

    @BeforeEach
    private void setup() {
        when(navigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        this.mockMvc = MockMvcBuilders.standaloneSetup(approvalController).build();
    }

    @Test
    @DisplayName("Get approval view success path")
    void getRequestSuccess() throws Exception {

        when (directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, true)).thenReturn(new Director[]{});

        this.mockMvc.perform(get(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(APPROVAL_VIEW))
                .andExpect(model().attributeExists(BACK_PAGE_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(APPROVAL_MODEL_ATTR))
                .andExpect(model().attributeExists(TRANSACTION_ID_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID_MODEL_ATTR))
                .andExpect(model().attributeExists(IS_PAYABLE_TRANSACTION_ATTR));
    }

    @Test
    @DisplayName("Get approval - throws service exception")
    void getRequestServiceException() throws Exception {

        when(transactionService.isPayableTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Get approval - approver options is equal to 1")
    void getRequestApproverOptionsIsOne() throws Exception {

        Director director = new Director();
        director.setName(DIRECTOR_NAME);
        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, true)).thenReturn(new Director[]{director});

        this.mockMvc.perform(get(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(model().attribute(APPROVAL_MODEL_ATTR, hasProperty(DIRECTOR_NAME, is(DIRECTOR_NAME))))
                .andExpect(model().attributeExists(BACK_PAGE_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(APPROVAL_MODEL_ATTR))
                .andExpect(model().attributeExists(TRANSACTION_ID_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID_MODEL_ATTR))
                .andExpect(model().attributeExists(IS_PAYABLE_TRANSACTION_ATTR));
    }

    @Test
    @DisplayName("Post approval api validation failure path")
    void postRequestApiValidationFailure() throws Exception {

        when(transactionService.getTransaction(TRANSACTION_ID)).thenReturn(transaction);

        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError());

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenReturn(validationErrors);

        this.mockMvc.perform(post(APPROVAL_PATH)
                .param(DIRECTOR_NAME, NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(APPROVAL_VIEW))
                .andExpect(model().attributeExists(APPROVAL_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post request with BindingResult errors")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(APPROVAL_VIEW));

    }

    @Test
    @DisplayName("Post approval submit approval exception failure path")
    void postRequestSubmitApprovalExceptionFailure() throws Exception {

        when(transactionService.getTransaction(TRANSACTION_ID)).thenReturn(transaction);

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(APPROVAL_PATH)
                .param(DIRECTOR_NAME, NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post approval close transaction exception failure path")
    void postRequestCloseTransactionExceptionFailure() throws Exception {

        when(transactionService.getTransaction(TRANSACTION_ID)).thenReturn(transaction);

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenReturn(new ArrayList<>());

        doThrow(ServiceException.class).when(transactionService).closeTransaction(TRANSACTION_ID);

        this.mockMvc.perform(post(APPROVAL_PATH)
                .param(DIRECTOR_NAME, NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post approval success path for non-payable transaction")
    void postRequestSuccessForNonPayableTransaction() throws Exception {

        when(transactionService.getTransaction(TRANSACTION_ID)).thenReturn(transaction);

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenReturn(new ArrayList<>());

        when(transactionService.closeTransaction(TRANSACTION_ID)).thenReturn(false);

        this.mockMvc.perform(post(APPROVAL_PATH)
                .param(DIRECTOR_NAME, NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + CONFIRMATION_VIEW));
    }

    @Test
    @DisplayName("Post approval success path for payable transaction")
    void postRequestSuccessForPayableTransaction() throws Exception {

        when(transactionService.getTransaction(TRANSACTION_ID)).thenReturn(transaction);

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenReturn(new ArrayList<>());

        when(transactionService.closeTransaction(TRANSACTION_ID)).thenReturn(true);

        when(paymentService.createPaymentSessionForTransaction(TRANSACTION_ID)).thenReturn(PAYMENT_WEB_ENDPOINT);

        this.mockMvc.perform(post(APPROVAL_PATH)
                .param(DIRECTOR_NAME, NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + PAYMENT_WEB_ENDPOINT + SUMMARY_FALSE_PARAMETER));
    }

    @Test
    @DisplayName("Post approval redirect for closed pending payment")
    void postRequestSuccessClosedPendingPayment() throws Exception {

        Transaction createdTransaction = new Transaction();

        createdTransaction.setStatus(TransactionStatus.CLOSED_PENDING_PAYMENT);

        when(transactionService.getTransaction(TRANSACTION_ID)).thenReturn(createdTransaction);

        this.mockMvc.perform(post(APPROVAL_PATH)
                .param(DIRECTOR_NAME, NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + SUBMITTED_ACCOUNTS_PATH));
    }
}
