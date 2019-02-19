package uk.gov.companieshouse.web.accounts.controller.smallfull;

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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get stocks view success path")
    void getRequestSuccess() throws Exception {

        when(mockNavigatorService.getPreviousControllerPath(any(), any())).thenReturn(MOCK_CONTROLLER_PATH);
        when(mockStocksService.getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(new StocksNote());

        this.mockMvc.perform(get(SMALL_FULL_STOCKS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(STOCKS_VIEW))
            .andExpect(model().attributeExists(STOCKS_MODEL_ATTR))
            .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));

        verify(mockStocksService, times(1)).getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);
    }

    @Test
    @DisplayName("Get stocks view failure path due to error on stocks retrieval")
    void getRequestFailureInGetBalanceSheet() throws Exception {

        when(mockStocksService.getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(SMALL_FULL_STOCKS_PATH))
            .andExpect(status().isOk())
            .andExpect(view().name(ERROR_VIEW))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }
}
