package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullCreate;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullGet;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SmallFullServiceImplTests {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private SmallFullCreate smallFullCreate;

    @Mock
    private SmallFullUpdate smallFullUpdate;

    @Mock
    private SmallFullGet smallFullGet;

    @Mock
    private ApiResponse<SmallFullApi> responseWithData;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private AccountingPeriodApi lastAccounts;

    @Mock
    private AccountingPeriodApi nextAccounts;

    @Mock
    private AccountsDatesHelper accountsDatesHelper;

    @InjectMocks
    private SmallFullService smallFullService = new SmallFullServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final LocalDate LAST_ACCOUNTS_PERIOD_START = LocalDate.of(2018, 1, 1);

    private static final LocalDate LAST_ACCOUNTS_PERIOD_END = LocalDate.of(2018, 12, 31);

    private static final LocalDate NEXT_ACCOUNTS_PERIOD_START = LocalDate.of(2019, 1, 1);

    private static final LocalDate NEXT_ACCOUNTS_PERIOD_END = LocalDate.of(2019, 12, 31);

    private static final String LAST_ACCOUNTS_HEADING = "2018";

    private static final String NEXT_ACCOUNTS_HEADING = "2019";


    @Test
    @DisplayName("Create Small Full Accounts - Success Path")
    void createSmallFullSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullResourceHandler.create(anyString(), any(SmallFullApi.class)))
                .thenReturn(smallFullCreate);

        smallFullService.createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(smallFullCreate, times(1)).execute();
    }

    @Test
    @DisplayName("Create Small Full Accounts - Throws ApiErrorResponseException")
    void createSmallFullApiErrorResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullResourceHandler.create(anyString(), any(SmallFullApi.class)))
                .thenReturn(smallFullCreate);

        when(smallFullCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                smallFullService.createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create Small Full Accounts - Throws URIValidationException")
    void createSmallFullURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullResourceHandler.create(anyString(), any(SmallFullApi.class)))
                .thenReturn(smallFullCreate);

        when(smallFullCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                smallFullService.createSmallFullAccounts(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Small Full Accounts - Success")
    void getSmallFullAccountsSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.get(anyString())).thenReturn(smallFullGet);

        when(smallFullGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(new SmallFullApi());

        assertNotNull(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(smallFullGet, times(1)).execute();
    }

    @Test
    @DisplayName("Get Small Full Accounts - Throws ApiErrorResponseException")
    void getSmallFullApiErrorResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.get(anyString())).thenReturn(smallFullGet);

        when(smallFullGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Small Full Accounts - Throws URIValidationException")
    void getSmallFullURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.get(anyString())).thenReturn(smallFullGet);

        when(smallFullGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Update Small Full Accounts - Success Path")
    void updateSmallFullSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        LocalDate newPeriodEndOn = LocalDate.now();

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullResourceHandler.update(anyString(), any(SmallFullApi.class)))
                .thenReturn(smallFullUpdate);

        smallFullService.updateSmallFullAccounts(newPeriodEndOn, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(smallFullUpdate, times(1)).execute();
    }

    @Test
    @DisplayName("Update Small Full Accounts - Throws ApiErrorResponseException")
    void updateSmallFullApiErrorResponseExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullResourceHandler.update(anyString(), any(SmallFullApi.class)))
                .thenReturn(smallFullUpdate);

        when(smallFullUpdate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                smallFullService.updateSmallFullAccounts(null, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Update Small Full Accounts - Throws URIValidationException")
    void updateSmallFullURIValidationExceptionThrown() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullResourceHandler.update(anyString(), any(SmallFullApi.class)))
                .thenReturn(smallFullUpdate);

        when(smallFullUpdate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                smallFullService.updateSmallFullAccounts(null, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Is Multi Year Filer - No Last Accounts")
    void isMultiYearFilerNoLastAccounts() {

        when(smallFullApi.getLastAccounts()).thenReturn(null);

        assertFalse(smallFullService.isMultiYearFiler(smallFullApi));
    }

    @Test
    @DisplayName("Is Multi Year Filer - No Last Accounts Period End")
    void isMultiYearFilerNoLastAccountsPeriodEnd() {

        when(smallFullApi.getLastAccounts()).thenReturn(lastAccounts);

        when(lastAccounts.getPeriodEndOn()).thenReturn(null);

        assertFalse(smallFullService.isMultiYearFiler(smallFullApi));
    }

    @Test
    @DisplayName("Is Multi Year Filer - Has Last Accounts Period End")
    void isMultiYearFilerHasLastAccountsPeriodEnd() {

        when(smallFullApi.getLastAccounts()).thenReturn(lastAccounts);

        when(lastAccounts.getPeriodEndOn()).thenReturn(LocalDate.now());

        assertTrue(smallFullService.isMultiYearFiler(smallFullApi));
    }

    @Test
    @DisplayName("Get Balance Sheet Headings - Single Year Filer")
    void getBalanceSheetHeadingsSingleYearFiler() {

        when(smallFullApi.getLastAccounts()).thenReturn(null);

        when(smallFullApi.getNextAccounts()).thenReturn(nextAccounts);

        when(nextAccounts.getPeriodStartOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_START);

        when(nextAccounts.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END);

        when(accountsDatesHelper.generateBalanceSheetHeading(
                NEXT_ACCOUNTS_PERIOD_START, NEXT_ACCOUNTS_PERIOD_END, false))
                .thenReturn(NEXT_ACCOUNTS_HEADING);

        BalanceSheetHeadings balanceSheetHeadings =
                smallFullService.getBalanceSheetHeadings(smallFullApi);

        assertNotNull(balanceSheetHeadings);
        assertEquals(NEXT_ACCOUNTS_HEADING, balanceSheetHeadings.getCurrentPeriodHeading());
        assertNull(balanceSheetHeadings.getPreviousPeriodHeading());
    }

    @Test
    @DisplayName("Get Balance Sheet Headings - Multi Year Filer")
    void getBalanceSheetHeadingsMultiYearFiler() {

        when(smallFullApi.getLastAccounts()).thenReturn(lastAccounts);

        when(lastAccounts.getPeriodStartOn()).thenReturn(LAST_ACCOUNTS_PERIOD_START);

        when(lastAccounts.getPeriodEndOn()).thenReturn(LAST_ACCOUNTS_PERIOD_END);

        when(smallFullApi.getNextAccounts()).thenReturn(nextAccounts);

        when(nextAccounts.getPeriodStartOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_START);

        when(nextAccounts.getPeriodEndOn()).thenReturn(NEXT_ACCOUNTS_PERIOD_END);

        when(accountsDatesHelper.isSameYear(LAST_ACCOUNTS_PERIOD_END, NEXT_ACCOUNTS_PERIOD_END))
                .thenReturn(true);

        when(accountsDatesHelper.generateBalanceSheetHeading(
                any(LocalDate.class), any(LocalDate.class), eq(true)))
                .thenReturn(LAST_ACCOUNTS_HEADING)
                .thenReturn(NEXT_ACCOUNTS_HEADING);

        BalanceSheetHeadings balanceSheetHeadings =
                smallFullService.getBalanceSheetHeadings(smallFullApi);

        assertNotNull(balanceSheetHeadings);
        assertEquals(NEXT_ACCOUNTS_HEADING, balanceSheetHeadings.getCurrentPeriodHeading());
        assertEquals(LAST_ACCOUNTS_HEADING, balanceSheetHeadings.getPreviousPeriodHeading());
    }
}
