package uk.gov.companieshouse.web.accounts.controller.payment;

import static org.mockito.Mockito.doThrow;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PayFilingFeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PayFilingFeeController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String PAY_FILING_FEE_VIEW = "payment/payFilingFee";

    private static final String PAY_FILING_FEE_MODEL_ATTR = "payFilingFeeChoice";

    private static final String NO_CHOICE_URL = "/user/transactions";

    private static final String ERROR_VIEW = "error";

    private static final String PAY_FILING_FEE_PATH =
        "/company/"+COMPANY_NUMBER+"/transaction/"+TRANSACTION_ID+"/company-accounts/"+COMPANY_ACCOUNTS_ID+"/pay-filing-fee";

    @Test
    @DisplayName("Pay filing fee get request - Success")
    void getPayFilingFeeSuccess() throws Exception {

        this.mockMvc.perform(get(PAY_FILING_FEE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(PAY_FILING_FEE_VIEW))
                .andExpect(model().attributeExists(PAY_FILING_FEE_MODEL_ATTR));
    }

    @Test
    @DisplayName("Pay filing fee post request - Yes")
    void postPayFilingFeeYesChoice() throws Exception {
        mockMvc.perform(post(PAY_FILING_FEE_PATH).
            param(PAY_FILING_FEE_MODEL_ATTR, "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX+paymentService.createPaymentSessionForTransaction(TRANSACTION_ID)));
    }

    @Test
    @DisplayName("Pay filing fee post request - No")
    void postPayFilingFeeNoChoice() throws Exception {
        mockMvc.perform(post(PAY_FILING_FEE_PATH).
            param(PAY_FILING_FEE_MODEL_ATTR, "0"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX+NO_CHOICE_URL));
    }

    @Test
    @DisplayName("Pay filing fee post request - No Decision Made")
    void postPayFilingFeeNoChoiceMade() throws Exception {
        mockMvc.perform(post(PAY_FILING_FEE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(PAY_FILING_FEE_VIEW));
    }

    @Test
    @DisplayName("Pay filing fee throws exception")
    void payFilingFeeThrowsException() throws Exception {
        doThrow(ServiceException.class)
            .when(paymentService).createPaymentSessionForTransaction(TRANSACTION_ID);

        this.mockMvc.perform(post(PAY_FILING_FEE_PATH)
            .param(PAY_FILING_FEE_MODEL_ATTR, "1"))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }
}
