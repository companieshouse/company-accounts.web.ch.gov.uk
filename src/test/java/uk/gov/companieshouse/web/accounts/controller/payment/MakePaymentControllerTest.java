package uk.gov.companieshouse.web.accounts.controller.payment;

import static org.mockito.ArgumentMatchers.anyString;
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
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MakePaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private MakePaymentController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String MAKE_PAYMENT_VIEW = "payment/makePayment";

    private static final String MAKE_PAYMENT_MODEL_ATTR = "makePaymentChoice";

    private static final String NO_CHOICE_URL = "/user/transactions";

    private static final String ERROR_VIEW = "error";

    private static final String MAKE_PAYMENT_PATH =
        "/company/"+COMPANY_NUMBER+"/transaction/"+TRANSACTION_ID+"/company-accounts/"+COMPANY_ACCOUNTS_ID+"/do-you-want-to-make-a-payment";

    @Test
    @DisplayName("Make payment get request - Success")
    void getMakePaymentSuccess() throws Exception {

        this.mockMvc.perform(get(MAKE_PAYMENT_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(MAKE_PAYMENT_VIEW))
                .andExpect(model().attributeExists(MAKE_PAYMENT_MODEL_ATTR));
    }

    @Test
    @DisplayName("Make payment post request - Yes")
    void postMakePaymentYesChoice() throws Exception {
        mockMvc.perform(post(MAKE_PAYMENT_PATH).
            param(MAKE_PAYMENT_MODEL_ATTR, "1"))
            .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Make payment post request - No")
    void postMakePaymentNoChoice() throws Exception {
        mockMvc.perform(post(MAKE_PAYMENT_PATH).
            param(MAKE_PAYMENT_MODEL_ATTR, "0"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX+NO_CHOICE_URL));
    }

    @Test
    @DisplayName("Make payment post request - No Decision Made")
    void postMakePaymentNoChoiceMade() throws Exception {
        mockMvc.perform(post(MAKE_PAYMENT_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(MAKE_PAYMENT_VIEW));
    }

    @Test
    @DisplayName("Make payment throws exception")
    void makePaymentThrowsException() throws Exception {
        doThrow(ServiceException.class)
            .when(paymentService).createPaymentSessionForTransaction(TRANSACTION_ID);

        this.mockMvc.perform(post(MAKE_PAYMENT_PATH)
            .param(MAKE_PAYMENT_MODEL_ATTR, "1"))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }
}
