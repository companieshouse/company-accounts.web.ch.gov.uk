package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.state.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.smallfull.RadioAndTextValidator;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class TurnoverPolicyControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full";

    private static final String TURNOVER_POLICY_PATH = SMALL_FULL_PATH + "/turnover-policy";
    private static final String TURNOVER_POLICY_VIEW = "smallfull/turnoverPolicy";
    private static final String ERROR_VIEW = "error";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";
    private static final String TURNOVER_POLICY_MODEL_ATTR = "turnoverPolicy";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String TURNOVER_POLICY_DETAILS_FIELD_PATH = "turnoverPolicyDetails";
    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE = "validation.length.minInvalid.accounting_policies.turnover_policy";

    private MockMvc mockMvc;

    @Mock
    private List<ValidationError> validationErrorsMock;

    @Mock
    private NoteService<uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies> noteService;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private AccountingPolicies accountingPoliciesDataState;

    @Mock
    private uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies accountingPolicies;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private RadioAndTextValidator radioAndTextValidator;

    @InjectMocks
    private TurnoverPolicyController turnoverPolicyController;

    @BeforeEach
    public void setupBeforeEach() {
        when(navigatorService.getPreviousControllerPath(
                any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc = MockMvcBuilders.standaloneSetup(turnoverPolicyController).build();
    }

    @Test
    @DisplayName("Get the Include Turnover Policy view")
    void shouldGetTurnoverPolicy() throws Exception {

        TurnoverPolicy turnoverPolicy = new TurnoverPolicy();
        turnoverPolicy.setIsIncludeTurnoverSelected(true);

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);
        when(accountingPolicies.getTurnoverPolicy()).thenReturn(turnoverPolicy);

        this.mockMvc
                .perform(get(TURNOVER_POLICY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(TURNOVER_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TURNOVER_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get the Include Turnover Policy view using the state to determine whether the policy is included")
    void shouldGetTurnoverPolicyUsingState() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);
        when(accountingPolicies.getTurnoverPolicy()).thenReturn(new TurnoverPolicy());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(
                accountingPoliciesDataState);
        when(accountingPoliciesDataState.getHasProvidedTurnoverPolicy()).thenReturn(false);

        this.mockMvc
                .perform(get(TURNOVER_POLICY_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(TURNOVER_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TURNOVER_POLICY_MODEL_ATTR));

        verify(companyAccountsDataState, times(1)).getAccountingPolicies();
        verify(accountingPoliciesDataState, times(1)).getHasProvidedTurnoverPolicy();
    }

    @Test
    @DisplayName("Get the Include Turnover Policy view fails due to service exception")
    void shouldNotGetTurnoverPolicyServiceException() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_ACCOUNTING_POLICIES)).thenThrow(ServiceException.class);
        this.mockMvc
                .perform(get(TURNOVER_POLICY_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post Turnover Policy call is successful")
    void shouldPostTurnoverPolicy() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        doReturn(validationErrorsMock).when(noteService)
                .submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies,
                        NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

        when(validationErrorsMock.isEmpty()).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(
                accountingPoliciesDataState);

        when(navigatorService.getNextControllerRedirect(any(Class.class),
                anyString(),
                anyString(),
                anyString()))
                .thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(createPostRequestWithParam(false).session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(
                        view().name(MOCK_CONTROLLER_PATH))
                .andExpect(flash().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(flash().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(flash().attributeExists(TURNOVER_POLICY_MODEL_ATTR));

        verify(accountingPolicies).setTurnoverPolicy(any(TurnoverPolicy.class));
        verify(accountingPoliciesDataState, times(1)).setHasProvidedTurnoverPolicy(anyBoolean());

        verify(radioAndTextValidator).validate(eq(true), eq(null), any(BindingResult.class),
                eq(INVALID_STRING_SIZE_ERROR_MESSAGE), eq(TURNOVER_POLICY_DETAILS_FIELD_PATH));
    }

    @Test
    @DisplayName("Post Turnover Policy call is not redirected as there is a binding error")
    void shouldPostTurnoverPolicyFailsBindingErrors() throws Exception {
        this.mockMvc
                .perform(createPostRequestWithParam(true))
                .andExpect(status().isOk())
                .andExpect(view().name(TURNOVER_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TURNOVER_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post Turnover Policy call is not redirected as there is a validation error")
    void shouldPostTurnoverPolicyFailsDueValidationErrors() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        doReturn(validationErrorsMock).when(noteService)
                .submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies,
                        NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

        when(validationErrorsMock.isEmpty()).thenReturn(false);

        this.mockMvc
                .perform(createPostRequestWithParam(false))
                .andExpect(status().isOk())
                .andExpect(view().name(TURNOVER_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TURNOVER_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post Turnover Policy call is not redirected as there is service exception")
    void shouldPostTurnoverPolicyFailsDueException() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        doThrow(ServiceException.class).when(noteService)
                .submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies,
                        NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

        this.mockMvc
                .perform(createPostRequestWithParam(false))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TURNOVER_POLICY_MODEL_ATTR));
    }

    /**
     * Add the information to the post request. Where the parameter will control if data to be set
     * needs to be valid or invalid
     *
     * @param addInvalidData
     * @return
     */
    private MockHttpServletRequestBuilder createPostRequestWithParam(boolean addInvalidData) {

        String beanElement = "isIncludeTurnoverSelected";
        String data = addInvalidData ? null : "1";

        return post(TURNOVER_POLICY_PATH).param(beanElement, data);
    }
}
