package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
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
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.state.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.smallfull.RadioAndTextValidator;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OtherAccountingPolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private NoteService<uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies> noteService;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private AccountingPolicies accountingPoliciesDataState;

    @Mock
    private RadioAndTextValidator radioAndTextValidator;

    @Mock
    private uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies accountingPolicies;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private OtherAccountingPolicyController controller;

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";
    private static final String OTHER_ACCOUNTING_POLICY_PATH =
        SMALL_FULL_PATH + "/other-accounting-policies";
    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String OTHER_ACCOUNTING_POLICY_MODEL_ATTR = "otherAccountingPolicy";
    private static final String OTHER_ACCOUNTING_POLICY_VIEW = "smallfull/otherAccountingPolicy";
    private static final String ERROR_VIEW = "error";
    private static final String MODEL_ELEMENT = "hasOtherAccountingPolicySelected";
    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";
    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get other accounting policy view - success path")
    void getRequestSuccess() throws Exception {

        OtherAccountingPolicy otherAccountingPolicy = new OtherAccountingPolicy();
        otherAccountingPolicy.setHasOtherAccountingPolicySelected(true);

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
            .thenReturn(accountingPolicies);

        when(accountingPolicies.getOtherAccountingPolicy()).thenReturn(otherAccountingPolicy);

        when(navigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(OTHER_ACCOUNTING_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(OTHER_ACCOUNTING_POLICY_VIEW))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(OTHER_ACCOUNTING_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get other accounting policy view using state to determine whether policy is provided - success path")
    void getRequestSuccessUsingState() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(accountingPolicies.getOtherAccountingPolicy()).thenReturn(new OtherAccountingPolicy());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPoliciesDataState);
        when(accountingPoliciesDataState.getHasProvidedOtherAccountingPolicy()).thenReturn(false);

        when(navigatorService.getPreviousControllerPath(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(OTHER_ACCOUNTING_POLICY_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(OTHER_ACCOUNTING_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(OTHER_ACCOUNTING_POLICY_MODEL_ATTR));

        verify(companyAccountsDataState, times(1)).getAccountingPolicies();
        verify(accountingPoliciesDataState, times(1)).getHasProvidedOtherAccountingPolicy();
    }

    @Test
    @DisplayName("Get other accounting policy view - other accounting policy service exception")
    void getRequestOtherAccountingPolicyServiceException() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
            .thenThrow(ServiceException.class);
        this.mockMvc.perform(get(OTHER_ACCOUNTING_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit other accounting policy - success path")
    void postRequestSuccess() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPoliciesDataState);
        when(navigatorService.getNextControllerRedirect(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(postRequestWithValidData().session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(accountingPoliciesDataState, times(1)).setHasProvidedOtherAccountingPolicy(anyBoolean());
    }

    @Test
    @DisplayName("Submit other accounting policy - binding result errors")
    void postRequestBindingResultErrors() throws Exception {
        this.mockMvc.perform(postRequestWithInvalidData())
            .andExpect(status().isOk())
            .andExpect(view().name(OTHER_ACCOUNTING_POLICY_VIEW));
    }

    @Test
    @DisplayName("Submit other accounting policy - validation errors")
    void postRequestWithValidationErrors() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);
        this.mockMvc.perform(postRequestWithValidData())
            .andExpect(status().isOk())
            .andExpect(view().name(OTHER_ACCOUNTING_POLICY_VIEW));
    }

    @Test
    @DisplayName("Submit other accounting policy - other accounting policy service exception")
    void postRequestOtherAccountingPolicyServiceException() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenThrow(ServiceException.class);
        this.mockMvc.perform(postRequestWithValidData())
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    private MockHttpServletRequestBuilder postRequestWithValidData() {
        // Mock boolean field input
        String validData = "1";
        return post(OTHER_ACCOUNTING_POLICY_PATH).param(MODEL_ELEMENT, validData);
    }

    private MockHttpServletRequestBuilder postRequestWithInvalidData() {
        // Mock lack of boolean field input
        String invalidData = null;
        return post(OTHER_ACCOUNTING_POLICY_PATH).param(MODEL_ELEMENT, invalidData);
    }
}