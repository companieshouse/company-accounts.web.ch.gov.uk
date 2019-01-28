package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreditorsAfterOneYearControllerTests {

    @InjectMocks
    private CreditorsAfterOneYearController mockController;

    @Mock
    private NavigatorService mockNavigatorService;

    @Mock
    private CreditorsAfterOneYearService mockService;

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

    @Test
    @DisplayName("Get creditors after one year view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockService.getCreditorsAfterOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER)).thenReturn(new CreditorsAfterOneYear());

        this.mockMvc.perform(get(CREDITORS_AFTER_ONE_YEAR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(CREDITORS_AFTER_ONE_YEAR_VIEW))
                .andExpect(model().attributeExists(CREDITORS_AFTER_ONE_YEAR_MODEL_ATTR))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockService, times(1)).getCreditorsAfterOneYear(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Get creditors after one year view failure path due to error on creditors after " +
            "one year retrieval")
    void getRequestFailureInGetBalanceSheet() throws Exception {

        when(mockService.getCreditorsAfterOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(CREDITORS_AFTER_ONE_YEAR_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}
