package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatementsControllerTests {

    private MockMvc mockMvc;

    @Mock
    private StatementsService statementsService;

    @InjectMocks
    private StatementsController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String STATEMENTS_PATH = "/company/" + COMPANY_NUMBER +
                                                    "/transaction/" + TRANSACTION_ID +
                                                    "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                    "/small-full/balance-sheet-statements";

    private static final String BASIS_OF_PREPARATION_PATH = "/company/" + COMPANY_NUMBER +
                                                            "/transaction/" + TRANSACTION_ID +
                                                            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                            "/small-full/basis-of-preparation";

    private static final String BACK_BUTTON_MODEL_ATTR = "backButton";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String STATEMENTS_MODEL_ATTR = "statements";

    private static final String STATEMENTS_VIEW = "smallfull/statements";

    private static final String ERROR_VIEW = "error";

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get statements view - success path")
    void getRequestSuccess() throws Exception {

        when(statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(new Statements());

        this.mockMvc.perform(get(STATEMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(STATEMENTS_VIEW))
                .andExpect(model().attributeExists(BACK_BUTTON_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR))
                .andExpect(model().attributeExists(STATEMENTS_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get statements view - statements service exception")
    void getRequestStatementsServiceException() throws Exception {

        when(statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(get(STATEMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Accept statements - success path")
    void postRequestSuccess() throws Exception {

        doNothing().when(statementsService).acceptBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(post(STATEMENTS_PATH))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + BASIS_OF_PREPARATION_PATH));
    }

    @Test
    @DisplayName("Accept statements - statements service exception")
    void postRequestStatementsServiceException() throws Exception {

        doThrow(ServiceException.class)
                .when(statementsService).acceptBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        this.mockMvc.perform(post(STATEMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }


}
