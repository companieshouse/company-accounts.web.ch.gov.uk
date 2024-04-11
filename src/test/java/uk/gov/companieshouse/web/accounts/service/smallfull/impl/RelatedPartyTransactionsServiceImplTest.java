package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.request.RelatedPartyTransactionsCreate;
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.request.RelatedPartyTransactionsDelete;
import uk.gov.companieshouse.api.handler.smallfull.relatedpartytransactions.request.RelatedPartyTransactionsGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RelatedPartyTransactionsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.RelatedPartyTransactionsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RelatedPartyTransactionsServiceImplTest {
    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private RelatedPartyTransactionsApi relatedPartyTransactionsApi;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ApiResponse<RelatedPartyTransactionsApi> responseWithData;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private RelatedPartyTransactionsResourceHandler relatedPartyTransactionsResourceHandler;

    @Mock
    private RelatedPartyTransactionsGet relatedPartyTransactionsGet;

    @Mock
    private RelatedPartyTransactionsCreate relatedPartyTransactionsCreate;

    @Mock
    private RelatedPartyTransactionsDelete relatedPartyTransactionsDelete;

    @InjectMocks
    private final RelatedPartyTransactionsService relatedPartyTransactionsService = new RelatedPartyTransactionsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String RELATED_PARTY_TRANSACTIONS_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/related-party-transactions";

    private static final String SMALL_FULL_DIRECTORS_LINK = "smallFullDirectorsLink";

    private static final String RESOURCE_NAME = "related party transactions";

    @Test
    @DisplayName("GET - related party transactions - success")
    void getRelatedPartyTransactionsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.get(RELATED_PARTY_TRANSACTIONS_URI)).thenReturn(relatedPartyTransactionsGet);
        when(relatedPartyTransactionsGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(relatedPartyTransactionsApi);

        RelatedPartyTransactionsApi response = relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(relatedPartyTransactionsApi, response);
    }

    @Test
    @DisplayName("GET - related party transactions - not found")
    void getRelatedPartyTransactionsNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.get(RELATED_PARTY_TRANSACTIONS_URI)).thenReturn(relatedPartyTransactionsGet);
        when(relatedPartyTransactionsGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        RelatedPartyTransactionsApi response = relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNull(response);
    }

    @Test
    @DisplayName("GET - related party transactions - ApiErrorResponseException")
    void getRelatedPartyTransactionsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.get(RELATED_PARTY_TRANSACTIONS_URI)).thenReturn(relatedPartyTransactionsGet);
        when(relatedPartyTransactionsGet.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("GET - related party transactions - URIValidationException")
    void getRelatedPartyTransactionsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.get(RELATED_PARTY_TRANSACTIONS_URI)).thenReturn(relatedPartyTransactionsGet);
        when(relatedPartyTransactionsGet.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                relatedPartyTransactionsService.getRelatedPartyTransactions(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - related party transactions - success")
    void createRelatedPartyTransactionsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.create(eq(RELATED_PARTY_TRANSACTIONS_URI), any(RelatedPartyTransactionsApi.class))).thenReturn(relatedPartyTransactionsCreate);

        assertAll(() -> relatedPartyTransactionsService.createRelatedPartyTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(relatedPartyTransactionsCreate).execute();
    }

    @Test
    @DisplayName("POST - related party transactions - ApiErrorResponseException")
    void createRelatedPartyTransactionsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.create(eq(RELATED_PARTY_TRANSACTIONS_URI), any(RelatedPartyTransactionsApi.class))).thenReturn(relatedPartyTransactionsCreate);
        when(relatedPartyTransactionsCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> relatedPartyTransactionsService.createRelatedPartyTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - related party transactions - URIValidationException")
    void createRelatedPartyTransactionsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.create(eq(RELATED_PARTY_TRANSACTIONS_URI), any(RelatedPartyTransactionsApi.class))).thenReturn(relatedPartyTransactionsCreate);
        when(relatedPartyTransactionsCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> relatedPartyTransactionsService.createRelatedPartyTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - related party transactions - success")
    void deleteRelatedPartyTransactionsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getRelatedPartyTransactions()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.delete(RELATED_PARTY_TRANSACTIONS_URI)).thenReturn(relatedPartyTransactionsDelete);

        assertAll(() -> relatedPartyTransactionsService.deleteRelatedPartyTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(relatedPartyTransactionsDelete).execute();
    }

    @Test
    @DisplayName("DELETE - related party transactions - no small full link")
    void deleteRelatedPartyTransactionsNoSmallFullLink() throws ServiceException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getRelatedPartyTransactions()).thenReturn(null);

        assertAll(() -> relatedPartyTransactionsService.deleteRelatedPartyTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(apiClient, never()).smallFull();
    }

    @Test
    @DisplayName("DELETE - related party transactions - ApiErrorResponseException")
    void deleteRelatedPartyTransactionsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getRelatedPartyTransactions()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.delete(RELATED_PARTY_TRANSACTIONS_URI)).thenReturn(relatedPartyTransactionsDelete);
        when(relatedPartyTransactionsDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> relatedPartyTransactionsService.deleteRelatedPartyTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - related party transactions - URIValidationException")
    void deleteRelatedPartyTransactionsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getRelatedPartyTransactions()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.relatedPartyTransactions()).thenReturn(relatedPartyTransactionsResourceHandler);
        when(relatedPartyTransactionsResourceHandler.delete(RELATED_PARTY_TRANSACTIONS_URI)).thenReturn(relatedPartyTransactionsDelete);
        when(relatedPartyTransactionsDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> relatedPartyTransactionsService.deleteRelatedPartyTransactions(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
