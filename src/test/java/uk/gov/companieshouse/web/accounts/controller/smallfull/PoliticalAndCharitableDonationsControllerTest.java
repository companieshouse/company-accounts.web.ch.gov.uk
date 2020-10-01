package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.PoliticalAndCharitableDonations;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PoliticalAndCharitableDonationsService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PoliticalAndCharitableDonationsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private PoliticalAndCharitableDonationsService service;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private PoliticalAndCharitableDonationsController controller;

    @Mock
    private PoliticalAndCharitableDonations politicalAndCharitableDonations;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private HttpSession httpSession;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private DirectorsReportStatements directorsReportStatements;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_PATH = "/company/" + COMPANY_NUMBER + "/transaction/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID + "/small-full/directors-report/political-and-charitable-donations";

    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_MODEL_ATTR = "politicalAndCharitableDonations";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_VIEW = "smallfull/politicalAndCharitableDonations";

    private static final String ERROR_VIEW = "error";

    private static final String POLITICAL_AND_CHARITABLE_DONATIONS_DETAILS = "politicalAndCharitableDonationsDetails";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get request - success")
    void getPoliticalAndCharitableDonationsSuccess() throws Exception {

        when(service.getPoliticalAndCharitableDonations(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(politicalAndCharitableDonations);

        this.mockMvc.perform(get(POLITICAL_AND_CHARITABLE_DONATIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(POLITICAL_AND_CHARITABLE_DONATIONS_VIEW))
                .andExpect(model().attributeExists(POLITICAL_AND_CHARITABLE_DONATIONS_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get request - service exception")
    void getPoliticalAndCharitableDonationsThrowsServiceException() throws Exception {

        when(service.getPoliticalAndCharitableDonations(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(POLITICAL_AND_CHARITABLE_DONATIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - success")
    void postPoliticalAndCharitableDonationsSuccess() throws Exception {

        when(service
                .submitPoliticalAndCharitableDonations(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(PoliticalAndCharitableDonations.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(POLITICAL_AND_CHARITABLE_DONATIONS_PATH)
                .param(POLITICAL_AND_CHARITABLE_DONATIONS_DETAILS, POLITICAL_AND_CHARITABLE_DONATIONS_DETAILS))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post request - validation errors")
    void postPoliticalAndCharitableDonationsWithValidationErrors() throws Exception {

        when(service
                .submitPoliticalAndCharitableDonations(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(PoliticalAndCharitableDonations.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(POLITICAL_AND_CHARITABLE_DONATIONS_PATH)
                .param(POLITICAL_AND_CHARITABLE_DONATIONS_DETAILS, POLITICAL_AND_CHARITABLE_DONATIONS_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(POLITICAL_AND_CHARITABLE_DONATIONS_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(), ArgumentMatchers.<String>any());
    }

    @Test
    @DisplayName("Post request - service exception")
    void postPoliticalAndCharitableDonationsThrowsServiceException() throws Exception {

        when(service
                .submitPoliticalAndCharitableDonations(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(PoliticalAndCharitableDonations.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(POLITICAL_AND_CHARITABLE_DONATIONS_PATH)
                .param(POLITICAL_AND_CHARITABLE_DONATIONS_DETAILS, POLITICAL_AND_CHARITABLE_DONATIONS_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - binding result errors")
    void postPoliticalAndCharitableDonationsBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(POLITICAL_AND_CHARITABLE_DONATIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(POLITICAL_AND_CHARITABLE_DONATIONS_VIEW));

        verify(service, never())
                .submitPoliticalAndCharitableDonations(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(PoliticalAndCharitableDonations.class));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(directorsReportStatements);
        when(directorsReportStatements.getHasProvidedPoliticalAndCharitableDonations()).thenReturn(false);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(directorsReportStatements);
        when(directorsReportStatements.getHasProvidedPoliticalAndCharitableDonations()).thenReturn(true);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
