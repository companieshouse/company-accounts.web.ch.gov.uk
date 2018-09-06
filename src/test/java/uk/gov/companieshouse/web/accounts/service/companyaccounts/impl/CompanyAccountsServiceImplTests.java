package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import uk.gov.companieshouse.api.handler.transaction.companyaccount.CompanyAccountResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.CompanyAccountsResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;

import javax.xml.ws.Service;

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

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CompanyAccountResourceHandler companyAccountResourceHandler;

    @InjectMocks
    private CompanyAccountsService companyAccountsService = new CompanyAccountsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);
    }

    @Test
    @DisplayName("Create Company Accounts - Success Path")
    void createCompanyAccountSuccess() throws ServiceException, ApiErrorResponseException {

        when(transactionResourceHandler.companyAccounts()).thenReturn(companyAccountsResourceHandler);

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
    @DisplayName("Create Company Accounts - Failure Path")
    void createCompanyAccountFailure() throws ServiceException, ApiErrorResponseException {

        when(transactionResourceHandler.companyAccounts()).thenReturn(companyAccountsResourceHandler);

        DateTime periodEndOn = new DateTime(new Date());

        when(companyAccountsResourceHandler.create(any(CompanyAccountsApi.class)))
                .thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                companyAccountsService.createCompanyAccounts(TRANSACTION_ID, periodEndOn));
    }

    @Test
    @DisplayName("Create Small Full Accounts - Success Path")
    void createSmallFullSuccess() throws ServiceException, ApiErrorResponseException {

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        SmallFullApi smallFullApi = new SmallFullApi();

        when(smallFullResourceHandler.create()).thenReturn(smallFullApi);

        companyAccountsService.createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(smallFullResourceHandler, times(1)).create();
    }

    @Test
    @DisplayName("Create Small Full Accounts - Failure Path")
    void createSmallFullFailure() throws ServiceException, ApiErrorResponseException {

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.create())
                .thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                companyAccountsService.createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }


}
