package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import uk.gov.companieshouse.api.handler.transaction.companyaccount.CompanyAccountsResourceHandler;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyAccountsServiceImplTests {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private TransactionResourceHandler transactionResourceHandler;

    @Mock
    private CompanyAccountsResourceHandler companyAccountsResourceHandler;

    @InjectMocks
    private CompanyAccountsService companyAccountsService = new CompanyAccountsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.companyAccounts())
                .thenReturn(companyAccountsResourceHandler);
    }

    @Test
    @DisplayName("Create Company Accounts - Success Path")
    void createCompanyAccountSuccess() throws ApiErrorResponseException {

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();

        when(companyAccountsResourceHandler.create(companyAccounts)).thenReturn(companyAccounts);

        CompanyAccountsApi createdCompanyAccounts =
                companyAccountsService.createCompanyAccounts(TRANSACTION_ID, companyAccounts);

        assertNotNull(createdCompanyAccounts);
    }

    @Test
    @DisplayName("Get Company Profile - Throws ApiErrorResponseException")
    void getBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException {

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();

        when(companyAccountsResourceHandler.create(companyAccounts))
                .thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () ->
                companyAccountsService.createCompanyAccounts(TRANSACTION_ID, companyAccounts));
    }
}
