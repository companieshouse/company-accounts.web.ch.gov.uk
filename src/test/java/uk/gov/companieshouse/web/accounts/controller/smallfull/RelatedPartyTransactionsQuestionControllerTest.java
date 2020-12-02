package uk.gov.companieshouse.web.accounts.controller.smallfull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.HashMap;
import java.util.Map;
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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RelatedPartyTransactionsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RelatedPartyTransactionsLinks;
import uk.gov.companieshouse.web.accounts.api.impl.ApiClientServiceImpl;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.state.CompanyAccountsDataState;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.RelatedPartyTransactionsServiceImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RelatedPartyTransactionsQuestionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private RelatedPartyTransactionsServiceImpl relatedPartyTransactionsService;

    @Mock
    private ApiClientServiceImpl apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private RelatedPartyTransactionsApi relatedPartyTransactionsApi;

    @Mock
    private RelatedPartyTransactionsLinks relatedPartyTransactionsLinks;

    @InjectMocks
    private RelatedPartyTransactionsQuestionController controller;

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String RELATED_PARTY_TRANSACTIONS_QUESTION_PATH = "/company/" + COMPANY_NUMBER +
                                                                "/transaction/" + TRANSACTION_ID +
                                                                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                                "/small-full/notes/related-party-transactions-question";

    private static final String RELATED_PARTY_TRANSACTIONS_QUESTION_MODEL_ATTR = "relatedPartyTransactionsQuestion";

    private static final String TEMPLATE_NAME_MODEL_ATTR = "templateName";

    private static final String RELATED_PARTY_TRANSACTIONS_QUESTION_VIEW = "smallfull/relatedPartyTransactionsQuestion";

    private static final String RELATED_PARTY_TRANSACTIONS_SELECTION = "hasIncludedRelatedPartyTransactions";

    private static final String MOCK_CONTROLLER_PATH = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "mockControllerPath";

    private static final String COMPANY_ACCOUNTS_DATA_STATE = "companyAccountsDataState";

    private static final String ERROR_VIEW = "error";

    @BeforeEach
    private void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get related party transactions question without transactions")
    void getRequestEmptyRelatedPartTransactions() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(relatedPartyTransactionsApi);

        this.mockMvc.perform(get(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH)
            .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
            .andExpect(status().isOk())
            .andExpect(view().name(RELATED_PARTY_TRANSACTIONS_QUESTION_VIEW))
            .andExpect(model().attributeExists(RELATED_PARTY_TRANSACTIONS_QUESTION_MODEL_ATTR))
            .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }


    @Test
    @DisplayName("Get related party transactions question with transactions")
    void getRequestWithTransactions() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(relatedPartyTransactionsApi);
        when(relatedPartyTransactionsApi.getTransactions()).thenReturn(getTransactions());

        this.mockMvc.perform(get(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH)
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().isOk())
                .andExpect(view().name(RELATED_PARTY_TRANSACTIONS_QUESTION_VIEW))
                .andExpect(model().attributeExists(RELATED_PARTY_TRANSACTIONS_QUESTION_MODEL_ATTR))
                .andExpect(model().attributeExists(TEMPLATE_NAME_MODEL_ATTR));
    }

    @Test
    @DisplayName("Get related party transactions question - Throws exception")
    void getRequestThrowsException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(get(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH)
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post related party transactions - has not included related party transactions")
    void postRequestHasNotIncludedRelatedPartyTransactions() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(relatedPartyTransactionsApi);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);
        
        this.mockMvc.perform(post(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH)
            .param(RELATED_PARTY_TRANSACTIONS_SELECTION, "0")
            .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post related party transactions - has included related party transactions")
    void postRequestHasIncludedRelatedPartyTransactions() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH)
                .param(RELATED_PARTY_TRANSACTIONS_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post related party transactions - has included related party transactions with existing api resource")
    void postRequestHasIncludedRelatedPartyTransactionsWithExistingAPi() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(relatedPartyTransactionsApi);

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH)
                .param(RELATED_PARTY_TRANSACTIONS_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post related party transactions - throws exception")
    void postRequestThrowsException() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        this.mockMvc.perform(post(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH)
                .param(RELATED_PARTY_TRANSACTIONS_SELECTION, "1")
                .sessionAttr(COMPANY_ACCOUNTS_DATA_STATE, new CompanyAccountsDataState()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post related party transactions - binding result errors")
    void postRequestBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(RELATED_PARTY_TRANSACTIONS_QUESTION_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(RELATED_PARTY_TRANSACTIONS_QUESTION_VIEW));
    }

    private Map<String, String> getTransactions() {
        Map<String, String> transactions = new HashMap<String, String>();
        transactions.put("id", "transactions");

        return transactions;
    }
}
