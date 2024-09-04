package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.List;
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
import uk.gov.companieshouse.web.accounts.model.directorsreport.PrincipalActivities;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.PrincipalActivitiesService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrincipalActivitiesControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String PRINCIPAL_ACTIVITIES_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/directors-report/principal-activities";
    private static final String PRINCIPAL_ACTIVITIES_MODEL_ATTR = "principalActivities";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String PRINCIPAL_ACTIVITIES_VIEW = "smallfull/principalActivities";
    private static final String ERROR_VIEW = "error";
    private static final String PRINCIPAL_ACTIVITIES_DETAILS = "principalActivitiesDetails";
    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";
    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";
    private MockMvc mockMvc;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PrincipalActivitiesService principalActivitiesService;
    @Mock
    private NavigatorService navigatorService;
    @InjectMocks
    private PrincipalActivitiesController controller;
    @Mock
    private PrincipalActivities principalActivities;
    @Mock
    private List<ValidationError> validationErrors;
    @Mock
    private HttpSession httpSession;
    @Mock
    private CompanyAccountsDataState companyAccountsDataState;
    @Mock
    private DirectorsReportStatements directorsReportStatements;

    @BeforeEach
    public void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get request - success")
    void getPrincipalActivitiesSuccess() throws Exception {

        when(principalActivitiesService.getPrincipalActivities(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(principalActivities);

        this.mockMvc.perform(get(PRINCIPAL_ACTIVITIES_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(PRINCIPAL_ACTIVITIES_VIEW))
                .andExpect(model().attributeExists(PRINCIPAL_ACTIVITIES_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get request - service exception")
    void getPrincipalActivitiesThrowsServiceException() throws Exception {

        when(principalActivitiesService.getPrincipalActivities(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(PRINCIPAL_ACTIVITIES_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - success")
    void postPrincipalActivitiesSuccess() throws Exception {

        when(principalActivitiesService
                .submitPrincipalActivities(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                        any(PrincipalActivities.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(PRINCIPAL_ACTIVITIES_PATH)
                        .param(PRINCIPAL_ACTIVITIES_DETAILS, PRINCIPAL_ACTIVITIES_DETAILS))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post request - validation errors")
    void postPrincipalActivitiesWithValidationErrors() throws Exception {

        when(principalActivitiesService
                .submitPrincipalActivities(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                        any(PrincipalActivities.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(PRINCIPAL_ACTIVITIES_PATH)
                        .param(PRINCIPAL_ACTIVITIES_DETAILS, PRINCIPAL_ACTIVITIES_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(PRINCIPAL_ACTIVITIES_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(),
                ArgumentMatchers.<String>any());
    }

    @Test
    @DisplayName("Post request - service exception")
    void postPrincipalActivitiesThrowsServiceException() throws Exception {

        when(principalActivitiesService
                .submitPrincipalActivities(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                        any(PrincipalActivities.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(PRINCIPAL_ACTIVITIES_PATH)
                        .param(PRINCIPAL_ACTIVITIES_DETAILS, PRINCIPAL_ACTIVITIES_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - binding result errors")
    void postPrincipalActivitiesBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(PRINCIPAL_ACTIVITIES_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(PRINCIPAL_ACTIVITIES_VIEW));

        verify(principalActivitiesService, never())
                .submitPrincipalActivities(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                        any(PrincipalActivities.class));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(
                companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(
                directorsReportStatements);
        when(directorsReportStatements.getHasProvidedPrincipalActivities()).thenReturn(false);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(
                companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(
                directorsReportStatements);
        when(directorsReportStatements.getHasProvidedPrincipalActivities()).thenReturn(true);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
