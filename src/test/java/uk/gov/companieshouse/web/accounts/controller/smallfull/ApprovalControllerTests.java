package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
    private TransactionService transactionService;

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

    private static final String ERROR_VIEW = "error";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(approvalController).build();
    }

    @Test
    @DisplayName("Get approval view success path")
    void getRequestSuccess() throws Exception {
        this.mockMvc.perform(get(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(APPROVAL_VIEW))
                .andExpect(model().attributeExists(BACK_PAGE_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post approval success path")
    void postRequestSuccess() throws Exception {

        doNothing().when(transactionService).closeTransaction(TRANSACTION_ID);

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + CONFIRMATION_VIEW));
    }

    @Test
    @DisplayName("Post approval failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
                .when(transactionService).closeTransaction(anyString());

        this.mockMvc.perform(post(APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }
}
