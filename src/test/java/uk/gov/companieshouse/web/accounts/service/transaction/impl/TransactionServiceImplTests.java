package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.transaction.Resource;
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

    @Mock
    private ApiResponse<Transaction> responseWithData;

    @Mock
    private ApiResponse<Void> responseWithoutData;

    @Mock
    private Map<String, Object> headers;

    @InjectMocks
    private TransactionService transactionService = new TransactionServiceImpl();

    private static final String COMPANY_NUMBER = "123456";

    private static final String TRANSACTION_ID = "111-222-333";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String GET_TRANSACTION_URI = "/transactions/" + TRANSACTION_ID;

    private static final String PAYMENT_REQUIRED_HEADER = "x-payment-required";

    private static final String PAYMENT_URL = "paymentUrl";

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

        when(transactionsCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(createdTransaction);

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
    @DisplayName("Close Transaction - Success Path for payable transaction")
    void closeTransactionSuccessForPayableTransaction() throws ServiceException, ApiErrorResponseException, URIValidationException {

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        when(transactionsResourceHandler.update(GET_TRANSACTION_URI, transaction))
                .thenReturn(transactionsUpdate);

        when(transactionsUpdate.execute()).thenReturn(responseWithoutData);

        when(responseWithoutData.getHeaders()).thenReturn(headers);

        List<String> paymentRequiredHeader = new ArrayList<>();
        paymentRequiredHeader.add(PAYMENT_URL);

        when(headers.get(PAYMENT_REQUIRED_HEADER)).thenReturn(paymentRequiredHeader);

        Optional<String> paymentUrl = transactionService.closeTransaction(TRANSACTION_ID);

        assertTrue(paymentUrl.isPresent());
        assertEquals(PAYMENT_URL, paymentUrl.get());
    }

    @Test
    @DisplayName("Close Transaction - Success Path for non payable transaction")
    void closeTransactionSuccessForNonPayableTransaction() throws ServiceException, ApiErrorResponseException, URIValidationException {

        Transaction transaction = new Transaction();
        transaction.setId(TRANSACTION_ID);

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        when(transactionsResourceHandler.update(GET_TRANSACTION_URI, transaction))
                .thenReturn(transactionsUpdate);

        when(transactionsUpdate.execute()).thenReturn(responseWithoutData);

        when(responseWithoutData.getHeaders()).thenReturn(headers);

        when(headers.get(PAYMENT_REQUIRED_HEADER)).thenReturn(null);

        Optional<String> paymentUrl = transactionService.closeTransaction(TRANSACTION_ID);

        assertFalse(paymentUrl.isPresent());
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

        when(transactionsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(transaction);

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

        when(transactionsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(transaction);

        transaction.setStatus(TransactionStatus.CLOSED);

        when(transactionsResourceHandler.update(GET_TRANSACTION_URI, transaction))
                .thenReturn(transactionsUpdate);

        doThrow(URIValidationException.class).when(transactionsUpdate).execute();

        assertThrows(ServiceException.class, () -> transactionService.closeTransaction(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Create transaction resume link")
    void createTransactionResumeLink() throws ServiceException {

        when(transactionsResourceHandler.update(anyString(), any(Transaction.class)))
                .thenReturn(transactionsUpdate);

        transactionService.createResumeLink(COMPANY_NUMBER, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionsResourceHandler).update(anyString(), transactionCaptor.capture());

        String expectedResumeJourneyUri = "/company/" + COMPANY_NUMBER +
                "/transaction/" + TRANSACTION_ID +
                "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                "/resume";

        assertEquals(expectedResumeJourneyUri, transactionCaptor.getValue().getResumeJourneyUri());
    }

    @Test
    @DisplayName("Is payable transaction returns true")
    void isPayableTransactionReturnsTrue()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        Map<String, String> links = new HashMap<>();
        links.put("costs", "tests");

        Resource resource = new Resource();
        resource.setLinks(links);

        Map<String, Resource> resources = new HashMap<>();
        resources.put("/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID, resource);

        Transaction transaction = new Transaction();
        transaction.setResources(resources);

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(transaction);

        assertTrue(transactionService.isPayableTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Is payable transaction returns false")
    void isPayableTransactionReturnsFalse()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        Resource resource = new Resource();
        resource.setLinks(new HashMap<>());

        Map<String, Resource> resources = new HashMap<>();
        resources.put("/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID, resource);

        Transaction transaction = new Transaction();
        transaction.setResources(resources);

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(transaction);

        assertFalse(transactionService.isPayableTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Is payable transaction throws ApiErrorResponseException")
    void isPayableTransactionThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class,
                () -> transactionService.isPayableTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Is payable transaction throws URIValidationException")
    void isPayableTransactionThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        when(transactionsResourceHandler.get(GET_TRANSACTION_URI)).thenReturn(transactionsGet);

        when(transactionsGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class,
                () -> transactionService.isPayableTransaction(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
