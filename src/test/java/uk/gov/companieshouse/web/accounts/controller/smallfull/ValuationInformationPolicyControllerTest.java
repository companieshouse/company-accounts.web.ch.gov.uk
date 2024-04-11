package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
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
import static org.mockito.Mockito.doReturn;
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
class ValuationInformationPolicyControllerTest {
    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
                                                    "/transaction/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full";

    private static final String VALUATION_INFORMATION_POLICY_PATH = SMALL_FULL_PATH + "/valuation-information";
    private static final String VALUATION_INFORMATION_POLICY_VIEW = "smallfull/valuationInformationPolicy";
    private static final String ERROR_VIEW = "error";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";
    private static final String VALUATION_INFORMATION_POLICY_MODEL_ATTR = "valuationInformationPolicy";

    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String VALUATION_INFORMATION_POLICY_FIELD_PATH =
            "valuationInformationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.valuation_information_and_policy";

    @Captor
    private ArgumentCaptor<String[]> captor = ArgumentCaptor.forClass(String[].class);

    private MockMvc mockMvc;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private NoteService<uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies> noteService;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private AccountingPolicies accountingPoliciesState;

    @Mock
    private uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies accountingPolicies;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private RadioAndTextValidator radioAndTextValidator;

    @InjectMocks
    private ValuationInformationPolicyController controller;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get valuation information policy view - success path")
    void getRequestSuccess() throws Exception {
        ValuationInformationPolicy valuationInformationPolicy = new ValuationInformationPolicy();
        valuationInformationPolicy.setIncludeValuationInformationPolicy(true);

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(accountingPolicies.getValuationInformationPolicy()).thenReturn(valuationInformationPolicy);

        when(navigatorService.getPreviousControllerPath(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc
            .perform(get(VALUATION_INFORMATION_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(VALUATION_INFORMATION_POLICY_VIEW))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(VALUATION_INFORMATION_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get valuation information policy view using state to determine whether policy is provided - success path")
    void getRequestSuccessUsingState() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(accountingPolicies.getValuationInformationPolicy()).thenReturn(new ValuationInformationPolicy());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPoliciesState);
        when(accountingPoliciesState.getHasProvidedValuationInformationPolicy()).thenReturn(false);

        when(navigatorService.getPreviousControllerPath(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc
                .perform(get(VALUATION_INFORMATION_POLICY_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(VALUATION_INFORMATION_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(VALUATION_INFORMATION_POLICY_MODEL_ATTR));

        verify(companyAccountsDataState, times(1)).getAccountingPolicies();
        verify(accountingPoliciesState, times(1)).getHasProvidedValuationInformationPolicy();
    }

    @Test
    @DisplayName("Get valuation information policy view - valuation information policy service exception")
    void getRequestValuationInformationPolicyServiceException() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
            .thenThrow(ServiceException.class);

        this.mockMvc
            .perform(get(VALUATION_INFORMATION_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit valuation information policy - success path")
    void postRequestSuccess() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        doReturn(validationErrors).when(noteService).submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies,
                NoteType.SMALL_FULL_ACCOUNTING_POLICIES);

        when(validationErrors.isEmpty()).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPoliciesState);

        when(navigatorService.getNextControllerRedirect(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(createPostRequestWithParam(true).session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));

        verify(accountingPolicies).setValuationInformationPolicy(any(ValuationInformationPolicy.class));
        verify(accountingPoliciesState, times(1)).setHasProvidedValuationInformationPolicy(anyBoolean());

        verify(radioAndTextValidator).validate(eq(true), eq(null), any(BindingResult.class), eq(INVALID_STRING_SIZE_ERROR_MESSAGE), eq(VALUATION_INFORMATION_POLICY_FIELD_PATH));
    }

    @Test
    @DisplayName("Submit valuation information policy - binding result errors")
    void postRequestBindingResultErrors() throws Exception {
        when(navigatorService.getPreviousControllerPath(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc
            .perform(createPostRequestWithParam(false))
            .andExpect(status().isOk())
            .andExpect(view().name(VALUATION_INFORMATION_POLICY_VIEW))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(VALUATION_INFORMATION_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Submit valuation information policy - validation errors")
    void postRequestWithValidationErrors() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        when(navigatorService.getPreviousControllerPath(any(), captor.capture())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc
            .perform(createPostRequestWithParam(true))
            .andExpect(status().isOk())
            .andExpect(view().name(VALUATION_INFORMATION_POLICY_VIEW))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(VALUATION_INFORMATION_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Submit valuation information policy - valuation information policy service exception")
    void postRequestValuationInformationPolicyServiceException() throws Exception {
        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenThrow(ServiceException.class);

        this.mockMvc
            .perform(createPostRequestWithParam(true))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    /**
     * Create a post request with or without valid data
     *
     * @param validData Determines where to add valid data to the request
     * @return a {@link MockHttpServletRequestBuilder}
     */
    private MockHttpServletRequestBuilder createPostRequestWithParam(boolean validData) {
        String beanElement = "includeValuationInformationPolicy";
        String data = validData ? "1" : null;

        return post(VALUATION_INFORMATION_POLICY_PATH).param(beanElement, data);
    }
}
