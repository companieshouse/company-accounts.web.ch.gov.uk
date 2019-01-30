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
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsWithinOneYearService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
public class CreditorsWithinOneYearControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreditorsWithinOneYearService mockCreditorsWithinOneYearService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private NavigatorService mockNavigatorService;

    @InjectMocks
    private CreditorsWithinOneYearController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SMALL_FULL_PATH = "/company/" + COMPANY_NUMBER +
        "/transaction/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String CREDITORS_WITHIN_ONE_YEAR_PATH = SMALL_FULL_PATH + "/creditors-within-one-year";

    private static final String CREDITORS_WITHIN_ONE_YEAR_MODEL_ATTR = "creditorsWithinOneYear";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String CREDITORS_WITHIN_ONE_YEAR_VIEW = "smallfull/creditorsWithinOneYear";

    private static final String ERROR_VIEW = "error";

    private static final String TEST_PATH = "tradeCreditors.currentTradeCreditors";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get creditors within one year view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockCreditorsWithinOneYearService.getCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(new CreditorsWithinOneYear());

        this.mockMvc.perform(get(CREDITORS_WITHIN_ONE_YEAR_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CREDITORS_WITHIN_ONE_YEAR_VIEW))
            .andExpect(model().attributeExists(CREDITORS_WITHIN_ONE_YEAR_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockCreditorsWithinOneYearService, times(1)).getCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Get creditors within one year view failure path due to error on creditors within one year retrieval")
    void getRequestFailureInGetBalanceSheet() throws Exception {

        when(mockCreditorsWithinOneYearService.getCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(CREDITORS_WITHIN_ONE_YEAR_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post creditors within one year success path")
    void postRequestSuccess() throws Exception {

        when(mockNavigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockCreditorsWithinOneYearService.submitCreditorsWithinOneYear(anyString(), anyString(), any(CreditorsWithinOneYear.class), anyString())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(CREDITORS_WITHIN_ONE_YEAR_PATH))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post creditors within one year failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
            .when(mockCreditorsWithinOneYearService).submitCreditorsWithinOneYear(anyString(), anyString(), any(CreditorsWithinOneYear.class), anyString());

        this.mockMvc.perform(post(CREDITORS_WITHIN_ONE_YEAR_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post creditors within one year failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath(TEST_PATH);
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(mockCreditorsWithinOneYearService.submitCreditorsWithinOneYear(anyString(), anyString(), any(CreditorsWithinOneYear.class), anyString())).thenReturn(errors);

        this.mockMvc.perform(post(CREDITORS_WITHIN_ONE_YEAR_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(CREDITORS_WITHIN_ONE_YEAR_VIEW));
    }

    @Test
    @DisplayName("Test will render with Creditors Within One Year present on balancesheet")
    void willRenderCreditorsWithinOneYearPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetWithCreditorsWithinOneYear());

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test will not render with Creditors Within One Year missing from balancesheet")
    void willRenderWhenCreditorsWithinOneYearNotPresent() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetWithoutCreditorsWithinOneYear());

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
    
    @Test
    @DisplayName("Test will not render with Creditors Within One Year zero from balancesheet")
    void willRenderWhenCreditorsWithinOneYearZero() throws Exception {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(getMockBalanceSheetWithZeroCreditorsWithinOneYear());

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test will not render when there is a service exception")
    void willRenderWithCreditorsWithinOneYearServiceException() throws ServiceException {
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class,
            () -> controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Post creditors within one year with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = TEST_PATH;
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "test";

        this.mockMvc.perform(post(CREDITORS_WITHIN_ONE_YEAR_PATH)
            .param(beanElement, invalidData))
            .andExpect(status().isOk())
            .andExpect(view().name(CREDITORS_WITHIN_ONE_YEAR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    private BalanceSheet getMockBalanceSheetWithCreditorsWithinOneYear() {
        BalanceSheet balanceSheet = new BalanceSheet();
        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();
        CreditorsDueWithinOneYear creditorsWithinOneYear = new CreditorsDueWithinOneYear();

        creditorsWithinOneYear.setCurrentAmount(1L);
        creditorsWithinOneYear.setPreviousAmount(2L);

        otherLiabilitiesOrAssets.setCreditorsDueWithinOneYear(creditorsWithinOneYear);

        balanceSheetHeadings.setCurrentPeriodHeading("currentBalanceSheetHeading");
        balanceSheetHeadings.setPreviousPeriodHeading("previousBalanceSheetHeading");

        balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);

        balanceSheet.setBalanceSheetHeadings(balanceSheetHeadings);
        return balanceSheet;
    }

    private BalanceSheet getMockBalanceSheetWithoutCreditorsWithinOneYear() {
        BalanceSheet balanceSheet = new BalanceSheet();
        FixedAssets fixedAssets = new FixedAssets();

        TangibleAssets tangibleAssets = new TangibleAssets();

        tangibleAssets.setCurrentAmount(1L);
        tangibleAssets.setPreviousAmount(1L);

        fixedAssets.setTangibleAssets(tangibleAssets);
        balanceSheet.setFixedAssets(fixedAssets);
        return balanceSheet;
    }
    
    private BalanceSheet getMockBalanceSheetWithZeroCreditorsWithinOneYear() {
      BalanceSheet balanceSheet = new BalanceSheet();
      OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();
      CreditorsDueWithinOneYear creditorsWithinOneYear = new CreditorsDueWithinOneYear();

      creditorsWithinOneYear.setCurrentAmount(0L);
      creditorsWithinOneYear.setPreviousAmount(0L);

      otherLiabilitiesOrAssets.setCreditorsDueWithinOneYear(creditorsWithinOneYear);

      balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);
      return balanceSheet;
  }
}
