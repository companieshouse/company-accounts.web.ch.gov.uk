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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasisOfPreparationControllerTests {

    private MockMvc mockMvc;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private BasisOfPreparationService basisOfPreparationService;

    @InjectMocks
    private BasisOfPreparationController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
                                                    "/transaction/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full";

    private static final String BASIS_OF_PREPARATION_PATH = SMALL_FULL_PATH + "/basis-of-preparation";

    private static final String TURNOVER_POLICY_PATH = SMALL_FULL_PATH + "/turnover-policy";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String BASIS_OF_PREPARATION_MODEL_ATTR = "basisOfPreparation";

    private static final String BASIS_OF_PREPARATION_VIEW = "smallfull/basisOfPreparation";

    private static final String ERROR_VIEW = "error";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get basis of preparation view - success path")
    void getRequestSuccess() throws Exception {

        when(basisOfPreparationService.getBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(new BasisOfPreparation());

        this.mockMvc.perform(get(BASIS_OF_PREPARATION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(BASIS_OF_PREPARATION_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(BASIS_OF_PREPARATION_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get basis of preparation view - basis of preparation service exception")
    void getRequestBasisOfPreparationServiceException() throws Exception {

        when(basisOfPreparationService.getBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(BASIS_OF_PREPARATION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit basis of preparation - success path")
    void postRequestSuccess() throws Exception {

        when(basisOfPreparationService.submitBasisOfPreparation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(BasisOfPreparation.class)))
                .thenReturn(validationErrors);
        when(validationErrors.isEmpty()).thenReturn(true);

        this.mockMvc.perform(postRequestWithValidData())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + TURNOVER_POLICY_PATH));
    }

    @Test
    @DisplayName("Submit basis of preparation - binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(postRequestWithInvalidData())
                .andExpect(status().isOk())
                .andExpect(view().name(BASIS_OF_PREPARATION_VIEW));
    }

    @Test
    @DisplayName("Submit basis of preparation - validation errors")
    void postRequestWithValidationErrors() throws Exception {

        when(basisOfPreparationService.submitBasisOfPreparation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(BasisOfPreparation.class)))
                .thenReturn(validationErrors);
        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(postRequestWithValidData())
                .andExpect(status().isOk())
                .andExpect(view().name(BASIS_OF_PREPARATION_VIEW));
    }

    @Test
    @DisplayName("Submit basis of preparation - basis of preparation service exception")
    void postRequestBasisOfPreparationServiceException() throws Exception {

        when(basisOfPreparationService.submitBasisOfPreparation(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(BasisOfPreparation.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(postRequestWithValidData())
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    private MockHttpServletRequestBuilder postRequestWithValidData() {

        String beanElement = "isPreparedInAccordanceWithStandards";
        // Mock boolean field input
        String validData = "1";

        return post(BASIS_OF_PREPARATION_PATH).param(beanElement, validData);
    }

    private MockHttpServletRequestBuilder postRequestWithInvalidData() {

        String beanElement = "isPreparedInAccordanceWithStandards";
        // Mock lack of boolean field input
        String invalidData = null;

        return post(BASIS_OF_PREPARATION_PATH).param(beanElement, invalidData);
    }


}
