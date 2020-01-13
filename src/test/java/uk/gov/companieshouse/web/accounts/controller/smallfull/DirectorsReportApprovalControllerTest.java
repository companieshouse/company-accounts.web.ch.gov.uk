package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportApprovalService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectorsReportApprovalControllerTest {

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
    private DirectorsReportApproval directorsReportApproval;

    @Mock
    private List<ValidationError> validationErrors;

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

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get directors report approval view - success path")
    void getRequestSuccess() throws Exception {

        when(directorsReportApprovalService.getDirectorsReportApproval(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(directorsReportApproval);

        when(directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(new Director[0]);

        when(secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(SECRETARY_NAME);

        this.mockMvc.perform(get(DIRECTORS_REPORT_APPROVAL_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(DIRECTORS_REPORT_APPROVAL_VIEW))
                .andExpect(model().attributeExists(DIRECTORS_REPORT_APPROVAL_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(directorsReportApproval).setApproverOptions(anyList());
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
}