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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsReportApprovalControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private DirectorsReportApprovalService directorsReportApprovalService;

    @Mock
    private DirectorService directorService;

    @Mock
    private SecretaryService secretaryService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MockHttpSession session;

    @Mock
    private DirectorsReportApproval directorsReportApproval;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private Director director;

    @InjectMocks
    private DirectorsReportApprovalController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SECRETARY_NAME = "secretaryName";

    private static final String DIRECTOR_NAME = "directorName";

    private static final String DIRECTORS_REPORT_APPROVAL_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/directors-report/approval";

    private static final String DIRECTORS_REPORT_APPROVAL_MODEL_ATTR = "directorsReportApproval";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String DIRECTORS_REPORT_APPROVAL_VIEW = "smallfull/directorsReportApproval";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get directors report approval view - success path - multiple approvers")
    void getRequestSuccessMultipleApprovers() throws Exception {

        when(directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(directorsReportApproval);

        Director director = new Director();
        director.setName(DIRECTOR_NAME);
        Director[] directors = new Director[]{director};
        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, true)).thenReturn(directors);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        this.mockMvc.perform(get(DIRECTORS_REPORT_APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REPORT_APPROVAL_VIEW))
                .andExpect(model().attributeExists(DIRECTORS_REPORT_APPROVAL_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID));

        verify(directorsReportApproval).setApproverOptions(anyList());
        verify(directorsReportApproval, never()).setName(anyString());
    }

    @Test
    @DisplayName("Get directors report approval view - success path - single approver")
    void getRequestSuccessSingleApprover() throws Exception {

        when(directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(directorsReportApproval);

        Director[] directors = createReappointedDirector();

        Director newDirector = new Director();
        newDirector.setResignationDate(LocalDate.of(2017, 04, 01));
        newDirector.setAppointmentDate(LocalDate.of(2017, 03, 01));

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, true)).thenReturn(directors);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        this.mockMvc.perform(get(DIRECTORS_REPORT_APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REPORT_APPROVAL_VIEW))
                .andExpect(model().attributeExists(DIRECTORS_REPORT_APPROVAL_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(directorsReportApproval).setApproverOptions(anyList());

        assertNull(directors[1].getResignationDate());
        assertEquals(newDirector.getResignationDate(), directors[0].getResignationDate());
    }

    @Test
    @DisplayName("Get directors report approval view - service exception")
    void getRequestServiceException() throws Exception {

        when(directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(DIRECTORS_REPORT_APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post directors report approval view - success path")
    void postRequestSuccess() throws Exception {

        when(directorsReportApprovalService.submitDirectorsReportApproval(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorsReportApproval.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(DIRECTORS_REPORT_APPROVAL_PATH)
                .param("name", DIRECTOR_NAME))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post directors report approval view - validation errors")
    void postRequestValidationErrors() throws Exception {

        when(directorsReportApprovalService.submitDirectorsReportApproval(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorsReportApproval.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(DIRECTORS_REPORT_APPROVAL_PATH)
                .param("name", DIRECTOR_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REPORT_APPROVAL_VIEW));
    }

    @Test
    @DisplayName("Post directors report approval view - service exception")
    void postRequestServiceException() throws Exception {

        when(directorsReportApprovalService.submitDirectorsReportApproval(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorsReportApproval.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(DIRECTORS_REPORT_APPROVAL_PATH)
                .param("name", DIRECTOR_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post directors report approval view - binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(DIRECTORS_REPORT_APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REPORT_APPROVAL_VIEW));

        verify(directorsReportApprovalService, never())
                .submitDirectorsReportApproval(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(DirectorsReportApproval.class));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedDirectorsReport()).thenReturn(false);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getHasIncludedDirectorsReport()).thenReturn(true);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    private Director[] createReappointedDirector() {

        Director[] directors = new Director[3];

        Director director = new Director();

        director.setResignationDate(LocalDate.of(2017, 04, 01));
        director.setAppointmentDate(LocalDate.of(2017, 03, 01));

        directors[0] = director;

        director = new Director();

        director.setAppointmentDate(LocalDate.of(2017, 01, 01));

        directors[1] = director;

        director = new Director();

        director.setResignationDate(LocalDate.of(2017, 03, 01));
        director.setAppointmentDate(LocalDate.of(2017, 04, 01));

        directors[2] = director;

        return directors;
    }
}
