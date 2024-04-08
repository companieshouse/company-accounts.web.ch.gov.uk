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
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
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
@Disabled
class CurrentAssetsInvestmentsControllerTests {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService mockNavigatorService;

    @Mock
    private NoteService<CurrentAssetsInvestments> mockCurrentAssetsInvestmentsService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @InjectMocks
    private CurrentAssetsInvestmentsController controller;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String SMALL_FULL_CURRENT_ASSETS_INVESTMENTS_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/current-assets-investments";

    private static final String CURRENT_ASSETS_INVESTMENTS_VIEW = "smallfull/currentAssetsInvestments";

    private static final String CURRENT_ASSETS_INVESTMENTS_MODEL_ATTR = "currentAssetsInvestments";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String ERROR_VIEW = "error";

    private static final String TEST_PATH = "currentAssetsInvestmentsDetails";


    @BeforeEach
    private void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get currentAssetsInvestments view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any()))
            .thenReturn(MOCK_CONTROLLER_PATH);
        when(mockCurrentAssetsInvestmentsService.get(
            TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS))
                .thenReturn(new CurrentAssetsInvestments());

        this.mockMvc.perform(get(SMALL_FULL_CURRENT_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CURRENT_ASSETS_INVESTMENTS_VIEW))
            .andExpect(model().attributeExists(CURRENT_ASSETS_INVESTMENTS_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get currentAssetsInvestments view failure path due to error on currentAssetsInvestments retrieval")
    void getRequestFailureInGetFixedAssetsInvestments() throws Exception {

        when(mockCurrentAssetsInvestmentsService.get(
            TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(SMALL_FULL_CURRENT_ASSETS_INVESTMENTS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post currentAssestInvestments success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);
        when(mockCurrentAssetsInvestmentsService.submit(
            anyString(), anyString(), any(CurrentAssetsInvestments.class), eq(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS)))
                .thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(SMALL_FULL_CURRENT_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, "Test"))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post currentAssetsInvestments failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(mockCurrentAssetsInvestmentsService).submit(
                anyString(), anyString(), any(CurrentAssetsInvestments.class), eq(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS));

        this.mockMvc.perform(post(SMALL_FULL_CURRENT_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, "Test"))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post currentAssetsInvestments failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(TEST_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockCurrentAssetsInvestmentsService.submit(
            anyString(), anyString(), any(CurrentAssetsInvestments.class), eq(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS)))
            .thenReturn(errors);

        this.mockMvc.perform(post(SMALL_FULL_CURRENT_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, "#¢¢#¢"))
            .andExpect(status().isOk())
            .andExpect(view().name(CURRENT_ASSETS_INVESTMENTS_VIEW));
    }

    @Test
    @DisplayName("Post currentAssetsInvestments with binding result error")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(SMALL_FULL_CURRENT_ASSETS_INVESTMENTS_PATH)
            .param(TEST_PATH, ""))
            .andExpect(status().isOk())
            .andExpect(view().name(CURRENT_ASSETS_INVESTMENTS_VIEW))
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
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetNoCurrentInvestments());

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
        CurrentAssets currentAssets = new CurrentAssets();
        uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssetsInvestments currentAssetsInvestments
            = new uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssetsInvestments();

        currentAssetsInvestments.setCurrentAmount(1L);
        currentAssetsInvestments.setPreviousAmount(2L);

        currentAssets.setInvestments(currentAssetsInvestments);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setCurrentAssets(currentAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }

    private BalanceSheet getMockBalanceSheetNoCurrentInvestments() {
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
        CurrentAssets currentAssets = new CurrentAssets();
        uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssetsInvestments currentAssetsInvestments
            = new uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssetsInvestments();

        currentAssetsInvestments.setCurrentAmount(0L);
        currentAssetsInvestments.setPreviousAmount(0L);

        currentAssets.setInvestments(currentAssetsInvestments);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setCurrentAssets(currentAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }
}
