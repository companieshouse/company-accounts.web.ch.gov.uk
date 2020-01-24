package uk.gov.companieshouse.web.accounts.controller.smallfull;

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

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.model.state.DirectorsReportStatements;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CompanyPolicyOnDisabledEmployeesService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyPolicyOnDisabledEmployeesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @Mock
    private CompanyPolicyOnDisabledEmployeesService companyPolicyOnDisabledEmployeesService;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private CompanyPolicyOnDisabledEmployeesController controller;

    @Mock
    private CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees;

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

    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES_PATH = "/company/" + COMPANY_NUMBER +
                                                            "/transaction/" + TRANSACTION_ID +
                                                            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                            "/small-full/directors-report/company-policy-on-disabled-employees";

    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES_MODEL_ATTR = "companyPolicyOnDisabledEmployees";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES_VIEW = "smallfull/companyPolicyOnDisabledEmployees";

    private static final String ERROR_VIEW = "error";

    private static final String COMPANY_POLICY_ON_DISABLED_EMPLOYEES_DETAILS = "companyPolicyOnDisabledEmployeesDetails";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get request - success")
    void getCompanyPolicyOnDisabledEmployeesSuccess() throws Exception {

        when(companyPolicyOnDisabledEmployeesService.getCompanyPolicyOnDisabledEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(companyPolicyOnDisabledEmployees);

        this.mockMvc.perform(get(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_VIEW))
                .andExpect(model().attributeExists(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get request - service exception")
    void getCompanyPolicyOnDisabledEmployeesThrowsServiceException() throws Exception {

        when(companyPolicyOnDisabledEmployeesService.getCompanyPolicyOnDisabledEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - success")
    void postCompanyPolicyOnDisabledEmployeesSuccess() throws Exception {

        when(companyPolicyOnDisabledEmployeesService
                .submitCompanyPolicyOnDisabledEmployees(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(CompanyPolicyOnDisabledEmployees.class)))
                        .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_PATH)
                .param(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_DETAILS, COMPANY_POLICY_ON_DISABLED_EMPLOYEES_DETAILS))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post request - validation errors")
    void postCompanyPolicyOnDisabledEmployeesWithValidationErrors() throws Exception {

        when(companyPolicyOnDisabledEmployeesService
                .submitCompanyPolicyOnDisabledEmployees(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(CompanyPolicyOnDisabledEmployees.class)))
                        .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_PATH)
                .param(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_DETAILS, COMPANY_POLICY_ON_DISABLED_EMPLOYEES_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_VIEW));

        verify(navigatorService, never()).getNextControllerRedirect(any(), ArgumentMatchers.<String>any());
    }

    @Test
    @DisplayName("Post request - service exception")
    void postCompanyPolicyOnDisabledEmployeesThrowsServiceException() throws Exception {

        when(companyPolicyOnDisabledEmployeesService
                .submitCompanyPolicyOnDisabledEmployees(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(CompanyPolicyOnDisabledEmployees.class)))
                        .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_PATH)
                .param(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_DETAILS, COMPANY_POLICY_ON_DISABLED_EMPLOYEES_DETAILS))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post request - binding result errors")
    void postCompanyPolicyOnDisabledEmployeesBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(COMPANY_POLICY_ON_DISABLED_EMPLOYEES_VIEW));

        verify(companyPolicyOnDisabledEmployeesService, never())
                .submitCompanyPolicyOnDisabledEmployees(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(CompanyPolicyOnDisabledEmployees.class));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(directorsReportStatements);
        when(directorsReportStatements.getHasProvidedCompanyPolicyOnDisabledEmployees()).thenReturn(false);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE)).thenReturn(companyAccountsDataState);
        when(companyAccountsDataState.getDirectorsReportStatements()).thenReturn(directorsReportStatements);
        when(directorsReportStatements.getHasProvidedCompanyPolicyOnDisabledEmployees()).thenReturn(true);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
