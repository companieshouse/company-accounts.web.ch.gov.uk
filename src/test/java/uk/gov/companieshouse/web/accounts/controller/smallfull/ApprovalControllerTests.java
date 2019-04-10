package uk.gov.companieshouse.web.accounts.controller.smallfull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApprovalControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ApprovalService approvalService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private ApprovalController approvalController;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String APPROVAL_VIEW = "smallfull/approval";

    private static final String APPROVAL_PATH = "/company/" + COMPANY_NUMBER +
                                                "/transaction/" + TRANSACTION_ID +
                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                "/small-full/approval";

    private static final String CONFIRMATION_VIEW = "/transaction/" + TRANSACTION_ID +
                                                    "/confirmation";

    private static final String BACK_PAGE_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String APPROVAL_MODEL_ATTR = "approval";

    private static final String TRANSACTION_ID_MODEL_ATTR = "transaction_id";

    private static final String COMPANY_ACCOUNTS_ID_MODEL_ATTR = "company_accounts_id";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {
        when(navigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        this.mockMvc = MockMvcBuilders.standaloneSetup(approvalController).build();
    }

    @Test
    @DisplayName("Get approval view success path")
    void getRequestSuccess() throws Exception {
        this.mockMvc.perform(get(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(APPROVAL_VIEW))
                .andExpect(model().attributeExists(BACK_PAGE_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(APPROVAL_MODEL_ATTR))
                .andExpect(model().attributeExists(TRANSACTION_ID_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post approval date validation failure path")
    void postRequestApprovalDateValidationFailure() throws Exception {

        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError());

        when(approvalService.validateApprovalDate(any(Approval.class))).thenReturn(validationErrors);

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(APPROVAL_VIEW))
                .andExpect(model().attributeExists(APPROVAL_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post approval api validation failure path")
    void postRequestApiValidationFailure() throws Exception {

        when(approvalService.validateApprovalDate(any(Approval.class))).thenReturn(new ArrayList<>());

        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError());

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenReturn(validationErrors);

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(APPROVAL_VIEW))
                .andExpect(model().attributeExists(APPROVAL_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post approval submit approval exception failure path")
    void postRequestSubmitApprovalExceptionFailure() throws Exception {

        when(approvalService.validateApprovalDate(any(Approval.class))).thenReturn(new ArrayList<>());

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post approval close transaction exception failure path")
    void postRequestCloseTransactionExceptionFailure() throws Exception {

        when(approvalService.validateApprovalDate(any(Approval.class))).thenReturn(new ArrayList<>());

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenReturn(new ArrayList<>());

        doThrow(ServiceException.class).when(transactionService).closeTransaction(TRANSACTION_ID);

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post approval success path")
    void postRequestSuccess() throws Exception {

        when(approvalService.validateApprovalDate(any(Approval.class))).thenReturn(new ArrayList<>());

        when(approvalService.submitApproval(anyString(), anyString(), any(Approval.class)))
                .thenReturn(new ArrayList<>());

        doNothing().when(transactionService).closeTransaction(TRANSACTION_ID);

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + CONFIRMATION_VIEW));
    }
}
