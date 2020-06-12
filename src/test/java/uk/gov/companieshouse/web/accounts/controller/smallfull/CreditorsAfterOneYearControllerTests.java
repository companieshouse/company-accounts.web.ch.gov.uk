package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import java.util.ArrayList;
import java.util.List;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.service.NoteService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditorsAfterOneYearControllerTests {

    @InjectMocks
    private CreditorsAfterOneYearController mockController;

    @Mock
    private NavigatorService mockNavigatorService;

    @Mock
    private NoteService<CreditorsAfterOneYear> mockService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(mockController).build();
    }

    private MockMvc mockMvc;

    private static final String MOCK_CONTROLLER_PATH =
            UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full";

    private static final String CREDITORS_AFTER_ONE_YEAR_PATH = SMALL_FULL_PATH + "/creditors" +
            "-after-more-than-one-year";

    private static final String CREDITORS_AFTER_ONE_YEAR_MODEL_ATTR = "creditorsAfterOneYear";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String CREDITORS_AFTER_ONE_YEAR_VIEW = "smallfull/creditorsAfterOneYear";

    private static final String ERROR_VIEW = "error";

    private static final String CURRENT_TOTAL_PATH = "total.currentTotal";

    @Test
    @DisplayName("Get creditors after one year view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR)).thenReturn(new CreditorsAfterOneYear());

        this.mockMvc.perform(get(CREDITORS_AFTER_ONE_YEAR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CREDITORS_AFTER_ONE_YEAR_VIEW))
                .andExpect(model().attributeExists(CREDITORS_AFTER_ONE_YEAR_MODEL_ATTR))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockService, times(1)).get(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR);
    }

    @Test
    @DisplayName("Get creditors after one year view failure path due to error on creditors after " +
            "one year retrieval")
    void getRequestFailureInGetBalanceSheet() throws Exception {

        when(mockService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(CREDITORS_AFTER_ONE_YEAR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post creditors after one year success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockService.submit(anyString(), anyString(), any(CreditorsAfterOneYear.class),
                        eq(NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR)))
                                        .thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(CREDITORS_AFTER_ONE_YEAR_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post creditors after one year failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class).when(mockService).submit(anyString(), anyString(),
                        any(CreditorsAfterOneYear.class),
                        eq(NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR));

        this.mockMvc.perform(post(CREDITORS_AFTER_ONE_YEAR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
    @Test
    @DisplayName("Post creditors after one year with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = CURRENT_TOTAL_PATH;
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "test";

        this.mockMvc.perform(post(CREDITORS_AFTER_ONE_YEAR_PATH)
                .param(beanElement, invalidData))
                .andExpect(status().isOk())
                .andExpect(view().name(CREDITORS_AFTER_ONE_YEAR_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Test will render with Creditors After One Year present on balancesheet")
    void willRenderCreditorsAfterOneYearPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheetWithCreditorsAfterOneYear());

        assertTrue(mockController.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test will not render with Creditors After One Year missing from balancesheet")
    void willRenderWhenCreditorsaFTEROneYearNotPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheetWithoutCreditorsAfterOneYear());

        assertFalse(mockController.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test will not render with Creditors After One Year zero from balancesheet")
    void willRenderWhenCreditorsAfterOneYearZero() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheetWithZeroCreditorsAfterOneYear());

        assertFalse(mockController.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test will not render when there is a service exception")
    void willRenderWithCreditorsAfterOneYearServiceException() throws ServiceException {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class,
                () -> mockController.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Post creditors after one year failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(CURRENT_TOTAL_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockService.submit(anyString(), anyString(), any(CreditorsAfterOneYear.class),
                        eq(NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR))).thenReturn(errors);

        this.mockMvc.perform(post(CREDITORS_AFTER_ONE_YEAR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CREDITORS_AFTER_ONE_YEAR_VIEW));
    }

    private BalanceSheet mockBalanceSheetWithCreditorsAfterOneYear() {
        BalanceSheet balanceSheet = new BalanceSheet();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();
        uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear creditorsAfterOneYear = new uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear();

        creditorsAfterOneYear.setCurrentAmount(1L);
        creditorsAfterOneYear.setPreviousAmount(2L);

        otherLiabilitiesOrAssets.setCreditorsAfterOneYear(creditorsAfterOneYear);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }

    private BalanceSheet mockBalanceSheetWithoutCreditorsAfterOneYear() {
        BalanceSheet balanceSheet = new BalanceSheet();
        FixedAssets fixedAssets = new FixedAssets();

        TangibleAssets tangibleAssets = new TangibleAssets();

        tangibleAssets.setCurrentAmount(1L);
        tangibleAssets.setPreviousAmount(1L);

        fixedAssets.setTangibleAssets(tangibleAssets);
        balanceSheet.setFixedAssets(fixedAssets);
        return balanceSheet;
    }

    private BalanceSheet mockBalanceSheetWithZeroCreditorsAfterOneYear() {
        BalanceSheet balanceSheet = new BalanceSheet();
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();
        uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear creditorsAfterOneYear = new uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear();

        creditorsAfterOneYear.setCurrentAmount(0L);
        creditorsAfterOneYear.setPreviousAmount(0L);

        otherLiabilitiesOrAssets.setCreditorsAfterOneYear(creditorsAfterOneYear);

        balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);
        return balanceSheet;
    }
}
