package uk.gov.companieshouse.web.accounts.service.smallfull.impl;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.RelatedPartyTransactionsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.rpttransactions.RptTransactionResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.rpttransactions.request.RptTransactionCreate;
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.rpttransactions.request.RptTransactionDelete;
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.rpttransactions.request.RptTransactionGetAll;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions.RptTransactionsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RptTransactionsServiceImplTest {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String RPT_TRANSACTION_ID = "rptTransactionsId";
    private static final String RPT_TRANSACTION_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/related-party-transactions/transactions";
    private static final String RPT_TRANSACTION_URI_WITH_ID = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/related-party-transactions/transactions/" + RPT_TRANSACTION_ID;

    private static final String RESOURCE_NAME = "transactions";

    @Mock
    private ApiClientService apiClientService;
    @Mock
    private ApiClient apiClient;

    @Mock
    private RptTransactionApi rptTransactionApi;

    @Mock
    private RptTransactionDelete rptTransactionDelete;
    @Mock
    private RptTransactionResourceHandler rptTransactionResourceHandler;
    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;
    @Mock
    private RelatedPartyTransactionsResourceHandler relatedPartyTransactionsResourceHandler;
    @Mock
    private ApiErrorResponseException apiErrorResponseException;
    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;
    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private ApiResponse<RptTransactionApi[]> responseWithMultipleDirectors;

    @Mock
    private ApiResponse<RptTransactionApi> responseWithSingleDirector;

    @Mock
    private RptTransactionsTransformer rptTransactionsTransformer;

    @Mock
    private RptTransactionToAdd rptTransactionToAdd;

    @Mock
    private RptTransactionGetAll rptTransactionGetAll;

    @Mock
    private RptTransactionCreate rptTransactionCreate;

    @InjectMocks
    private RptTransactionsServiceImpl rptTransactionsService;

    @Test
    @DisplayName("GET - all RPT transactions - success")
    void getAllRptTransactionsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenReturn(responseWithMultipleDirectors);
        RptTransactionApi[] rptTransactionApi = new RptTransactionApi[1];
        when(responseWithMultipleDirectors.getData()).thenReturn(rptTransactionApi);
        RptTransaction[] allRptTransactions = new RptTransaction[1];
        when(rptTransactionsTransformer.getAllRptTransactions(rptTransactionApi)).thenReturn(allRptTransactions);

        RptTransaction[] response = rptTransactionsService.getAllRptTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(allRptTransactions, response);
    }

    @Test
    @DisplayName("DELETE - RPT transaction - success")
    void deleteRptTransactionSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.delete(RPT_TRANSACTION_URI_WITH_ID)).thenReturn(rptTransactionDelete);
        rptTransactionsService.deleteRptTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, RPT_TRANSACTION_ID);
        verify(rptTransactionDelete).execute();
    }

    @Test
    @DisplayName("GET - all RPT transactions - not found")
    void getAllRptTransactionNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        RptTransaction[] response = rptTransactionsService.getAllRptTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(response);
        assertEquals(0, response.length);
    }

    @Test
    @DisplayName("GET - all RPT transaction - ApiErrorResponseException")
    void getAllRptTransactionsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> rptTransactionsService.getAllRptTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("GET - all RPT transactions - URIValidationException")
    void getAllRptTransactionsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> rptTransactionsService.getAllRptTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - RPT transaction - success")
    void createRptTransactionSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI, rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenReturn(responseWithSingleDirector);

        List<ValidationError> validationErrors = rptTransactionsService.createRptTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, rptTransactionToAdd);

        assertNotNull(validationErrors);
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - Rpt transaction - ApiErrorResponseException")
    void createRptTransactionApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI, rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> rptTransactionsService.createRptTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, rptTransactionToAdd));
    }

    @Test
    @DisplayName("POST - Rpt transaction - URIValidationException")
    void createRptTransactionURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI, rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> rptTransactionsService.createRptTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, rptTransactionToAdd));
    }

    @Test
    @DisplayName("DELETE - Rpt transaction - ApiErrorResponseException")
    void deleteRptTransactionApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.delete(RPT_TRANSACTION_URI_WITH_ID)).thenReturn(rptTransactionDelete);
        when(rptTransactionDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleDeletionException(apiErrorResponseException, RESOURCE_NAME);
        assertThrows(ServiceException.class, () -> rptTransactionsService.deleteRptTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, RPT_TRANSACTION_ID));
    }

    @Test
    @DisplayName("DELETE - Rpt transaction - URIValidationException")
    void deleteRptTransactionURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.delete(RPT_TRANSACTION_URI_WITH_ID)).thenReturn(rptTransactionDelete);
        when(rptTransactionDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> rptTransactionsService.deleteRptTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, RPT_TRANSACTION_ID));
    }
}