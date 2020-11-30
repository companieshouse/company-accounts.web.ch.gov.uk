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
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.RptTransactionService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AddOrRemoveRptTransactionsControllerTest {

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH = "/company/" + COMPANY_NUMBER +
            "/transaction/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/small-full/note/add-or-remove-transactions";

    private static final String ADD_OR_REMOVE_RPT_TRANSACTIONS_VIEW = "smallfull/addOrRemoveTransactions";

    private static final String ADD_OR_REMOVE_RPT_TRANSACTIONS_MODEL_ATTR = "addOrRemoveTransactions";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String ERROR_VIEW = "error";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private MockMvc mockMvc;

    @Mock
    private RptTransactionService rptTransactionService;

    @Mock
    private NavigatorService navigatorService;

    @InjectMocks
    private AddOrRemoveRptTransactionsController controller;


    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get add or remove RPT transactions view - success path")
    void getRequestSuccess() throws Exception {

        when(rptTransactionService.getAllRptTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(new RptTransaction[0]);

        this.mockMvc.perform(get(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_RPT_TRANSACTIONS_VIEW))
                .andExpect(model().attributeExists(ADD_OR_REMOVE_RPT_TRANSACTIONS_MODEL_ATTR))
                .andExpect(model().attributeExists(COMPANY_NUMBER))
                .andExpect(model().attributeExists(TRANSACTION_ID))
                .andExpect(model().attributeExists(COMPANY_ACCOUNTS_ID))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get add or remove RPT transactions view - service exception")
    void getRequestServiceException() throws Exception {

        when(rptTransactionService.getAllRptTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add director - throws service exception")
    void postDirectorAddRequestThrowsServiceException() throws Exception {

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?submit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }
}