package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.*;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.state.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.smallfull.RadioAndTextValidator;

import java.util.List;

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

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class TangibleDepreciationPolicyControllerTest {

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
    NavigatorService navigatorService;

    @InjectMocks
    private TangibleDepreciationPolicyController controller;
    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";
    private static final String TANGIBLE_DEPRECIATION_POLICY_PATH =
        SMALL_FULL_PATH + "/tangible-depreciation-policy";
    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String TANGIBLE_DEPRECIATION_POLICY_MODEL_ATTR = "tangibleDepreciationPolicy";
    private static final String TANGIBLE_DEPRECIATION_POLICY_VIEW = "smallfull/tangibleDepreciationPolicy";
    private static final String ERROR_VIEW = "error";
    private static final String MODEL_ELEMENT = "hasTangibleDepreciationPolicySelected";
    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";
    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String TANGIBLE_DEPRECIATION_POLICY_FIELD_PATH =
            "tangibleDepreciationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.tangible_fixed_assets_depreciation_policy";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get tangible depreciation policy view - success path")
    void getRequestSuccess() throws Exception {

        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(accountingPolicies.getTangibleDepreciationPolicy()).thenReturn(tangibleDepreciationPolicy);

        when(navigatorService.getPreviousControllerPath(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(TANGIBLE_DEPRECIATION_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(TANGIBLE_DEPRECIATION_POLICY_VIEW))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(TANGIBLE_DEPRECIATION_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get tangible depreciation policy view using state to determine whether policy is included - success path")
    void getRequestSuccessUsingState() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(accountingPolicies.getTangibleDepreciationPolicy()).thenReturn(new TangibleDepreciationPolicy());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPoliciesDataState);
        when(accountingPoliciesDataState.getHasProvidedTangiblePolicy()).thenReturn(false);

        when(navigatorService.getPreviousControllerPath(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(get(TANGIBLE_DEPRECIATION_POLICY_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(TANGIBLE_DEPRECIATION_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TANGIBLE_DEPRECIATION_POLICY_MODEL_ATTR));

        verify(companyAccountsDataState, times(1)).getAccountingPolicies();
        verify(accountingPoliciesDataState, times(1)).getHasProvidedTangiblePolicy();
    }

    @Test
    @DisplayName("Get tangible depreciation policy view - tangible depreciation policy service exception")
    void getRequestTangibleDepreciationPolicyServiceException() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenThrow(ServiceException.class);
        this.mockMvc.perform(get(TANGIBLE_DEPRECIATION_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit tangible depreciation policy - success path")
    void postRequestSuccess() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPoliciesDataState);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(postRequestWithValidData().session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(accountingPoliciesDataState, times(1)).setHasProvidedTangiblePolicy(anyBoolean());

        verify(radioAndTextValidator).validate(eq(true), eq(null), any(BindingResult.class), eq(INVALID_STRING_SIZE_ERROR_MESSAGE), eq(TANGIBLE_DEPRECIATION_POLICY_FIELD_PATH));
    }

    @Test
    @DisplayName("Submit tangible depreciation policy - binding result errors")
    void postRequestBindingResultErrors() throws Exception {
        this.mockMvc.perform(postRequestWithInvalidData())
            .andExpect(status().isOk())
            .andExpect(view().name(TANGIBLE_DEPRECIATION_POLICY_VIEW));
    }

    @Test
    @DisplayName("Submit tangible depreciation policy - validation errors")
    void postRequestWithValidationErrors() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);
        this.mockMvc.perform(postRequestWithValidData())
            .andExpect(status().isOk())
            .andExpect(view().name(TANGIBLE_DEPRECIATION_POLICY_VIEW));
    }

    @Test
    @DisplayName("Submit tangible depreciation policy - tangible depreciation policy service exception")
    void postRequestTangibleDepreciationPolicyServiceException() throws Exception {
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
        return post(TANGIBLE_DEPRECIATION_POLICY_PATH).param(MODEL_ELEMENT, validData);
    }

    private MockHttpServletRequestBuilder postRequestWithInvalidData() {
        // Mock lack of boolean field input
        String invalidData = null;
        return post(TANGIBLE_DEPRECIATION_POLICY_PATH).param(MODEL_ELEMENT, invalidData);
    }
}