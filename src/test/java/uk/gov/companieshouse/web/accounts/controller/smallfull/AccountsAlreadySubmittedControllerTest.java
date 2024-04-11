package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountsAlreadySubmittedControllerTest {
    private static final String YOUR_FILINGS_PATH = "/user/transactions";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SUBMITTED_ACCOUNTS_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/approved-accounts";

    private static final String SUBMITTED_ACCOUNTS_VIEW = "smallfull/accountsAlreadySubmitted";

    private MockMvc mockMvc;

    private AccountsAlreadySubmittedController accountsAlreadySubmittedController = new AccountsAlreadySubmittedController();

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountsAlreadySubmittedController).build();
    }

    @Test
    @DisplayName("Get payment session for acccounts already submitted success path")
    void getRequestSuccess() throws Exception {
        this.mockMvc.perform(get(SUBMITTED_ACCOUNTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(SUBMITTED_ACCOUNTS_VIEW));
    }

    @Test
    @DisplayName("Post accounts already submitted session success path")
    void postRequestSuccess() throws Exception {
        this.mockMvc.perform(post(SUBMITTED_ACCOUNTS_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + YOUR_FILINGS_PATH));
    }
}