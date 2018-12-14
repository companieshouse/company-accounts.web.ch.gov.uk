package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.request.TransactionsCreate;
import uk.gov.companieshouse.api.handler.transaction.request.TransactionsGet;
import uk.gov.companieshouse.api.handler.transaction.request.TransactionsUpdate;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private TransactionsResourceHandler transactionsResourceHandler;

    @Mock
    private TransactionsCreate transactionsCreate;

    @Mock
    private TransactionsGet transactionsGet;

    @Mock
    private TransactionsUpdate transactionsUpdate;

    @InjectMocks
    private TransactionService transactionService = new TransactionServiceImpl();

    private static final String COMPANY_NUMBER = "123456";

    private static final String TRANSACTION_ID = "111-222-333";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String GET_TRANSACTION_URI = "/transactions/" + TRANSACTION_ID;

    private static final String UPDATE_TRANSACTION_URI = "/transactions/" + TRANSACTION_ID;

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
    }
    
    @Test
    @DisplayName("Create Transaction - Success Path")
    void createTransactionSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        Transaction createdTransaction = new Transaction();
        createdTransaction.setId(TRANSACTION_ID);

        when(transactionsResourceHandler.create(anyString(), any(Transaction.class)))
                .thenReturn(transactionsCreate);

        when(transactionsCreate.execute()).thenReturn(createdTransaction);

        String transactionId = transactionService.createTransaction(COMPANY_NUMBER);

        assertEquals(TRANSACTION_ID, transactionId);
    }

    @Test
    @DisplayName("Create Transaction - API Error Response Exception Thrown")
    void createTransactionApiResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(transactionsResourceHandler.create(anyString(), any(Transaction.class)))
                .thenReturn(transactionsCreate);

        when(transactionsCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () -> transactionService.createTransaction(COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Create Transaction - URIValidationException Thrown")
    void createTransactionURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(transactionsResourceHandler.create(anyString(), any(Transaction.class)))
                .thenReturn(transactionsCreate);

        when(transactionsCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () -> transactionService.createTransaction(COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Close Transaction - Success Path")
    void closeTransactionSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        when(transactionsResourceHandler.update(GET_TRANSACTION_URI, transaction))
                .thenReturn(transactionsUpdate);

        doNothing().when(transactionsUpdate).execute();

        transactionService.closeTransaction(TRANSACTION_ID);

        verify(transactionsResourceHandler, times(1))
                .update(GET_TRANSACTION_URI, transaction);
    }

    @Test
    @DisplayName("Close Transaction - API Error Response Exception Thrown on Get")
    void closeTransactionApiResponseExceptionThrownOnGet() throws ApiErrorResponseException, URIValidationException {

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () -> transactionService.closeTransaction(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Close Transaction - URIValidationException Thrown on Get")
    void closeTransactionURIValidationExceptionThrownOnGet() throws ApiErrorResponseException, URIValidationException {

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () -> transactionService.closeTransaction(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Close Transaction - API Error Response Exception Thrown on Update")
    void closeTransactionApiResponseExceptionThrownOnUpdate() throws ApiErrorResponseException, URIValidationException {

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        when(transactionsResourceHandler.update(GET_TRANSACTION_URI, transaction))
                .thenReturn(transactionsUpdate);

        doThrow(ApiErrorResponseException.class).when(transactionsUpdate).execute();

        assertThrows(ServiceException.class, () -> transactionService.closeTransaction(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Close Transaction - URIValidationException Thrown on Update")
    void closeTransactionURIValidationExceptionThrownOnUpdate() throws ApiErrorResponseException, URIValidationException {

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        when(transactionsResourceHandler.update(GET_TRANSACTION_URI, transaction))
                .thenReturn(transactionsUpdate);

        doThrow(URIValidationException.class).when(transactionsUpdate).execute();

        assertThrows(ServiceException.class, () -> transactionService.closeTransaction(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Create transaction resume link")
    void createTransactionResumeLink() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(transactionsResourceHandler.update(anyString(), any(Transaction.class)))
                .thenReturn(transactionsUpdate);

        doNothing().when(transactionsUpdate).execute();

        transactionService.createResumeLink(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionsResourceHandler).update(anyString(), transactionCaptor.capture());

        String expectedResumeJourneyUri = "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/small-full/resume";

        assertEquals(expectedResumeJourneyUri, transactionCaptor.getValue().getResumeJourneyUri());
    }
}
