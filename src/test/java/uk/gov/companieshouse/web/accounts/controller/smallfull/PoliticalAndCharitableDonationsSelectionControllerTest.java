package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonationsSelection;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsSelectionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PoliticalAndCharitableDonationsSelectionControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_PATH =
            "/company/" + COMPANY_NUMBER +
                    "/transaction/" + TRANSACTION_ID +
                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                    "/small-full/directors-report/political-and-charitable-donations-question";
    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_MODEL_ATTR = "politicalAndCharitableDonationsSelection";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_VIEW = "smallfull/politicalAndCharitableDonationsSelection";
    private static final String ERROR_VIEW = "error";
    private static final String HAS_POLITICAL_AND_CHARITABLE_DONATIONS = "hasPoliticalAndCharitableDonations";
    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";
    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";
    private MockMvc mockMvc;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PoliticalAndCharitableDonationsSelectionService service;
    @Mock
    private DirectorsReportService directorsReportService;
    @Mock
    private DirectorsReportApi directorsReportApi;
    @Mock
    private NavigatorService navigatorService;
    @InjectMocks
    private PoliticalAndCharitableDonationsSelectionController controller;
    @Mock
    private PoliticalAndCharitableDonationsSelection politicalAndCharitableDonationsSelection;
    @Mock
    private HttpSession httpSession;
    @Mock
    private ApiClient apiClient;
    @Mock
    private ApiClientService apiClientService;
    @Mock
    private CompanyAccountsDataState companyAccountsDataState;
    @Mock
    private DirectorsReportStatements directorsReportStatements;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get request - success - hasPoliticalAndCharitableDonations set from db")
    void getPoliticalAndCharitableDonationsSelectionSuccessHasSelectionSetFromDB()
            throws Exception {

        when(service.getPoliticalAndCharitableDonationsSelection(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(politicalAndCharitableDonationsSelection);

        when(politicalAndCharitableDonationsSelection.getHasPoliticalAndCharitableDonations()).thenReturn(
                true);

        this.mockMvc.perform(get(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_VIEW))
                .andExpect(model().attributeExists(
                        POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        // verify no interactions with the data state
        verify(request, never()).getSession();
    }

    @Test
    @DisplayName("Get request - success - hasPoliticalAndCharitableDonations derived from data state")
    void getPoliticalAndCharitableDonationsSelectionSuccessHasSelectionDerivedFromDataState()
            throws Exception {

        when(service.getPoliticalAndCharitableDonationsSelection(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenReturn(politicalAndCharitableDonationsSelection);

        when(politicalAndCharitableDonationsSelection.getHasPoliticalAndCharitableDonations()).thenReturn(
                null);

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(
                companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(
                directorsReportStatements);
        when(directorsReportStatements.getHasProvidedPoliticalAndCharitableDonations()).thenReturn(
                false);

        this.mockMvc.perform(get(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_VIEW))
                .andExpect(model().attributeExists(
                        POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(politicalAndCharitableDonationsSelection).setHasPoliticalAndCharitableDonations(
                false);

    }

    @Test
    @DisplayName("Get request - service exception")
    void getPoliticalAndCharitableDonationsSelectionThrowsServiceException() throws Exception {

        when(service.getPoliticalAndCharitableDonationsSelection(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - success")
    void postPoliticalAndCharitableDonationsSuccess() throws Exception {

        doNothing()
                .when(service)
                .submitPoliticalAndCharitableDonationsSelection(eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID),
                        any(PoliticalAndCharitableDonationsSelection.class));

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(
                companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(
                directorsReportStatements);

        when(navigatorService.getNextControllerRedirect(any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_PATH)
                        .param(HAS_POLITICAL_AND_CHARITABLE_DONATIONS, "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(directorsReportStatements).setHasProvidedPoliticalAndCharitableDonations(true);

        verify(httpSession).setAttribute(COMPANY_ACCOUNTS_DATA_STATE, companyAccountsDataState);
    }

    @Test
    @DisplayName("Post request - service exception")
    void postPoliticalAndCharitableDonationsServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(service)
                .submitPoliticalAndCharitableDonationsSelection(eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID),
                        any(PoliticalAndCharitableDonationsSelection.class));

        this.mockMvc.perform(post(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_PATH)
                        .param(HAS_POLITICAL_AND_CHARITABLE_DONATIONS, "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - binding result errors")
    void postPoliticalAndCharitableDonationsWithBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(POLITICAL_AND_CHARITABLE_DONATIONS_SELECTION_VIEW));

        verify(service, never())
                .submitPoliticalAndCharitableDonationsSelection(eq(TRANSACTION_ID),
                        eq(COMPANY_ACCOUNTS_ID),
                        any(PoliticalAndCharitableDonationsSelection.class));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(null);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }


}
