package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.api.client.util.DateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

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

        DateTime periodEndOn = new DateTime(new Date());

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();
        Map<String, String> links = new HashMap<>();
        links.put("self", "/company-accounts/" + COMPANY_ACCOUNTS_ID);

        companyAccounts.setLinks(links);

        when(companyAccountsResourceHandler.create(any(CompanyAccountsApi.class))).thenReturn(companyAccounts);

        String companyAccountsId = companyAccountsService.createCompanyAccounts(TRANSACTION_ID, periodEndOn);

        assertEquals(COMPANY_ACCOUNTS_ID, companyAccountsId);
    }

    @Test
    @DisplayName("Get Company Profile - Throws ApiErrorResponseException")
    void getBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException {

        DateTime periodEndOn = new DateTime(new Date());

        when(companyAccountsResourceHandler.create(any(CompanyAccountsApi.class)))
                .thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () ->
                companyAccountsService.createCompanyAccounts(TRANSACTION_ID, periodEndOn));
    }
}
