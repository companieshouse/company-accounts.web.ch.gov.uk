package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
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
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.AccountingPoliciesResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.request.AccountingPoliciesCreate;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.request.AccountingPoliciesGet;
import uk.gov.companieshouse.api.handler.smallfull.accountingpolicies.request.AccountingPoliciesUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountingPoliciesServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

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
    private ApiResponse<AccountingPoliciesApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private AccountingPoliciesApi accountingPolicies;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private AccountingPoliciesService accountingPoliciesService = new AccountingPoliciesServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ACCOUNTING_POLICIES_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                    COMPANY_ACCOUNTS_ID + "/small-full/notes/accounting-policy";

    private static final String RESOURCE_NAME = "accounting policies";

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

        when(accountingPoliciesGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(accountingPolicies);

        AccountingPoliciesApi returnedAccountingPolicies = accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(accountingPolicies, returnedAccountingPolicies);
    }

    @Test
    @DisplayName("Get accounting policies - throws ApiErrorResponseException")
    void getAccountingPoliciesThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.get(ACCOUNTING_POLICIES_URI)).thenReturn(accountingPoliciesGet);

        when(accountingPoliciesGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get accounting policies - resource not found")
    void getAccountingPoliciesResourceNotFound()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.get(ACCOUNTING_POLICIES_URI)).thenReturn(accountingPoliciesGet);

        when(accountingPoliciesGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertNull(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get accounting policies - throws URIValidationException")
    void getAccountingPoliciesThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.get(ACCOUNTING_POLICIES_URI)).thenReturn(accountingPoliciesGet);

        when(accountingPoliciesGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create accounting policies - success")
    void createAccountingPoliciesSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPolicies))
                .thenReturn(accountingPoliciesCreate);

        when(accountingPoliciesCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                accountingPoliciesService.createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Create accounting policies - throws ApiErrorResponseException")
    void createAccountingPoliciesThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPolicies))
                .thenReturn(accountingPoliciesCreate);

        when(accountingPoliciesCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.createAccountingPoliciesApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies));
    }

    @Test
    @DisplayName("Create accounting policies - validation errors")
    void createAccountingPoliciesValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPolicies))
                .thenReturn(accountingPoliciesCreate);

        when(accountingPoliciesCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                accountingPoliciesService.createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Create accounting policies - throws URIValidationException")
    void createAccountingPoliciesThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.create(ACCOUNTING_POLICIES_URI, accountingPolicies))
                .thenReturn(accountingPoliciesCreate);

        when(accountingPoliciesCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.createAccountingPoliciesApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies));
    }

    @Test
    @DisplayName("Update accounting policies - success")
    void updateAccountingPoliciesSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPoliciesApi))
                .thenReturn(accountingPoliciesUpdate);

        when(accountingPoliciesUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Update accounting policies - throws ApiErrorResponseException")
    void updateAccountingPoliciesThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPolicies))
                .thenReturn(accountingPoliciesUpdate);

        when(accountingPoliciesUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.updateAccountingPoliciesApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies));
    }

    @Test
    @DisplayName("Update accounting policies - validation errors")
    void updateAccountingPoliciesValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPolicies))
                .thenReturn(accountingPoliciesUpdate);

        when(accountingPoliciesUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                accountingPoliciesService.updateAccountingPoliciesApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Update accounting policies - throws URIValidationException")
    void updateAccountingPoliciesThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(accountingPoliciesResourceHandler.update(ACCOUNTING_POLICIES_URI, accountingPolicies))
                .thenReturn(accountingPoliciesUpdate);

        when(accountingPoliciesUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                accountingPoliciesService.updateAccountingPoliciesApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPolicies));
    }

}
