package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.AccountingPoliciesResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.request.AccountingPoliciesCreate;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.request.AccountingPoliciesGet;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.request.AccountingPoliciesUpdate;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountingPoliciesServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private AccountingPoliciesResourceHandler accountingPoliciesResourceHandler;

    @Mock
    private AccountingPoliciesCreate accountingPoliciesCreate;

    @Mock
    private AccountingPoliciesUpdate accountingPoliciesUpdate;

    @Mock
    private AccountingPoliciesGet accountingPoliciesGet;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @InjectMocks
    private AccountingPoliciesService accountingPoliciesService = new AccountingPoliciesServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ACCOUNTING_POLICIES_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                    COMPANY_ACCOUNTS_ID + "/small-full/notes/accounting-policy";

    @BeforeEach
    void setUp() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.accountingPolicies()).thenReturn(accountingPoliciesResourceHandler);
    }

    @Test
    @DisplayName("Get accounting policies - success")
    void getAccountingPoliciesSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.get(ACCOUNTING_POLICIES_URI)).thenReturn(accountingPoliciesGet);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesGet.execute()).thenReturn(accountingPoliciesApi);

        AccountingPoliciesApi accountingPolicies = accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(accountingPolicies);
    }

    @Test
    @DisplayName("Get accounting policies - throws ApiErrorResponseException")
    void getAccountingPoliciesThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        when(accountingPoliciesResourceHandler.get(ACCOUNTING_POLICIES_URI)).thenReturn(accountingPoliciesGet);

        when(accountingPoliciesGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get accounting policies - resource not found")
    void getAccountingPoliciesResourceNotFound()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.get(ACCOUNTING_POLICIES_URI)).thenReturn(accountingPoliciesGet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(accountingPoliciesGet.execute()).thenThrow(apiErrorResponseException);

        assertNull(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get accounting policies - throws URIValidationException")
    void getAccountingPoliciesThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        when(accountingPoliciesResourceHandler.get(ACCOUNTING_POLICIES_URI)).thenReturn(accountingPoliciesGet);

        when(accountingPoliciesGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create accounting policies - success")
    void createAccountingPoliciesSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesCreate);
        when(accountingPoliciesCreate.execute()).thenReturn(accountingPoliciesApi);

        List<ValidationError> validationErrors =
                accountingPoliciesService.createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Create accounting policies - throws ApiErrorResponseException")
    void createAccountingPoliciesThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesCreate);
        when(accountingPoliciesCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi));
    }

    @Test
    @DisplayName("Create accounting policies - bad request")
    void createAccountingPoliciesBadRequest()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(accountingPoliciesCreate.execute()).thenThrow(apiErrorResponseException);

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(mockValidationErrors);
        when(mockValidationErrors.isEmpty()).thenReturn(false);

        List<ValidationError> validationErrors =
                accountingPoliciesService.createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Create accounting policies - bad request without validation errors")
    void createAccountingPoliciesBadRequestWithoutValidationErrors()
            throws ApiErrorResponseException, URIValidationException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(accountingPoliciesCreate.execute()).thenThrow(apiErrorResponseException);

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(mockValidationErrors);
        when(mockValidationErrors.isEmpty()).thenReturn(true);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi));
    }

    @Test
    @DisplayName("Create accounting policies - throws URIValidationException")
    void createAccountingPoliciesThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesCreate);
        when(accountingPoliciesCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi));
    }

    @Test
    @DisplayName("Update accounting policies - success")
    void updateAccountingPoliciesSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesUpdate);
        doNothing().when(accountingPoliciesUpdate).execute();

        List<ValidationError> validationErrors =
                accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Update accounting policies - throws ApiErrorResponseException")
    void updateAccountingPoliciesThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesUpdate);
        doThrow(ApiErrorResponseException.class).when(accountingPoliciesUpdate).execute();

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi));
    }

    @Test
    @DisplayName("Update accounting policies - bad request")
    void updateAccountingPoliciesBadRequest()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesUpdate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(accountingPoliciesUpdate.execute()).thenThrow(apiErrorResponseException);

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(mockValidationErrors);
        when(mockValidationErrors.isEmpty()).thenReturn(false);

        List<ValidationError> validationErrors =
                accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Update accounting policies - bad request without validation errors")
    void updateAccountingPoliciesBadRequestWithoutValidationErrors()
            throws ApiErrorResponseException, URIValidationException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesUpdate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(accountingPoliciesUpdate.execute()).thenThrow(apiErrorResponseException);

        when(validationContext.getValidationErrors(apiErrorResponseException)).thenReturn(mockValidationErrors);
        when(mockValidationErrors.isEmpty()).thenReturn(true);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi));
    }

    @Test
    @DisplayName("Update accounting policies - throws URIValidationException")
    void updateAccountingPoliciesThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesUpdate);
        doThrow(URIValidationException.class).when(accountingPoliciesUpdate).execute();

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi));
    }

}
