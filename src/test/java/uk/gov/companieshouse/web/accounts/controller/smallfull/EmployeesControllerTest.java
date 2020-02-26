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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeesService mockEmployeesService;

    @Mock
    private NavigatorService mockNavigatorService;

    @Mock
    private MockHttpServletRequest mockHttpServletRequest;

    @Mock
    private MockHttpSession mockHttpSession;

    @Mock
    private Model mockModel;

    @Mock
    private Employees mockEmployees;

    @InjectMocks
    private EmployeesController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String EMPLOYEES_PATH = SMALL_FULL_PATH + "/employees";

    private static final String EMPLOYEES_MODEL_ATTR = "employees";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String EMPLOYEES_VIEW = "smallfull/employees";

    private static final String ERROR_VIEW = "error";

    private static final String TEST_PATH = "averageNumberOfEmployees.currentAverageNumberOfEmployees";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get employees view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockEmployeesService.getEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(new Employees());

        this.mockMvc.perform(get(EMPLOYEES_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(EMPLOYEES_VIEW))
            .andExpect(model().attributeExists(EMPLOYEES_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockEmployeesService, times(1)).getEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Get employees view failure path due to error on employees retrieval")
    void getRequestFailure() throws Exception {

        when(mockEmployeesService.getEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(EMPLOYEES_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post employees success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(EMPLOYEES_PATH)
            .param("details", "test"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post employees failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(mockEmployeesService).submitEmployees(anyString(), anyString(), any(Employees.class), anyString());

        this.mockMvc.perform(post(EMPLOYEES_PATH)
            .param("details", "test"))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post employees with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = TEST_PATH;
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "test";

        this.mockMvc.perform(post(EMPLOYEES_PATH)
            .param(beanElement, invalidData))
            .andExpect(status().isOk())
            .andExpect(view().name(EMPLOYEES_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post employees failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(TEST_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockEmployeesService.submitEmployees(anyString(), anyString(), any(Employees.class), anyString())).thenReturn(errors);

        this.mockMvc.perform(post(EMPLOYEES_PATH)
            .param("details", "test"))
            .andExpect(status().isOk())
            .andExpect(view().name(EMPLOYEES_VIEW));
    }

    @Test
    @DisplayName("Test will render with selected employees note")
    void willRenderWithSelectedEmployeesNote() throws Exception {

        CompanyAccountsDataState companyAccountsDataState = new CompanyAccountsDataState();
        companyAccountsDataState.setHasSelectedEmployeesNote(true);

        when(mockHttpServletRequest.getSession()).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE))
        .thenReturn(companyAccountsDataState);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test will not render with unselected employees note")
    void willNotRenderWithUnselectedEmployeesNote() throws Exception {
        CompanyAccountsDataState companyAccountsDataState = new CompanyAccountsDataState();
        companyAccountsDataState.setHasSelectedEmployeesNote(false);

        when(mockHttpServletRequest.getSession()).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE))
        .thenReturn(companyAccountsDataState);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test will not render without employees note")
    void willNotRenderWithoutEmployeesNote() throws Exception {
        CompanyAccountsDataState companyAccountsDataState = new CompanyAccountsDataState();
        companyAccountsDataState.setHasSelectedEmployeesNote(null);

        when(mockHttpServletRequest.getSession()).thenReturn(mockHttpSession);
        when(mockHttpSession.getAttribute(COMPANY_ACCOUNTS_DATA_STATE))
            .thenReturn(companyAccountsDataState);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
