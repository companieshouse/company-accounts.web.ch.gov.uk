package uk.gov.companieshouse.web.accounts.controller.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class IntangibleAssetsNoteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IntangibleAssetsNoteService intangibleAssetsNoteService;

    @Mock
    private IntangibleAssets intangibleAssets;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private IntangibleAssetsNoteController intangibleAssetsNoteController;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @InjectMocks
    private IntangibleAssetsNoteController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String INTANGIBLE_VIEW = "smallfull/intangibleAssetsNote";

    private static final String INTANGIBLE_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/note/intangible-assets";

    private static final String MODEL_ATTR_BACK_PAGE = "backButton";

    private static final String MODEL_ATTR_TEMPLATE = "templateName";

    private static final String MODEL_ATTR_ASSET = "intangibleAssets";

    private static final String MOCK_CONTROLLER_PATH_NEXT = "nextControllerPath";

    private static final String MOCK_CONTROLLER_PATH_PREVIOUS = "previousControllerPath";

    private static final String ERROR_VIEW = "error";

    private void setUpMockMvc() {
        when(navigatorService.getPreviousControllerPath(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH_PREVIOUS);
        mockMvc = MockMvcBuilders.standaloneSetup(intangibleAssetsNoteController).build();
    }

    @Test
    @DisplayName("Get intangible asset note view - success path")
    void getRequestSuccess() throws Exception {

        setUpMockMvc();

        when(intangibleAssetsNoteService
            .getIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(intangibleAssets);

        mockMvc.perform(get(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(INTANGIBLE_VIEW))
            .andExpect(model().attributeExists(MODEL_ATTR_BACK_PAGE))
            .andExpect(model().attributeExists(MODEL_ATTR_TEMPLATE))
            .andExpect(model().attributeExists(MODEL_ATTR_ASSET));
    }

    @Test
    @DisplayName("Get intangible asset note - service exception")
    void getRequestServiceException() throws Exception {

        setUpMockMvc();

        doThrow(ServiceException.class)
            .when(intangibleAssetsNoteService)
            .getIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        mockMvc.perform(get(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post intangible asset note - success path")
    void postRequestSuccess() throws Exception {

        setUpMockMvc();

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH_NEXT);

        when(
            intangibleAssetsNoteService.postIntangibleAssets(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                IntangibleAssets.class), eq(COMPANY_NUMBER)))
            .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(true);

        mockMvc.perform(post(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(MOCK_CONTROLLER_PATH_NEXT));
    }

    @Test
    @DisplayName("Post intangible asset note - validation errors")
    void postRequestValidationErrors() throws Exception {

        setUpMockMvc();

        when(
            intangibleAssetsNoteService.postIntangibleAssets(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                IntangibleAssets.class), eq(COMPANY_NUMBER)))
            .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        mockMvc.perform(post(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(INTANGIBLE_VIEW))
            .andExpect(model().attributeExists(MODEL_ATTR_BACK_PAGE));
    }

    @Test
    @DisplayName("Post intangible asset note - service exception")
    void postRequestServiceException() throws Exception {

        setUpMockMvc();

        doThrow(ServiceException.class)
            .when(intangibleAssetsNoteService)
            .postIntangibleAssets(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(
                IntangibleAssets.class), eq(COMPANY_NUMBER));

        mockMvc.perform(post(INTANGIBLE_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post intangible assets note - binding errors")
    void postRequestBindingErrors() throws Exception {

        setUpMockMvc();

        mockMvc.perform(post(INTANGIBLE_PATH)
        .param("cost.atPeriodStart.goodwill", "abc"))
            .andExpect(status().isOk())
            .andExpect(view().name(INTANGIBLE_VIEW))
            .andExpect(model().attributeExists(MODEL_ATTR_BACK_PAGE));
    }

    @Test
    @DisplayName("Test will render with Intangible present on balancesheet")
    void willRenderDebtorsPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheet());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertTrue(renderPage);
    }

    @Test
    @DisplayName("Test will render with Intangible not present on balancesheet")
    void willRenderDebtorsNotPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetNoIntangibleAssets());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }

    @Test
    @DisplayName("Test will not render with 0 values in intangible on balance sheet")
    void willNotRenderDebtorsZeroValues() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetZeroValues());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }
    
    private BalanceSheet getMockBalanceSheet() {
        BalanceSheet balanceSheet = new BalanceSheet();
        FixedAssets fixedAssets = new FixedAssets();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();

        uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets intangibleAssets = new uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets();

        intangibleAssets.setCurrentAmount(1L);
        intangibleAssets.setPreviousAmount(1L);

        fixedAssets.setIntangibleAssets(intangibleAssets);
        balanceSheet.setFixedAssets(fixedAssets);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }
    private BalanceSheet getMockBalanceSheetNoIntangibleAssets() {
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
        FixedAssets fixedAssets = new FixedAssets();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();

        uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets intangibleAssets = new uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets();

        intangibleAssets.setCurrentAmount(0L);
        intangibleAssets.setPreviousAmount(0L);

        fixedAssets.setIntangibleAssets(intangibleAssets);
        balanceSheet.setFixedAssets(fixedAssets);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }
}
