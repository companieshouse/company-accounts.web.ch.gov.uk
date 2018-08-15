package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.transaction.TransactionResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
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
    private TransactionResourceHandler transactionResourceHandler;

    @InjectMocks
    private TransactionService transactionService = new TransactionServiceImpl();

    private static final String COMPANY_NUMBER = "123456";

    private static final String TRANSACTION_ID = "111-222-333";

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
    }
    
    @Test
    @DisplayName("Create Transaction - Success Path")
    void createTransactionSuccess() throws ApiErrorResponseException {

        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);

        Transaction createdTransaction = new Transaction();
        createdTransaction.setCompanyNumber(COMPANY_NUMBER);

        when(transactionsResourceHandler.create(any(Transaction.class))).thenReturn(createdTransaction);

        Transaction transaction = transactionService.createTransaction(COMPANY_NUMBER);

        assertNotNull(transaction);
        assertEquals(COMPANY_NUMBER, transaction.getCompanyNumber());
    }

    @Test
    @DisplayName("Create Transaction - API Error Response Exception Thrown")
    void createTransactionApiResponseExceptionThrown() throws ApiErrorResponseException {

        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);

        when(transactionsResourceHandler.create(any(Transaction.class))).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> transactionService.createTransaction(COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Close Transaction - Success Path")
    void closeTransactionSuccess() throws ApiErrorResponseException {

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(transactionResourceHandler.get()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        doNothing().when(transactionResourceHandler).update(transaction);

        transactionService.closeTransaction(TRANSACTION_ID);

        verify(transactionResourceHandler, times(1)).update(transaction);
    }

    @Test
    @DisplayName("Close Transaction - API Error Response Exception Thrown on Get")
    void closeTransactionApiResponseExceptionThrownOnGet() throws ApiErrorResponseException {

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.get()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> transactionService.closeTransaction(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Close Transaction - API Error Response Exception Thrown on Update")
    void closeTransactionApiResponseExceptionThrownOnUpdate() throws ApiErrorResponseException {

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(transactionResourceHandler.get()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        doThrow(ApiErrorResponseException.class).when(transactionResourceHandler).update(transaction);

        assertThrows(ApiErrorResponseException.class, () -> transactionService.closeTransaction(TRANSACTION_ID));
    }
}
