package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
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
public class DebtorsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NoteService<Debtors> mockDebtorsService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private NavigatorService mockNavigatorService;

    @InjectMocks
    private DebtorsController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String DEBTORS_PATH = SMALL_FULL_PATH + "/debtors";

    private static final String DEBTORS_MODEL_ATTR = "debtors";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String DEBTORS_VIEW = "smallfull/debtors";

    private static final String ERROR_VIEW = "error";

    private static final String TEST_PATH = "tradeDebtors.currentTradeDebtors";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get debtors view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockDebtorsService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_DEBTORS)).thenReturn(new Debtors());

        this.mockMvc.perform(get(DEBTORS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(DEBTORS_VIEW))
            .andExpect(model().attributeExists(DEBTORS_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockDebtorsService, times(1)).get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_DEBTORS);
    }

    @Test
    @DisplayName("Get debtors view failure path due to error on debtors retrieval")
    void getRequestFailureInGetBalanceSheet() throws Exception {

        when(mockDebtorsService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_DEBTORS)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(DEBTORS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post debtors success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockDebtorsService.submit(anyString(), anyString(), any(Debtors.class), NoteType.SMALL_FULL_DEBTORS)).thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(DEBTORS_PATH))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post debtors failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(mockDebtorsService).submit(anyString(), anyString(), any(Debtors.class), NoteType.SMALL_FULL_DEBTORS);

        this.mockMvc.perform(post(DEBTORS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post debtors failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(TEST_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockDebtorsService.submit(anyString(), anyString(), any(Debtors.class), NoteType.SMALL_FULL_DEBTORS)).thenReturn(errors);

        this.mockMvc.perform(post(DEBTORS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(DEBTORS_VIEW));
    }

    @Test
    @DisplayName("Test will render with Debtors present on balancesheet")
    void willRenderDebtorsPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheet());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertTrue(renderPage);
    }

    @Test
    @DisplayName("Test will render with Debtors not present on balancesheet")
    void willRenderDebtorsNotPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetNoDebtors());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }

    @Test
    @DisplayName("Test will not render with 0 values in debtors on balance sheet")
    void willNotRenderDebtorsZeroValues() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetZeroValues());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }

    @Test
    @DisplayName("Post debtors with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = TEST_PATH;
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "test";

        this.mockMvc.perform(post(DEBTORS_PATH)
            .param(beanElement, invalidData))
            .andExpect(status().isOk())
            .andExpect(view().name(DEBTORS_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    private BalanceSheet getMockBalanceSheet() {
        BalanceSheet balanceSheet = new BalanceSheet();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        CurrentAssets currentAssets = new CurrentAssets();
        uk.gov.companieshouse.web.accounts.model.smallfull.Debtors debtors = new uk.gov.companieshouse.web.accounts.model.smallfull.Debtors();

        debtors.setCurrentAmount(1L);
        debtors.setPreviousAmount(2L);

        currentAssets.setDebtors(debtors);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setCurrentAssets(currentAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }

    private BalanceSheet getMockBalanceSheetNoDebtors() {
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
        uk.gov.companieshouse.web.accounts.model.smallfull.Debtors debtors = new uk.gov.companieshouse.web.accounts.model.smallfull.Debtors();

        debtors.setCurrentAmount(0L);
        debtors.setPreviousAmount(0L);

        currentAssets.setDebtors(debtors);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setCurrentAssets(currentAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }
}
