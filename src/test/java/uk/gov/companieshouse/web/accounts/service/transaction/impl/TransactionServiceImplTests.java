package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.model.transaction.Transaction;
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

    @InjectMocks
    private TransactionService transactionService = new TransactionServiceImpl();

    private static final String COMPANY_NUMBER = "123456";

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transactions()).thenReturn(transactionsResourceHandler);
    }
    
    @Test
    @DisplayName("Create Transaction - Success Path")
    void createTransactionSuccess() throws ApiErrorResponseException {

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

        when(transactionsResourceHandler.create(any(Transaction.class))).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> transactionService.createTransaction(COMPANY_NUMBER));
    }

}
