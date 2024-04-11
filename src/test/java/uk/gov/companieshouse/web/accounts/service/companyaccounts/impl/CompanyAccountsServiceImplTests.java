package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import uk.gov.companieshouse.api.handler.companyaccount.CompanyAccountsResourceHandler;
import uk.gov.companieshouse.api.handler.companyaccount.request.CompanyAccountsCreate;
import uk.gov.companieshouse.api.handler.companyaccount.request.CompanyAccountsGet;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CompanyAccountsServiceImplTests {
    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private CompanyAccountsResourceHandler companyAccountsResourceHandler;

    @Mock
    private CompanyAccountsCreate companyAccountsCreate;
    
    @Mock
    private CompanyAccountsGet companyAccountsGet;

    @Mock
    private CompanyAccountsApi companyAccountsApi;

    @Mock
    private ApiResponse<CompanyAccountsApi> responseWithData;

    @InjectMocks
    private CompanyAccountsService companyAccountsService = new CompanyAccountsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @BeforeEach
    private void init() {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
    }

    @Test
    @DisplayName("Create Company Accounts - Success Path")
    void createCompanyAccountSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClient.companyAccounts()).thenReturn(companyAccountsResourceHandler);

        CompanyAccountsLinks companyAccountsLinks = new CompanyAccountsLinks();
        companyAccountsLinks.setSelf("/company-accounts/" + COMPANY_ACCOUNTS_ID);

        CompanyAccountsApi companyAccounts = new CompanyAccountsApi();
        companyAccounts.setLinks(companyAccountsLinks);

        when(companyAccountsResourceHandler.create(anyString(), any(CompanyAccountsApi.class)))
                .thenReturn(companyAccountsCreate);

        when(companyAccountsCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(companyAccounts);

        String companyAccountsId = companyAccountsService.createCompanyAccounts(TRANSACTION_ID);

        assertEquals(COMPANY_ACCOUNTS_ID, companyAccountsId);
    }

    @Test
    @DisplayName("Create Company Accounts - Throws ApiErrorResponseException")
    void createCompanyAccountsApiErrorResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {
        when(apiClient.companyAccounts()).thenReturn(companyAccountsResourceHandler);

        when(companyAccountsResourceHandler.create(anyString(), any(CompanyAccountsApi.class)))
                .thenReturn(companyAccountsCreate);

        when(companyAccountsCreate.execute())
                .thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                companyAccountsService.createCompanyAccounts(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Create Company Accounts - Throws URIValidationException")
    void createCompanyAccountsURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {
        when(apiClient.companyAccounts()).thenReturn(companyAccountsResourceHandler);

        when(companyAccountsResourceHandler.create(anyString(), any(CompanyAccountsApi.class)))
                .thenReturn(companyAccountsCreate);

        when(companyAccountsCreate.execute())
                .thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                companyAccountsService.createCompanyAccounts(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Get company accounts - success")
    void getCompanyAccountsSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {
        when(apiClient.companyAccounts()).thenReturn(companyAccountsResourceHandler);

        when(companyAccountsResourceHandler.get(anyString())).thenReturn(companyAccountsGet);
        when(companyAccountsGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(companyAccountsApi);

        CompanyAccountsApi returnedCompanyAccounts =
                companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returnedCompanyAccounts);
        assertEquals(companyAccountsApi, returnedCompanyAccounts);
    }

    @Test
    @DisplayName("Get company accounts - api error response exception")
    void getCompanyAccountsApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {
        when(apiClient.companyAccounts()).thenReturn(companyAccountsResourceHandler);

        when(companyAccountsResourceHandler.get(anyString())).thenReturn(companyAccountsGet);
        when(companyAccountsGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class,
                () -> companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get company accounts - uri validation exception")
    void getCompanyAccountsURIValidationException() throws ApiErrorResponseException, URIValidationException {
        when(apiClient.companyAccounts()).thenReturn(companyAccountsResourceHandler);

        when(companyAccountsResourceHandler.get(anyString())).thenReturn(companyAccountsGet);
        when(companyAccountsGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class,
                () -> companyAccountsService.getCompanyAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
