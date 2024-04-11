package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
class FixedAssetInvestmentsControllerTests {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService mockNavigatorService;
    
    @Mock
    private NoteService<FixedAssetsInvestments> mockFixedAssetsInvestmentsService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @InjectMocks
    private FixedAssetsInvestmentsController controller;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/fixed-assets-investments";

    private static final String FIXED_ASSETS_INVESTMENTS_VIEW = "smallfull/fixedAssetsInvestments";

    private static final String FIXED_ASSETS_INVESTMENTS_MODEL_ATTR = "fixedAssetsInvestments";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String ERROR_VIEW = "error";

    private static final String TEST_PATH = "fixedAssetsDetails";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get fixedAssetsInvestments view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockFixedAssetsInvestmentsService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT))
            .thenReturn(new FixedAssetsInvestments());

        this.mockMvc.perform(get(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(FIXED_ASSETS_INVESTMENTS_VIEW))
            .andExpect(model().attributeExists(FIXED_ASSETS_INVESTMENTS_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockFixedAssetsInvestmentsService, times(1))
            .get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT);
    }

    @Test
    @DisplayName("Get fixedAssetsInvestments view failure path due to error on fixedAssetsInvestments retrieval")
    void getRequestFailureInGetFixedAssetsInvestments() throws Exception {

        when(mockFixedAssetsInvestmentsService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT))
            .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post fixedAssetsInvestments success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);
        when(mockFixedAssetsInvestmentsService.submit(anyString(), anyString(), any(FixedAssetsInvestments.class), eq(NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT)))
            .thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, "Test"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post fixedAssetsInvestments failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(mockFixedAssetsInvestmentsService).submit(anyString(), anyString(), any(FixedAssetsInvestments.class), eq(NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT));

        this.mockMvc.perform(post(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, "Test"))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
    
    @Test
    @DisplayName("Post fixedAssetsInvestments failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(TEST_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockFixedAssetsInvestmentsService.submit(anyString(), anyString(), any(FixedAssetsInvestments.class), eq(NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT)))
            .thenReturn(errors);

        this.mockMvc.perform(post(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, "#¢¢#¢"))
            .andExpect(status().isOk())
            .andExpect(view().name(FIXED_ASSETS_INVESTMENTS_VIEW));
    }
    
    @Test
    @DisplayName("Post fixedAssetsInvestments with binding result error")
    void postRequestBindingResultErrors() throws Exception {
        
        this.mockMvc.perform(post(SMALL_FULL_FIXED_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, ""))
            .andExpect(status().isOk())
            .andExpect(view().name(FIXED_ASSETS_INVESTMENTS_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Test will render with Investments present on balancesheet")
    void willRenderInvestmentsPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheet());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertTrue(renderPage);
    }

    @Test
    @DisplayName("Test will render with Investments not present on balancesheet")
    void willRenderInvestmentsNotPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetNoFixedInvestments());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }

    @Test
    @DisplayName("Test will not render with 0 values in Investments on balance sheet")
    void willNotRenderDebtorsZeroValues() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetZeroValues());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }

    private BalanceSheet getMockBalanceSheet() {
        BalanceSheet balanceSheet = new BalanceSheet();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        FixedAssets fixedAssets = new FixedAssets();
        FixedInvestments fixedInvestments = new FixedInvestments();

        fixedInvestments.setCurrentAmount(1L);
        fixedInvestments.setPreviousAmount(2L);

        fixedAssets.setInvestments(fixedInvestments);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setFixedAssets(fixedAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }

    private BalanceSheet getMockBalanceSheetNoFixedInvestments() {
        BalanceSheet balanceSheet = new BalanceSheet();
        FixedAssets fixedAssets = new FixedAssets();

        TangibleAssets tangibleAssets = new TangibleAssets();

        tangibleAssets.setCurrentAmount(1L);
        tangibleAssets.setPreviousAmount(1L);

        fixedAssets.setTangibleAssets(tangibleAssets);
        balanceSheet.setFixedAssets(fixedAssets);
        return balanceSheet;
    }

    private BalanceSheet getMockBalanceSheetZeroValues() {
        BalanceSheet balanceSheet = new BalanceSheet();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        FixedAssets fixedAssets = new FixedAssets();
        FixedInvestments fixedInvestments = new FixedInvestments();

        fixedInvestments.setCurrentAmount(0L);
        fixedInvestments.setPreviousAmount(0L);

        fixedAssets.setInvestments(fixedInvestments);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setFixedAssets(fixedAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }
}
