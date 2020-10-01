package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.servlet.http.HttpServletRequest;
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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportReview;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportReviewService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsReportReviewControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private DirectorsReportReviewService directorsReportReviewService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private DirectorsReportService directorsReportService;

    @Mock
    private DirectorsReportApi directorsReportApi;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private DirectorsReportReview directorsReportReview;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @InjectMocks
    private DirectorsReportReviewController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DIRECTORS_REPORT_REVIEW_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/directors-report/review";

    private static final String DIRECTORS_REPORT_REVIEW_MODEL_ATTR = "directorsReportReview";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String DIRECTORS_REPORT_REVIEW_VIEW = "smallfull/directorsReportReview";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get directors report review view - success path")
    void getRequestSuccess() throws Exception {

        when(directorsReportReviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(directorsReportReview);

        this.mockMvc.perform(get(DIRECTORS_REPORT_REVIEW_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REPORT_REVIEW_VIEW))
                .andExpect(model().attributeExists(DIRECTORS_REPORT_REVIEW_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get directors report review view - service exception")
    void getRequestServiceException() throws Exception {

        when(directorsReportReviewService.getReview(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(DIRECTORS_REPORT_REVIEW_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post directors report review view")
    void postRequest() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(DIRECTORS_REPORT_REVIEW_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
