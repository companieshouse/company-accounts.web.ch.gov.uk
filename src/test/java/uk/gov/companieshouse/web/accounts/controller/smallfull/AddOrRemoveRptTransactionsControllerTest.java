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
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RelatedPartyTransactionsApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.AddOrRemoveRptTransactions;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.navigation.NavigatorService;
import uk.gov.companieshouse.web.accounts.service.smallfull.RelatedPartyTransactionsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.RptTransactionService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
    private HttpServletRequest request;

    @Mock
    private RptTransactionService rptTransactionService;

    @Mock
    private NavigatorService navigatorService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private RelatedPartyTransactionsService relatedPartyTransactionsService;

    @Mock
    private RelatedPartyTransactionsApi relatedPartyTransactionsApi;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private CompanyService companyService;

    @Mock
    private CompanyProfileApi companyProfileApi;

    @InjectMocks
    private AddOrRemoveRptTransactionsController controller;

    @BeforeEach
    private void setup() {

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Get add or remove RPT transactions view - success path")
    void getRequestSuccess() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(rptTransactionService.getAllRptTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(new RptTransaction[0]);
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfileApi);
        when(companyService.isMultiYearFiler(companyProfileApi)).thenReturn(true);

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
        when(companyService.getCompanyProfile(COMPANY_NUMBER)).thenReturn(companyProfileApi);

        this.mockMvc.perform(get(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post submit RPT transaction - throws service exception")
    void postRptTransactionSubmitAddRequestThrowsServiceException() throws Exception {

        when(rptTransactionService.submitAddOrRemoveRptTransactions(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveRptTransactions.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?submit")
                .param("rptTransactionToAdd.nameOfRelatedParty", "nameOfRelatedParty"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post add RPT transaction - success")
    void postRptTransactionAddRequestSuccess() throws Exception {

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?add")
                .param("rptTransactionToAdd.nameOfRelatedParty", "nameOfRelatedParty"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(UrlBasedViewResolver.REDIRECT_URL_PREFIX + ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH));
    }

    @Test
    @DisplayName("Post add RPT transaction - throws service exception")
    void postRptTransactionAddRequestThrowsServiceException() throws Exception {

        when(rptTransactionService.createRptTransaction(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveRptTransactions.class)))
                .thenThrow(ServiceException.class);

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?add")
                .param("rptTransactionToAdd.nameOfRelatedParty", "nameOfRelatedParty"))
                .andExpect(status().isOk())
                .andExpect(view().name(ERROR_VIEW));
    }

    @Test
    @DisplayName("Post submit or remove RPT transaction view - success")
    void postRequestSubmitAddOrRemoveRptTransactionSuccess() throws Exception {

        when(rptTransactionService.submitAddOrRemoveRptTransactions(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveRptTransactions.class))).thenReturn(new ArrayList<>());

        when(navigatorService.getNextControllerRedirect(any(), ArgumentMatchers.<String>any())).thenReturn(MOCK_CONTROLLER_PATH);

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?submit")
                .param("rptTransactionToAdd.nameOfRelatedParty", "nameOfRelatedParty"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name(MOCK_CONTROLLER_PATH));
    }

    @Test
    @DisplayName("Post add Rpt transaction - throws binding result errors")
    void postRptTransactionAddRequestThrowsBindingResultErrors() throws Exception {

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?add")
                .param("rptTransactionToAdd.breakdown.balanceAtPeriodStart", "invalid"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_RPT_TRANSACTIONS_VIEW));

        verify(rptTransactionService, never()).createRptTransaction(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveRptTransactions.class));
    }

    @Test
    @DisplayName("Post submit Rpt transaction - throws validation errors")
    void postRptTransactionSubmitRequestThrowsValidationErrors() throws Exception {

        when(rptTransactionService.submitAddOrRemoveRptTransactions(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveRptTransactions.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?submit")
                .param("rptTransactionToAdd.nameOfRelatedParty", "nameOfRelatedParty"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_RPT_TRANSACTIONS_VIEW));
    }

    @Test
    @DisplayName("Post add Rpt transaction - throws validation errors")
    void postRptTransactionAddRequestThrowsValidationErrors() throws Exception {

        when(rptTransactionService.createRptTransaction(
                eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AddOrRemoveRptTransactions.class)))
                .thenReturn(validationErrors);

        when(validationErrors.isEmpty()).thenReturn(false);

        this.mockMvc.perform(post(ADD_OR_REMOVE_RPT_TRANSACTIONS_PATH + "?add")
                .param("rptTransactionToAdd.nameOfRelatedParty", "nameOfRelatedParty"))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_OR_REMOVE_RPT_TRANSACTIONS_VIEW));
    }

    @Test
    @DisplayName("Will render - false")
    void willRenderFalse() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(null);

        assertFalse(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Will render - true")
    void willRenderTrue() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(relatedPartyTransactionsApi);

        assertTrue(controller.willRender(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

}