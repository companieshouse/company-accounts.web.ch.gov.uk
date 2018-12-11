package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.ValuationInformationPolicyService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ValuationInformationPolicyControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
                                                    "/transaction/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full";

    private static final String VALUATION_INFORMATION_POLICY_PATH = SMALL_FULL_PATH + "/valuation-information";
    private static final String REVIEW_PATH = SMALL_FULL_PATH + "/review";

    private static final String VALUATION_INFORMATION_POLICY_VIEW = "smallfull/valuationInformationPolicy";
    private static final String ERROR_VIEW = "error";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";
    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";
    private static final String VALUATION_INFORMATION_POLICY_MODEL_ATTR = "valuationInformationPolicy";

    private MockMvc mockMvc;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private ValuationInformationPolicyService valuationInformationPolicyService;

    @InjectMocks
    private ValuationInformationPolicyController controller;

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get valuation information policy view - success path")
    void getRequestSuccess() throws Exception {

        when(valuationInformationPolicyService.getValuationInformationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(new ValuationInformationPolicy());

        this.mockMvc
            .perform(get(VALUATION_INFORMATION_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(VALUATION_INFORMATION_POLICY_VIEW))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
            .andExpect(model().attributeExists(VALUATION_INFORMATION_POLICY_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get valuation information policy view - valuation information policy service exception")
    void getRequestValuationInformationPolicyServiceException() throws Exception {

        when(valuationInformationPolicyService.getValuationInformationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenThrow(ServiceException.class);

        this.mockMvc
            .perform(get(VALUATION_INFORMATION_POLICY_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit valuation information policy - success path")
    void postRequestSuccess() throws Exception {

        when(valuationInformationPolicyService
            .submitValuationInformationPolicy(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                any(ValuationInformationPolicy.class)))
                    .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        this.mockMvc.perform(createPostRequestWithParam(true))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + REVIEW_PATH));
    }

    @Test
    @DisplayName("Submit valuation information policy - binding result errors")
    void postRequestBindingResultErrors() throws Exception {
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

        when(valuationInformationPolicyService
            .submitValuationInformationPolicy(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                any(ValuationInformationPolicy.class)))
                    .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

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

        when(valuationInformationPolicyService
            .submitValuationInformationPolicy(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID),
                any(ValuationInformationPolicy.class)))
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
