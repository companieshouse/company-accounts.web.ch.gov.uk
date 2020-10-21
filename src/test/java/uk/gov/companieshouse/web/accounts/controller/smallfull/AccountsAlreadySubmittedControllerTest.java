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

    private static final String DROPOUT_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/approved-accounts";

    private static final String DROPOUT_VIEW = "smallfull/paymentSessionDropout";

    private MockMvc mockMvc;

    private AccountsAlreadySubmittedController paymentDropoutController = new AccountsAlreadySubmittedController();

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(paymentDropoutController).build();
    }

    @Test
    @DisplayName("Get payment session dropout success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(DROPOUT_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(DROPOUT_VIEW));
    }

    @Test
    @DisplayName("Post dropout session success path")
    void postRequestSuccess() throws Exception {

        this.mockMvc.perform(post(DROPOUT_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + YOUR_FILINGS_PATH));
    }
}