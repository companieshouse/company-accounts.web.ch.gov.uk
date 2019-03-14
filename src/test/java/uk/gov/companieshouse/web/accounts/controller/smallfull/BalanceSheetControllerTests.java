package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BalanceSheetControllerTests {

    private MockMvc mockMvc;

    @Mock
    private BalanceSheetService balanceSheetService;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private BalanceSheetController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String BALANCE_SHEET_PATH = "/company/" + COMPANY_NUMBER +
                                                     "/transaction/" + TRANSACTION_ID +
                                                     "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                     "/small-full/balance-sheet";

    private static final String BALANCE_SHEET_MODEL_ATTR = "balanceSheet";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String BALANCE_SHEET_VIEW = "smallfull/balanceSheet";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get balance sheet view success path")
    void getRequestSuccess() throws Exception {

        when(balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(new BalanceSheet());

        this.mockMvc.perform(get(BALANCE_SHEET_PATH))
                    .andExpect(status().isOk())
                    .andExpect(view().name(BALANCE_SHEET_VIEW))
                    .andExpect(model().attributeExists(BALANCE_SHEET_MODEL_ATTR))
                    .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(balanceSheetService, times(1)).getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Get balance sheet view failure path due to error on balance sheet retrieval")
    void getRequestFailureInGetBalanceSheet() throws Exception {

        doThrow(ServiceException.class)
                .when(balanceSheetService).getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        this.mockMvc.perform(get(BALANCE_SHEET_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post balance sheet success path")
    void postRequestSuccess() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(balanceSheetService.postBalanceSheet(anyString(), anyString(), any(BalanceSheet.class), anyString())).thenReturn(new ArrayList<>());

        this.mockMvc.perform(post(BALANCE_SHEET_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post balance sheet failure path")
    void postRequestFailure() throws Exception {

        doThrow(ServiceException.class)
                .when(balanceSheetService).postBalanceSheet(anyString(), anyString(), any(BalanceSheet.class), anyString());

        this.mockMvc.perform(post(BALANCE_SHEET_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Post balance sheet failure path with API validation errors")
    void postRequestFailureWithApiValidationErrors() throws Exception {

        ValidationError validationError = new ValidationError();
        validationError.setFieldPath("calledUpShareCapitalNotPaid");
        validationError.setMessageKey("invalid_character");

        List<ValidationError> errors = new ArrayList<>();
        errors.add(validationError);

        when(balanceSheetService.postBalanceSheet(anyString(), anyString(), any(BalanceSheet.class), anyString())).thenReturn(errors);

        this.mockMvc.perform(post(BALANCE_SHEET_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(BALANCE_SHEET_VIEW));
    }
    
    @Test
    @DisplayName("Post balance sheet with binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        String beanElement = "calledUpShareCapitalNotPaid.currentAmount";
        // Mock non-numeric input to trigger binding result errors
        String invalidData = "test";

        this.mockMvc.perform(post(BALANCE_SHEET_PATH)
                .param(beanElement, invalidData))
                .andExpect(status().isOk())
                .andExpect(view().name(BALANCE_SHEET_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}
