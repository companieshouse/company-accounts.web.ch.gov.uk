package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
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
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddOrRemoveDirectorsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DirectorService directorService;

    @Mock
    private SecretaryService secretaryService;

    @Mock
    private DirectorsReportService directorsReportService;

    @Mock
    private DirectorsReportApi directorsReportApi;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private LoansToDirectorsApi loansToDirectorsApi;

    @Mock
    private LoansToDirectorsService loansToDirectorsService;

    @Mock
    private DirectorsReportApprovalService directorsReportApprovalService;

    @Mock
    private ApprovalService approvalService;

    @InjectMocks
    private AddOrRemoveDirectorsController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DIRECTOR_ID = "directorId";

    private static final String DIRECTOR_NAME = "directorName";

    private static final String SECRETARY_NAME = "secretaryName";

    private static final String ADD_OR_REMOVE_DIRECTORS_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/add-or-remove-directors";

    private static final String REMOVE_DIRECTOR_PATH = ADD_OR_REMOVE_DIRECTORS_PATH + "/remove/" + DIRECTOR_ID;

    private static final String ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR = "addOrRemoveDirectors";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ADD_OR_REMOVE_DIRECTORS_VIEW = "smallfull/addOrRemoveDirectors";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String DISPLAY_LTD_WARNING_BANNER = "displayLtdWarningBanner";

    private static final String DISPLAY_APPROVAL_WARNING_BANNER = "displayApprovalWarningBanner";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get add or remove directors view - success path")
    void getRequestSuccess() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false)).thenReturn(new Director[0]);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        when(loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(loansToDirectorsApi.getLoans()).thenReturn(null);

        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove directors view LTD resource is null- success path")
    void getRequestWithNullLTDResourceSuccess() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false)).thenReturn(new Director[0]);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        when(loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);
        
        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attribute(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR, hasProperty(DISPLAY_LTD_WARNING_BANNER, is(false))))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove directors view when a user has no LTD loans - success path")
    void getRequestUserHasNoLTDLoansSuccess() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false)).thenReturn(new Director[0]);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        when(loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(loansToDirectorsApi.getLoans()).thenReturn(new HashMap<String, String>());

        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attribute(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR, hasProperty(DISPLAY_LTD_WARNING_BANNER, is(false))))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove directors view when a user has existing LTD loans - success path")
    void getRequestUserHasLTDLoansSuccess() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false)).thenReturn(new Director[0]);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        when(loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(loansToDirectorsApi.getLoans()).thenReturn(createLoans());

        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attribute(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR, hasProperty(DISPLAY_LTD_WARNING_BANNER, is(true))))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove directors view when a directors report approval exists - success path")
    void getRequestUserHasDirectorsReportApprovalSuccess() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false)).thenReturn(new Director[0]);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        when(loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(loansToDirectorsApi.getLoans()).thenReturn(new HashMap<String, String>());
        
        DirectorsReportApproval directorsReportApproval = new DirectorsReportApproval();
        directorsReportApproval.setName(DIRECTOR_NAME);
        when(directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApproval);
        
        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attribute(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR, hasProperty(DISPLAY_APPROVAL_WARNING_BANNER, is(true))))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove directors view when an accounts approval exists - success path")
    void getRequestUserHasAccountsApprovalSuccess() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false)).thenReturn(new Director[0]);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        when(loansToDirectorsService.getLoansToDirectors(apiClientService.getApiClient(), TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(loansToDirectorsApi);

        when(loansToDirectorsApi.getLoans()).thenReturn(new HashMap<String, String>());
        
        when(directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(new DirectorsReportApproval());
        
        Approval approval = new Approval();
        approval.setDirectorName(DIRECTOR_NAME);
        when(approvalService.getApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(approval);
        
        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attribute(ADD_OR_REMOVE_DIRECTORS_MODEL_ATTR, hasProperty(DISPLAY_APPROVAL_WARNING_BANNER, is(true))))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove directors view - service exception")
    void getRequestServiceException() throws Exception {

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(ADD_OR_REMOVE_DIRECTORS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add director - throws service exception")
    void postDirectorAddRequestThrowsServiceException() throws Exception {


        when(directorService.createDirector(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorToAdd.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH + "?add")
                .param("directorToAdd.wasDirectorAppointedDuringPeriod", "0")
                .param("directorToAdd.didDirectorResignDuringPeriod", "0")
                .param("directorToAdd.name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add director - success")
    void postDirectorAddRequestSuccess() throws Exception {

        when(directorService.createDirector(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorToAdd.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH + "?add")
                .param("directorToAdd.wasDirectorAppointedDuringPeriod", "0")
                .param("directorToAdd.didDirectorResignDuringPeriod", "0")
                .param("directorToAdd.name", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + ADD_OR_REMOVE_DIRECTORS_PATH));
    }

    @Test
    @DisplayName("Post add director - throws validation errors")
    void postDirectorAddRequestThrowsValidationErrors() throws Exception {


        when(directorService.createDirector(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorToAdd.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH + "?add")
                .param("directorToAdd.wasDirectorAppointedDuringPeriod", "0")
                .param("directorToAdd.didDirectorResignDuringPeriod", "0")
                .param("directorToAdd.name", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW));
    }

    @Test
    @DisplayName("Delete director - success path")
    void deleteDirectorSuccess() throws Exception {

        this.mockMvc.perform(get(REMOVE_DIRECTOR_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + ADD_OR_REMOVE_DIRECTORS_PATH));

        verify(directorService).deleteDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, DIRECTOR_ID);
    }

    @Test
    @DisplayName("Delete director - service exception")
    void deleteDirectorServiceException() throws Exception {

        doThrow(ServiceException.class).when(directorService).deleteDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, DIRECTOR_ID);

        this.mockMvc.perform(get(REMOVE_DIRECTOR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add or remove directors view - remove secretary - success path")
    void postRequestRemoveSecretarySuccess() throws Exception {

        when(directorService.submitAddOrRemoveDirectors(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveDirectors.class))).thenReturn(new ArrayList<>());

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH + "?submit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(secretaryService).deleteSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Post add or remove directors view - add secretary - success path")
    void postRequestAddSecretarySuccess() throws Exception {

        when(directorService.submitAddOrRemoveDirectors(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveDirectors.class))).thenReturn(new ArrayList<>());

        when(secretaryService.submitSecretary(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                AddOrRemoveDirectors.class))).thenReturn(new ArrayList<>());

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH + "?submit")
                .param("secretary", "name"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post add or remove directors view - add secretary - validation errors")
    void postRequestAddSecretaryValidationErrors() throws Exception {

        when(directorService.submitAddOrRemoveDirectors(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveDirectors.class))).thenReturn(new ArrayList<>());

        List<ValidationError> secretaryValidationErrors = new ArrayList<>();
        secretaryValidationErrors.add(new ValidationError());
        when(secretaryService.submitSecretary(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                AddOrRemoveDirectors.class))).thenReturn(secretaryValidationErrors);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH + "?submit")
                .param("secretary", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_DIRECTORS_VIEW));
    }

    @Test
    @DisplayName("Post add or remove directors view - add secretary - service exception")
    void postRequestAddSecretaryServiceException() throws Exception {

        when(directorService.submitAddOrRemoveDirectors(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveDirectors.class))).thenReturn(new ArrayList<>());

        when(secretaryService.submitSecretary(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                AddOrRemoveDirectors.class))).thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADD_OR_REMOVE_DIRECTORS_PATH + "?submit")
                .param("secretary", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
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

    private HashMap<String, String> createLoans() {
        HashMap<String, String> loans = new HashMap<>();
        loans.put("id", "value");

        return loans;
    }
}
