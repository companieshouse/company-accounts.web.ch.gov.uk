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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.state.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.TangibleDepreciationPolicyServiceImpl;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TangibleDepreciationPolicyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private TangibleDepreciationPolicyServiceImpl tangibleDepreciationPolicyService;

    @Mock
    private CompanyAccountsDataState companyAccountsDataState;

    @Mock
    private AccountingPolicies accountingPolicies;

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
    private static final String INTANGIBLE_AMORTISATION_POLICY_PATH =
        SMALL_FULL_PATH + "/intangible-fixed-assets-amortisation";
    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";
    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String TANGIBLE_DEPRECIATION_POLICY_MODEL_ATTR = "tangibleDepreciationPolicy";
    private static final String TANGIBLE_DEPRECIATION_POLICY_VIEW = "smallfull/tangibleDepreciationPolicy";
    private static final String ERROR_VIEW = "error";
    private static final String MODEL_ELEMENT = "hasTangibleDepreciationPolicySelected";
    private static final String COMPANY_ACCOUNTS_STATE = "companyAccountsDataState";


    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get tangible depreciation policy view - success path")
    void getRequestSuccess() throws Exception {

        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
        when(tangibleDepreciationPolicyService
            .getTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(tangibleDepreciationPolicy);

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

        when(tangibleDepreciationPolicyService
                .getTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(new TangibleDepreciationPolicy());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPolicies);
        when(accountingPolicies.getHasProvidedTangiblePolicy()).thenReturn(false);

        this.mockMvc.perform(get(TANGIBLE_DEPRECIATION_POLICY_PATH).session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(TANGIBLE_DEPRECIATION_POLICY_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(TANGIBLE_DEPRECIATION_POLICY_MODEL_ATTR));

        verify(companyAccountsDataState, times(1)).getAccountingPolicies();
        verify(accountingPolicies, times(1)).getHasProvidedTangiblePolicy();
    }

    @Test
    @DisplayName("Get tangible depreciation policy view - tangible depreciation policy service exception")
    void getRequestTangibleDepreciationPolicyServiceException() throws Exception {
        when(tangibleDepreciationPolicyService
            .getTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenThrow(ServiceException.class);
        this.mockMvc.perform(get(TANGIBLE_DEPRECIATION_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit tangible depreciation policy - success path")
    void postRequestSuccess() throws Exception {

        when(tangibleDepreciationPolicyService
            .submitTangibleDepreciationPolicy(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                any(TangibleDepreciationPolicy.class)))
            .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(COMPANY_ACCOUNTS_STATE, companyAccountsDataState);

        when(companyAccountsDataState.getAccountingPolicies()).thenReturn(accountingPolicies);

        this.mockMvc.perform(postRequestWithValidData().session(session))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(
                UrlBasedViewResolver.REDIRECT_URL_PREFIX + INTANGIBLE_AMORTISATION_POLICY_PATH));

        verify(accountingPolicies, times(1)).setHasProvidedTangiblePolicy(anyBoolean());
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
        when(tangibleDepreciationPolicyService
            .submitTangibleDepreciationPolicy(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                any(TangibleDepreciationPolicy.class)))
            .thenReturn(validationErrors);
        when(validationErrors.isEmpty()).thenReturn(false);
        this.mockMvc.perform(postRequestWithValidData())
            .andExpect(status().isOk())
            .andExpect(view().name(TANGIBLE_DEPRECIATION_POLICY_VIEW));
    }

    @Test
    @DisplayName("Submit tangible depreciation policy - tangible depreciation policy service exception")
    void postRequestTangibleDepreciationPolicyServiceException() throws Exception {
        when(tangibleDepreciationPolicyService
            .submitTangibleDepreciationPolicy(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                any(TangibleDepreciationPolicy.class)))
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