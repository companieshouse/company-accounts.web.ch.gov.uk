package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.enumeration.AccountingRegulatoryStandard;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class BasisOfPreparationControllerTests {

    private MockMvc mockMvc;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private NoteService<AccountingPolicies> noteService;

    @Mock
    private AccountingPolicies accountingPolicies;

    @Mock
    private NavigatorService navigatorService;

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

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String BASIS_OF_PREPARATION_MODEL_ATTR = "basisOfPreparation";

    private static final String BASIS_OF_PREPARATION_VIEW = "smallfull/basisOfPreparation";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {
        when(navigatorService.getPreviousControllerPath(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get basis of preparation view - success path")
    void getRequestSuccess() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        basisOfPreparation.setAccountingRegulatoryStandard(AccountingRegulatoryStandard.FRS101);

        when(accountingPolicies.getBasisOfPreparation()).thenReturn(basisOfPreparation);

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

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(BASIS_OF_PREPARATION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Submit basis of preparation - success path")
    void postRequestSuccess() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);
        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(postRequestWithValidData())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
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

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(validationErrors);
        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(postRequestWithValidData())
                .andExpect(status().isOk())
                .andExpect(view().name(BASIS_OF_PREPARATION_VIEW));
    }

    @Test
    @DisplayName("Submit basis of preparation - basis of preparation service exception")
    void postRequestBasisOfPreparationServiceException() throws Exception {

        when(noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenReturn(accountingPolicies);

        when(noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies, NoteType.SMALL_FULL_ACCOUNTING_POLICIES))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(postRequestWithValidData())
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    private MockHttpServletRequestBuilder postRequestWithValidData() {

        String beanElement = "accountingRegulatoryStandard";
        // Mock boolean field input
        String validData = "FRS102";

        return post(BASIS_OF_PREPARATION_PATH).param(beanElement, validData);
    }

    private MockHttpServletRequestBuilder postRequestWithInvalidData() {

        String beanElement = "isPreparedInAccordanceWithStandards";
        // Mock lack of boolean field input
        String invalidData = null;

        return post(BASIS_OF_PREPARATION_PATH).param(beanElement, invalidData);
    }
}
