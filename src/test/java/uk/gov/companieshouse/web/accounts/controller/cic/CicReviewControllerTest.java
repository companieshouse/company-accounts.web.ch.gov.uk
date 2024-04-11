package uk.gov.companieshouse.web.accounts.controller.cic;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicReview;
import uk.gov.companieshouse.web.accounts.service.cic.CicReviewService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CicReviewControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String REVIEW_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/cic/review";

    private static final String REVIEW_VIEW = "cic/cicReview";

    private static final String REVIEW_MODEL_ATTR = "cicReview";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH =
        UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";


    @Mock
    private CicReviewService cicReviewService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private CicReview cicReview;

    private MockMvc mockMvc;

    @InjectMocks
    private CicReviewController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get review page success path")
    void getRequestSuccess() throws Exception {

        when(cicReviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(cicReview);

        this.mockMvc.perform(get(REVIEW_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(REVIEW_VIEW))
            .andExpect(model().attributeExists(REVIEW_MODEL_ATTR));

        verify(cicReviewService, times(1))
            .getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Get review page failure path")
    void getRequestFailureWithServiceException() throws Exception {

        doThrow(ServiceException.class)
            .when(cicReviewService).getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(get(REVIEW_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post review page success path")
    void postRequestSuccess() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(REVIEW_PATH))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }
}
