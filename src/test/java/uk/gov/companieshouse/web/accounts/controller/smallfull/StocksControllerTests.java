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
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
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
public class StocksControllerTests {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService mockNavigatorService;

    @Mock
    private StocksService mockStocksService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @InjectMocks
    private StocksController controller;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String SMALL_FULL_STOCKS_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/stocks";

    private static final String STOCKS_VIEW = "smallfull/stocks";

    private static final String STOCKS_MODEL_ATTR = "stocksNote";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String ERROR_VIEW = "error";

    private static final String TEST_PATH = "stocks.currentStocks";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get stocks view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockStocksService.getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(new StocksNote());

        this.mockMvc.perform(get(SMALL_FULL_STOCKS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(STOCKS_VIEW))
            .andExpect(model().attributeExists(STOCKS_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockStocksService, times(1))
            .getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Get stocks view failure path due to error on stocks retrieval")
    void getRequestFailureInGetBalanceSheet() throws Exception {

        when(mockStocksService.getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(SMALL_FULL_STOCKS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post stocks success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any()))
            .thenReturn(MOCK_CONTROLLER_PATH);
        when(mockStocksService.submitStocks(anyString(), anyString(), any(StocksNote.class), anyString()))
            .thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(STOCKS_PATH))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post stocks failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(mockStocksService).submitStocks(anyString(), anyString(), any(StocksNote.class), anyString());

        this.mockMvc.perform(post(STOCKS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post stocks failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(TEST_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockStocksService.submitStocks(anyString(), anyString(), any(StocksNote.class), anyString()))
            .thenReturn(errors);

        this.mockMvc.perform(post(STOCKS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(STOCKS_VIEW));
    }

    @Test
    @DisplayName("Test will render with stocks present on balancesheet")
    void willRenderStocksPresent() throws Exception {

        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(getMockBalanceSheet());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertTrue(renderPage);
    }

    @Test
    @DisplayName("Test will render with stocks not present on balancesheet")
    void willRenderStocksNotPresent() throws Exception {

        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(getMockBalanceSheetNoDebtors());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }

    @Test
    @DisplayName("Test will not render with 0 values in stocks on balance sheet")
    void willNotRenderStocksZeroValues() throws Exception {

        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(getMockBalanceSheetZeroValues());

        boolean renderPage = controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertFalse(renderPage);
    }

    @Test
    @DisplayName("Post stocks with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = TEST_PATH;
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "test";

        this.mockMvc.perform(post(STOCKS_PATH)
            .param(beanElement, invalidData))
            .andExpect(status().isOk())
            .andExpect(view().name(STOCKS_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    private BalanceSheet getMockBalanceSheet() {

        BalanceSheet balanceSheet = new BalanceSheet();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        CurrentAssets currentAssets = new CurrentAssets();
        Stocks stocks = new Stocks();

        stocks.setCurrentAmount(1L);
        stocks.setPreviousAmount(2L);

        currentAssets.setStocks(stocks);

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
        Stocks stocks = new Stocks();

        stocks.setCurrentAmount(0L);
        stocks.setPreviousAmount(0L);

        currentAssets.setStocks(stocks);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setCurrentAssets(currentAssets);
        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);

        return balanceSheet;
    }
}
