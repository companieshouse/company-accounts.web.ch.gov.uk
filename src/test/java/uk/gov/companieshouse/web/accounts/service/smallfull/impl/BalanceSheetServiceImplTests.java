package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.DateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.CurrentPeriodResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodCreate;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodGet;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.PreviousPeriodResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.request.PreviousPeriodCreate;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    private CompanyService companyServiceMock;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CurrentPeriodResourceHandler currentPeriodResourceHandler;

    @Mock
    private PreviousPeriodResourceHandler previousPeriodResourceHandler;

    @Mock
    private AccountsDatesHelper accountsDatesHelper;

    @Mock
    private CurrentPeriodGet currentPeriodGet;

    @Mock
    private CurrentPeriodCreate currentPeriodCreate;

    @Mock
    private PreviousPeriodCreate previousPeriodCreate;

    @Mock
    private ValidationContext validationContext;

    @InjectMocks
    private BalanceSheetService balanceSheetService = new BalanceSheetServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
                                                        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                        "/small-full/";

    private static final String CURRENT_PERIOD_URI = BASE_SMALL_FULL_URI + "current-period";

    private static final String PREVIOUS_PERIOD_URI = BASE_SMALL_FULL_URI + "previous-period";

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Success Path")
    void getFirstYearFilerBalanceSheetSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        when(currentPeriodGet.execute()).thenReturn(currentPeriodApi);
        when(transformer.getBalanceSheet(currentPeriodApi)).thenReturn(createFirstYearFilerBalanceSheetTestObject());
        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertNotNull(balanceSheet.getFixedAssets());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
    }

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Failure - ApiErrorResponseException - Not found")
    void getFirstYearFilerBalanceSheetFailureNotFound() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(currentPeriodGet.execute()).thenThrow(apiErrorResponseException);
        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodGet.execute());
        assertNotNull(balanceSheet);
    }

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Throws ApiErrorResponseException")
    void getFirstYearFilerBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();

        when(currentPeriodGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodGet.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }


    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Throws URIValidationException")
    void getFirstYearFilerBalanceSheetThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();

        when(currentPeriodGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodGet.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("First Year Filer - POST - Balance Sheet - Success Path")
    void postFirstYearFilerBalanceSheetSuccess() throws ServiceException {
        mockApiClientPost();
        createFirstYearFilerCompanyProfile();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriod(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000");
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("First Year Filer - POST - Balance Sheet - Failure - Throws ServiceException due to ApiErrorResponseException")
    void postFirstYearFilerBalanceSheetFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientPost();
        createFirstYearFilerCompanyProfile();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriod(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(currentPeriodCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                        "0064000"));
    }

    @Test
    @DisplayName("First Year Filer - POST - Balance Sheet - Failure - Throws ServiceException due to URIValidationException")
    void postFirstYearFilerBalanceSheetFailureURIException() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientPost();
        createFirstYearFilerCompanyProfile();
        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriod(balanceSheet);

        when(currentPeriodCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Success Path")
    void postMultipleYearFilerBalanceSheetSuccess() throws ServiceException {
        mockApiClientPost();
        createMultipleYearFilerCompanyProfile();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriod(balanceSheet);
        mockCurrentPeriod(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000");
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Previous Period Failure - Throws ServiceException due to ApiErrorResponseException")
    void postMultipleYearFilerBalanceSheetPreviousPeriodFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientPost();
        createMultipleYearFilerCompanyProfile();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriod(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(previousPeriodCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> previousPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Previous Period Failure - Throws ServiceException due to URIValidationException")
    void postMultipleYearFilerBalanceSheetPreviousPeriodFailureURIException() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientPost();
        createMultipleYearFilerCompanyProfile();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriod(balanceSheet);

        when(previousPeriodCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> previousPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Current Period Failure - Throws ServiceException due to ApiErrorResponseException")
    void postMultipleYearFilerBalanceSheetCurrentPeriodFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientPost();
        createMultipleYearFilerCompanyProfile();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriod(balanceSheet);
        mockCurrentPeriod(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(currentPeriodCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Current Period Failure - Throws ServiceException due to URIValidationException")
    void postMultipleYearFilerBalanceSheetCurrentPeriodFailureURIException() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientPost();
        createMultipleYearFilerCompanyProfile();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriod(balanceSheet);
        mockCurrentPeriod(balanceSheet);

        when(currentPeriodCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("First Year Filer - Get Balance Sheet Headings")
    void getBalanceSheetHeadingsPreviousPeriodNull() {
        String currentPeriodHeading = "currentPeriodHeading";
        when(accountsDatesHelper.generateBalanceSheetHeading(any(LocalDate.class), any(LocalDate.class), anyBoolean())).thenReturn(currentPeriodHeading);
        when(accountsDatesHelper.convertDateToLocalDate(any(Date.class))).thenReturn(LocalDate.now());

        CompanyProfileApi firstYearFilerCompanyProfile = new CompanyProfileApi();
        firstYearFilerCompanyProfile.setAccounts(createFirstYearCompanyAccountsObject());
        BalanceSheetHeadings balanceSheetHeadings = balanceSheetService.getBalanceSheetHeadings(firstYearFilerCompanyProfile);

        assertEquals(currentPeriodHeading, balanceSheetHeadings.getCurrentPeriodHeading());
        assertNull(balanceSheetHeadings.getPreviousPeriodHeading());
    }

    @Test
    @DisplayName("Multiple Year Filer - Get Balance Sheet Headings")
    void getBalanceSheetHeadingsPreviousPeriodDataTypeNull() {
        String currentPeriodHeading = "currentPeriodHeading";
        when(accountsDatesHelper.generateBalanceSheetHeading(any(LocalDate.class), any(LocalDate.class), anyBoolean())).thenReturn(currentPeriodHeading);
        when(accountsDatesHelper.convertDateToLocalDate(any(Date.class))).thenReturn(LocalDate.now());

        CompanyProfileApi multipleYearFilerCompanyProfile = new CompanyProfileApi();
        multipleYearFilerCompanyProfile.setAccounts(createMultipleYearCompanyAccountsObject());
        BalanceSheetHeadings balanceSheetHeadings = balanceSheetService.getBalanceSheetHeadings(multipleYearFilerCompanyProfile);

        assertEquals(currentPeriodHeading, balanceSheetHeadings.getCurrentPeriodHeading());
        assertNotNull(balanceSheetHeadings.getPreviousPeriodHeading());
    }

    private void createFirstYearFilerCompanyProfile() throws ServiceException {
        CompanyProfileApi companyProfile = new CompanyProfileApi();
        companyProfile.setAccounts(createFirstYearCompanyAccountsObject());

        when(companyServiceMock.getCompanyProfile(anyString())).thenReturn(companyProfile);
    }

    private void createMultipleYearFilerCompanyProfile() throws ServiceException {
        CompanyProfileApi companyProfile = new CompanyProfileApi();
        companyProfile.setAccounts(createMultipleYearCompanyAccountsObject());

        when(companyServiceMock.getCompanyProfile(anyString())).thenReturn(companyProfile);
    }

    private BalanceSheet createFirstYearFilerBalanceSheetTestObject() {
        BalanceSheet balanceSheet = new BalanceSheet();

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount((long)1000);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        FixedAssets fixedAssets = new FixedAssets();
        TangibleAssets tangibleAssets = new TangibleAssets();
        tangibleAssets.setCurrentAmount((long)1000);
        fixedAssets.setTangibleAssets(tangibleAssets);
        balanceSheet.setFixedAssets(fixedAssets);

        return balanceSheet;
    }

    private BalanceSheet createMultipleYearFilerBalanceSheetTestObject() {
        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        balanceSheet.getCalledUpShareCapitalNotPaid().setPreviousAmount((long)1000);
        return balanceSheet;
    }

    private CompanyAccountApi createFirstYearCompanyAccountsObject() {
        CompanyAccountApi companyAccounts = new CompanyAccountApi();
        companyAccounts.setNextAccounts(createCurrentPeriodObject());
        return companyAccounts;
    }

    private CompanyAccountApi createMultipleYearCompanyAccountsObject() {
        CompanyAccountApi companyAccounts = createFirstYearCompanyAccountsObject();
        companyAccounts.setLastAccounts(createPreviousPeriodObject());
        return companyAccounts;
    }

    private LastAccountsApi createPreviousPeriodObject() {
        DateTime previousPeriodEnd = new DateTime("2018-01-01");
        DateTime previousPeriodStart = new DateTime("2017-01-01");

        LastAccountsApi lastAccounts = new LastAccountsApi();
        lastAccounts.setPeriodStartOn(previousPeriodStart);
        lastAccounts.setPeriodEndOn(previousPeriodEnd);
        return lastAccounts;
    }

    private NextAccountsApi createCurrentPeriodObject() {
        DateTime currentPeriodStart = new DateTime("2018-01-01");
        DateTime currentPeriodEnd = new DateTime("2019-01-01");

        NextAccountsApi nextAccounts = new NextAccountsApi();
        nextAccounts.setPeriodStartOn(currentPeriodStart);
        nextAccounts.setPeriodEndOn(currentPeriodEnd);
        return nextAccounts;
    }

    private void mockApiClientGet() {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
    }

    private void mockApiClientGetFirstYearFiler() {
        mockApiClientGet();
        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);
        when(currentPeriodResourceHandler.get(CURRENT_PERIOD_URI)).thenReturn(currentPeriodGet);
    }

    private void mockApiClientPost() {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
    }

    private void mockPreviousPeriod(BalanceSheet balanceSheet) {
        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        when(transformer.getPreviousPeriod(balanceSheet)).thenReturn(previousPeriodApi);
        when(smallFullResourceHandler.previousPeriod()).thenReturn(previousPeriodResourceHandler);
        when(previousPeriodResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriodApi)).thenReturn(previousPeriodCreate);
    }

    private void mockCurrentPeriod(BalanceSheet balanceSheet) {
        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();
        when(transformer.getCurrentPeriod(balanceSheet)).thenReturn(currentPeriod);
        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);
        when(currentPeriodResourceHandler.create(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodCreate);
    }
}