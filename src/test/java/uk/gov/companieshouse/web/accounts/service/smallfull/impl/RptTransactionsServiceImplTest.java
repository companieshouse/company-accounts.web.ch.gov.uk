package uk.gov.companieshouse.web.accounts.service.smallfull.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.AddOrRemoveRptTransactions;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions.RptTransactionsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;
import uk.gov.companieshouse.web.accounts.validation.smallfull.RptTransactionValidator;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RptTransactionsServiceImplTest {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String RPT_TRANSACTION_ID = "rptTransactionsId";
    private static final String RPT_TRANSACTION_URI =
            "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                    COMPANY_ACCOUNTS_ID
                    + "/small-full/notes/related-party-transactions/transactions";
    private static final String RPT_TRANSACTION_URI_WITH_ID =
            "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                    COMPANY_ACCOUNTS_ID
                    + "/small-full/notes/related-party-transactions/transactions/"
                    + RPT_TRANSACTION_ID;

    private static final String RESOURCE_NAME = "transactions";

    @Mock
    private ApiClientService apiClientService;
    @Mock
    private ApiClient apiClient;

    @Mock
    private AddOrRemoveRptTransactions addOrRemoveRptTransactions;

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
    private ApiResponse<RptTransactionApi[]> responseWithMultipleRptTransactions;

    @Mock
    private ApiResponse<RptTransactionApi> responseWithSingleRptTransaction;

    @Mock
    private RptTransactionsTransformer rptTransactionsTransformer;

    @Mock
    private RptTransactionToAdd rptTransactionToAdd;

    @Mock
    private RptTransactionGetAll rptTransactionGetAll;

    @Mock
    private RptTransactionCreate rptTransactionCreate;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private RptTransactionValidator rptTransactionValidator;

    @InjectMocks
    private RptTransactionsServiceImpl rptTransactionsService;

    @Test
    @DisplayName("GET - all RPT transactions - success")
    void getAllRptTransactionsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(
                rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenReturn(responseWithMultipleRptTransactions);
        RptTransactionApi[] rptTransactionApi = new RptTransactionApi[1];
        rptTransactionApi[0] = new RptTransactionApi();
        when(responseWithMultipleRptTransactions.getData()).thenReturn(rptTransactionApi);
        RptTransaction[] allRptTransactions = new RptTransaction[1];
        when(rptTransactionsTransformer.getAllRptTransactions(rptTransactionApi)).thenReturn(
                allRptTransactions);

        RptTransaction[] response = rptTransactionsService.getAllRptTransactions(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID);

        assertEquals(allRptTransactions, response);
    }


    @Test
    @DisplayName("GET - all RPT transactions some with blank name - success")
    void getAllRptTransactionsSomeBlankNameSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(
                rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenReturn(responseWithMultipleRptTransactions);
        RptTransactionApi[] rptTransactionApi = new RptTransactionApi[1];
        RptTransactionApi rptTransaction = new RptTransactionApi();
        rptTransaction.setNameOfRelatedParty("");
        rptTransactionApi[0] = rptTransaction;
        when(responseWithMultipleRptTransactions.getData()).thenReturn(rptTransactionApi);
        RptTransaction[] allRptTransactions = new RptTransaction[1];
        RptTransaction transaction = new RptTransaction();
        transaction.setNameOfRelatedParty("Not provided");
        allRptTransactions[0] = transaction;
        when(rptTransactionsTransformer.getAllRptTransactions(rptTransactionApi)).thenReturn(
                allRptTransactions);

        RptTransaction[] response = rptTransactionsService.getAllRptTransactions(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID);

        assertEquals(allRptTransactions, response);
        assertEquals("Not provided", response[0].getNameOfRelatedParty());
    }

    @Test
    @DisplayName("DELETE - RPT transaction - success")
    void deleteRptTransactionSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.delete(RPT_TRANSACTION_URI_WITH_ID)).thenReturn(
                rptTransactionDelete);
        rptTransactionsService.deleteRptTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                RPT_TRANSACTION_ID);
        verify(rptTransactionDelete).execute();
    }

    @Test
    @DisplayName("GET - all RPT transactions - not found")
    void getAllRptTransactionNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(
                rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        RptTransaction[] response = rptTransactionsService.getAllRptTransactions(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID);

        assertNotNull(response);
        assertEquals(0, response.length);
    }

    @Test
    @DisplayName("GET - all RPT transaction - ApiErrorResponseException")
    void getAllRptTransactionsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(
                rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> rptTransactionsService.getAllRptTransactions(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("GET - all RPT transactions - URIValidationException")
    void getAllRptTransactionsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.getAll(RPT_TRANSACTION_URI)).thenReturn(
                rptTransactionGetAll);
        when(rptTransactionGetAll.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> rptTransactionsService.getAllRptTransactions(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - RPT transaction - success")
    void createRptTransactionSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(
                rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI,
                rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenReturn(responseWithSingleRptTransaction);

        List<ValidationError> validationErrors = rptTransactionsService.createRptTransaction(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveRptTransactions);

        assertNotNull(validationErrors);
        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - Rpt transaction - validation failed for fields")
    void createRptTransactionThrowsValidationError()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        ValidationError validationError = new ValidationError();
        List<ValidationError> nameValidationError = new ArrayList<>();
        nameValidationError.add(validationError);

        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(rptTransactionValidator.validateRptTransactionToAdd(rptTransactionToAdd)).thenReturn(
                nameValidationError);

        List<ValidationError> validationErrors = rptTransactionsService.createRptTransaction(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveRptTransactions);

        assertEquals(nameValidationError, validationErrors);

        verify(apiClientService, never()).getApiClient();

    }

    @Test
    @DisplayName("POST - RPT transaction - api response error")
    void createRptTransactionApiResponseError()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(
                rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI,
                rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenReturn(responseWithSingleRptTransaction);
        when(responseWithSingleRptTransaction.hasErrors()).thenReturn(true);

        ValidationError validationError = new ValidationError();
        List<ValidationError> apiValidationErrors = new ArrayList<>();
        apiValidationErrors.add(validationError);
        when(validationContext.getValidationErrors(
                responseWithSingleRptTransaction.getErrors())).thenReturn(apiValidationErrors);

        List<ValidationError> validationErrors = rptTransactionsService.createRptTransaction(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveRptTransactions);

        assertEquals(apiValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("POST - Rpt transaction - ApiErrorResponseException")
    void createRptTransactionApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(
                rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI,
                rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> rptTransactionsService.createRptTransaction(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID, addOrRemoveRptTransactions));
    }

    @Test
    @DisplayName("POST - Rpt transaction - URIValidationException")
    void createRptTransactionURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(
                rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI,
                rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> rptTransactionsService.createRptTransaction(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID, addOrRemoveRptTransactions));
    }

    @Test
    @DisplayName("DELETE - Rpt transaction - ApiErrorResponseException")
    void deleteRptTransactionApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.delete(RPT_TRANSACTION_URI_WITH_ID)).thenReturn(
                rptTransactionDelete);
        when(rptTransactionDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);
        assertThrows(ServiceException.class,
                () -> rptTransactionsService.deleteRptTransaction(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID, RPT_TRANSACTION_ID));
    }

    @Test
    @DisplayName("DELETE - Rpt transaction - URIValidationException")
    void deleteRptTransactionURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.delete(RPT_TRANSACTION_URI_WITH_ID)).thenReturn(
                rptTransactionDelete);
        when(rptTransactionDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> rptTransactionsService.deleteRptTransaction(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID, RPT_TRANSACTION_ID));
    }

    @Test
    @DisplayName("POST - submit Rpt transaction - resource has validation errors")
    void submitAddOrRemoveRptTransactionValidationError()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        ValidationError validationError = new ValidationError();
        List<ValidationError> nameValidationError = new ArrayList<>();
        nameValidationError.add(validationError);

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(
                rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI,
                rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenReturn(responseWithSingleRptTransaction);
        when(responseWithSingleRptTransaction.hasErrors()).thenReturn(true);

        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(validationContext.getValidationErrors(
                responseWithSingleRptTransaction.getErrors())).thenReturn(nameValidationError);

        List<ValidationError> validationErrors = rptTransactionsService.submitAddOrRemoveRptTransactions(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveRptTransactions);

        assertEquals(nameValidationError, validationErrors);
    }

    @Test
    @DisplayName("POST - submit RPT transaction - successful with no validation errors")
    void submitAddOrRemoveRptTransactionSuccessfulForMultiYearFilerNonEmptyResource()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(addOrRemoveRptTransactions.getRptTransactionToAdd()).thenReturn(rptTransactionToAdd);

        when(rptTransactionsTransformer.getRptTransactionsApi(rptTransactionToAdd)).thenReturn(
                rptTransactionApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(
                relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.rptTransactions()).thenReturn(
                rptTransactionResourceHandler);
        when(rptTransactionResourceHandler.create(RPT_TRANSACTION_URI,
                rptTransactionApi)).thenReturn(rptTransactionCreate);
        when(rptTransactionCreate.execute()).thenReturn(responseWithSingleRptTransaction);

        List<ValidationError> validationErrors = rptTransactionsService.submitAddOrRemoveRptTransactions(
                TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveRptTransactions);

        assertNotNull(validationErrors);
        assertTrue(validationErrors.isEmpty());
    }
}