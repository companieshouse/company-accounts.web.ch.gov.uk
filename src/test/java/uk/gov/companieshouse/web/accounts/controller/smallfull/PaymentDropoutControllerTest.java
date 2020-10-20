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
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentDropoutControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private PaymentDropoutController paymentDropoutController;

    private static final String YOUR_FILINGS_PATH = "/user/transactions";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DROPOUT_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/approved-accounts";

    private static final String BACK_PAGE_MODEL_ATTR = "backButton";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String DROPOUT_VIEW = "smallfull/paymentSessionDropout";

    @BeforeEach
    private void setup() {
        when(navigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        this.mockMvc = MockMvcBuilders.standaloneSetup(paymentDropoutController).build();
    }

    @Test
    @DisplayName("Get payment session dropout success path")
    void getRequestSuccess() throws Exception {

        this.mockMvc.perform(get(DROPOUT_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(DROPOUT_VIEW))
                .andExpect(model().attributeExists(BACK_PAGE_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post dropout session success path")
    void postRequestSuccess() throws Exception {

        this.mockMvc.perform(post(DROPOUT_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + YOUR_FILINGS_PATH));
    }
}