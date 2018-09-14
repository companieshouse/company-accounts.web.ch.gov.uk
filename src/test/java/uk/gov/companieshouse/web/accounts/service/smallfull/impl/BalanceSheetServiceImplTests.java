package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.DateTime;
import java.time.LocalDate;
import java.util.Date;
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
import uk.gov.companieshouse.api.handler.transaction.TransactionResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.CompanyAccountResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.smallfull.subresource.CurrentPeriodResourceHandler;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BalanceSheetServiceImplTests {

    @Mock
    private BalanceSheetTransformer transformer;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private TransactionResourceHandler transactionResourceHandler;

    @Mock
    private CompanyAccountResourceHandler companyAccountResourceHandler;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CurrentPeriodResourceHandler currentPeriodResourceHandler;

    @Mock
    private AccountsDatesHelper accountsDatesHelper;

    @InjectMocks
    private BalanceSheetService balanceSheetService = new BalanceSheetServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Get Balance Sheet - Success Path")
    void getBalanceSheetSuccess() throws ServiceException, ApiErrorResponseException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();

        when(currentPeriodResourceHandler.get()).thenReturn(currentPeriod);

        when(transformer.getBalanceSheet(currentPeriod)).thenReturn(new BalanceSheet());

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(balanceSheet);
    }

    @Test
    @DisplayName("Get Balance Sheet - Throws ApiErrorResponseException")
    void getBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);

        when(currentPeriodResourceHandler.get()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Balance Sheet - Creates Balance Sheet if not found")
    void getBalanceSheetNotFound() throws ServiceException, ApiErrorResponseException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404, "Not Found", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(currentPeriodResourceHandler.get()).thenThrow(apiErrorResponseException);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(balanceSheet);
    }

    @Test
    @DisplayName("Post Balance Sheet - Success Path")
    void postBalanceSheetSuccess() throws ApiErrorResponseException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);

        BalanceSheet balanceSheet = new BalanceSheet();

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();

        when(transformer.getCurrentPeriod(balanceSheet)).thenReturn(currentPeriod);

        when(currentPeriodResourceHandler.create(currentPeriod)).thenReturn(currentPeriod);

        assertAll(() ->
                balanceSheetService.postBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, balanceSheet));
    }

    @Test
    @DisplayName("Post Balance Sheet - Throws ApiErrorResponseException")
    void postBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);

        BalanceSheet balanceSheet = new BalanceSheet();

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();

        when(transformer.getCurrentPeriod(balanceSheet)).thenReturn(currentPeriod);

        when(currentPeriodResourceHandler.create(currentPeriod)).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                balanceSheetService.postBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, balanceSheet));
    }

    @Test
    @DisplayName("Get Balance Sheet Headings when Previous Period is Null")
    void getBalanceSheetHeadingsPreviousPeriodNull() {

        DateTime currentPeriodStart = new DateTime("2018-01-01");
        DateTime currentPeriodEnd = new DateTime("2018-01-01");

        NextAccountsApi nextAccounts = new NextAccountsApi();
        nextAccounts.setPeriodStartOn(currentPeriodStart);
        nextAccounts.setPeriodEndOn(currentPeriodEnd);

        CompanyAccountApi companyAccounts = new CompanyAccountApi();
        companyAccounts.setNextAccounts(nextAccounts);

        CompanyProfileApi companyProfile = new CompanyProfileApi();
        companyProfile.setAccounts(companyAccounts);

        String currentPeriodHeading = "currentPeriodHeading";

        when(accountsDatesHelper.generateBalanceSheetHeading(
                any(LocalDate.class), any(LocalDate.class), anyBoolean())).thenReturn(currentPeriodHeading);

        when(accountsDatesHelper.convertDateToLocalDate(any(Date.class))).thenReturn(LocalDate.now());

        BalanceSheetHeadings balanceSheetHeadings = balanceSheetService.getBalanceSheetHeadings(companyProfile);

        assertEquals(currentPeriodHeading, balanceSheetHeadings.getCurrentPeriodHeading());
    }

    @Test
    @DisplayName("Get Balance Sheet Headings when Previous Period exists but period end date is null")
    void getBalanceSheetHeadingsPreviousPeriodDataTypeNull() {

        DateTime currentPeriodStart = new DateTime("2018-01-01");
        DateTime currentPeriodEnd = new DateTime("2018-01-01");

        NextAccountsApi nextAccounts = new NextAccountsApi();
        nextAccounts.setPeriodStartOn(currentPeriodStart);
        nextAccounts.setPeriodEndOn(currentPeriodEnd);

        LastAccountsApi lastAccounts = new LastAccountsApi();
        lastAccounts.setPeriodEndOn(null);

        CompanyAccountApi companyAccounts = new CompanyAccountApi();
        companyAccounts.setNextAccounts(nextAccounts);
        companyAccounts.setLastAccounts(lastAccounts);

        CompanyProfileApi companyProfile = new CompanyProfileApi();
        companyProfile.setAccounts(companyAccounts);

        String currentPeriodHeading = "currentPeriodHeading";

        when(accountsDatesHelper.generateBalanceSheetHeading(
                any(LocalDate.class), any(LocalDate.class), anyBoolean())).thenReturn(currentPeriodHeading);

        when(accountsDatesHelper.convertDateToLocalDate(any(Date.class))).thenReturn(LocalDate.now());

        BalanceSheetHeadings balanceSheetHeadings = balanceSheetService.getBalanceSheetHeadings(companyProfile);

        assertEquals(currentPeriodHeading, balanceSheetHeadings.getCurrentPeriodHeading());
    }

    @Test
    @DisplayName("Get Balance Sheet Headings when Previous Period exists and period end date is not null")
    void getBalanceSheetHeadingsPreviousPeriodNotNull() {

        DateTime currentPeriodStart = new DateTime("2018-01-01");
        DateTime currentPeriodEnd = new DateTime("2018-01-01");
        DateTime previousPeriodEnd = new DateTime("2018-01-01");

        NextAccountsApi nextAccounts = new NextAccountsApi();
        nextAccounts.setPeriodStartOn(currentPeriodStart);
        nextAccounts.setPeriodEndOn(currentPeriodEnd);

        LastAccountsApi lastAccounts = new LastAccountsApi();
        lastAccounts.setPeriodEndOn(previousPeriodEnd);

        CompanyAccountApi companyAccounts = new CompanyAccountApi();
        companyAccounts.setNextAccounts(nextAccounts);
        companyAccounts.setLastAccounts(lastAccounts);

        CompanyProfileApi companyProfile = new CompanyProfileApi();
        companyProfile.setAccounts(companyAccounts);


        String currentPeriodHeading = "currentPeriodHeading";

        when(accountsDatesHelper.generateBalanceSheetHeading(
                any(LocalDate.class), any(LocalDate.class), anyBoolean()))
                .thenReturn(currentPeriodHeading);

        when(accountsDatesHelper.convertDateToLocalDate(any(Date.class)))
                .thenReturn(LocalDate.now());

        when(accountsDatesHelper.isSameYear(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        BalanceSheetHeadings balanceSheetHeadings = balanceSheetService.getBalanceSheetHeadings(companyProfile);

        assertEquals(currentPeriodHeading, balanceSheetHeadings.getCurrentPeriodHeading());

        verify(accountsDatesHelper, times(1))
                .isSameYear(any(LocalDate.class), any(LocalDate.class));
    }
}
