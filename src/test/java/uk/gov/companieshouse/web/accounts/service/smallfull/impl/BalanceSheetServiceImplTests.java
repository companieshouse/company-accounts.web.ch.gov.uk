package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.CurrentPeriodResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodCreate;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodGet;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodUpdate;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.PreviousPeriodResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.request.PreviousPeriodCreate;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.request.PreviousPeriodGet;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.request.PreviousPeriodUpdate;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.api.model.company.account.NextAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsAfterOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.CreditorsWithinOneYearService;
import uk.gov.companieshouse.web.accounts.service.smallfull.DebtorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
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
    private SmallFullGet smallFullGetMock;

    @Mock
    private CurrentPeriodResourceHandler currentPeriodResourceHandler;

    @Mock
    private PreviousPeriodResourceHandler previousPeriodResourceHandler;

    @Mock
    private AccountsDatesHelper accountsDatesHelper;

    @Mock
    private CurrentPeriodGet currentPeriodGet;

    @Mock
    private PreviousPeriodGet previousPeriodGet;

    @Mock
    private CurrentPeriodCreate currentPeriodCreate;

    @Mock
    private CurrentPeriodUpdate currentPeriodUpdate;

    @Mock
    private PreviousPeriodCreate previousPeriodCreate;

    @Mock
    private PreviousPeriodUpdate previousPeriodUpdate;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private DebtorsService debtorsService;

    @Mock
    private CreditorsWithinOneYearService creditorsWithinOneYearService;

    @Mock
    private CreditorsAfterOneYearService creditorsAfterOneYearService;

    @Mock
    private StocksService stocksService;

    @Mock
    private TangibleAssetsNoteService tangibleAssetsNoteService;

    @InjectMocks
    private BalanceSheetService balanceSheetService = new BalanceSheetServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
                                                        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                        "/small-full/";

    private static final String CURRENT_PERIOD_URI = BASE_SMALL_FULL_URI + "current-period";

    private static final String PREVIOUS_PERIOD_URI = BASE_SMALL_FULL_URI + "previous-period";

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Success Path")
    void getFirstYearFilerBalanceSheetSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();
        createFirstYearFilerCompanyProfile();

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        when(currentPeriodGet.execute()).thenReturn(currentPeriodApi);
        when(transformer.getBalanceSheet(currentPeriodApi, null)).thenReturn(createFirstYearFilerBalanceSheetTestObject());

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertNotNull(balanceSheet.getFixedAssets());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertNotNull(balanceSheet.getBalanceSheetHeadings());
    }

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Success - ApiErrorResponseException - Not found")
    void getFirstYearFilerBalanceSheetSuccessNotFound() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();
        createFirstYearFilerCompanyProfile();

        BalanceSheet mockBalanceSheet = new BalanceSheet();

        when(transformer.getBalanceSheet(isNull(), isNull())).thenReturn(mockBalanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(currentPeriodGet.execute()).thenThrow(apiErrorResponseException);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertAll("Should return a balance sheet instance",
                () -> assertEquals(mockBalanceSheet, balanceSheet),
                () -> assertNotNull(balanceSheet));
    }

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Company profile service throws ServiceException")
    void getFirstYearFilerBalanceSheetFailureCompanyService() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        when(currentPeriodGet.execute()).thenReturn(currentPeriodApi);

        doThrow(ServiceException.class).when(companyServiceMock).getCompanyProfile(anyString());

        assertThrows(ServiceException.class, () -> balanceSheetService.getBalanceSheet(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Throws ApiErrorResponseException")
    void getFirstYearFilerBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();

        when(currentPeriodGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodGet.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER));
    }

    @Test
    @DisplayName("First Year Filer - GET - Balance Sheet - Throws URIValidationException")
    void getFirstYearFilerBalanceSheetThrowsURIValidationException() throws ApiErrorResponseException, URIValidationException {
        mockApiClientGetFirstYearFiler();

        when(currentPeriodGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodGet.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Balance Sheet - Failure - Throws ApiErrorResponseException getting Small Full Data")
    void getSmallFullDataFailureApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {
        mockApiClientGet();
        when(smallFullResourceHandler.get(anyString())).thenReturn(smallFullGetMock);
        when(smallFullGetMock.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> smallFullGetMock.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        new BalanceSheet(),
                                                        "0064000"));
    }

    @Test
    @DisplayName("POST - Balance Sheet - Failure - Throws URIValidationException getting Small Full Data")
    void getSmallFullDataFailureURIValidationException() throws ApiErrorResponseException, URIValidationException {
        mockApiClientGet();
        when(smallFullResourceHandler.get(anyString())).thenReturn(smallFullGetMock);
        when(smallFullGetMock.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> smallFullGetMock.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        new BalanceSheet(),
                                                        "0064000"));
    }

    @Test
    @DisplayName("First Year Filer - POST - Balance Sheet - Success Path")
    void postFirstYearFilerBalanceSheetSuccess() throws ServiceException, URIValidationException, ApiErrorResponseException {
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createFirstYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriodPost(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000");
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("First Year Filer - POST - Balance Sheet - Success Path - Notes Present to Delete")
    void postFirstYearFilerBalanceSheetSuccessNotesPresentToDelete() throws ServiceException,
        URIValidationException, ApiErrorResponseException {
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPostNotesPresent();

        BalanceSheet balanceSheet = new BalanceSheet();

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount((long)1000);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        mockCurrentPeriodPost(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            balanceSheet,
            "0064000");
        assertEquals(0, validationErrors.size());

        verify(debtorsService, times(1)).deleteDebtors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(creditorsWithinOneYearService, times(1)).deleteCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(creditorsAfterOneYearService, times(1)).deleteCreditorsAfterOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(stocksService, times(1)).deleteStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(tangibleAssetsNoteService, times(1)).deleteTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("First Year Filer - POST - Balance Sheet - Failure - Throws ServiceException due to ApiErrorResponseException - Bad Request")
    void postFirstYearFilerBalanceSheetFailureBadRequestWithNoValidationErrorsBadRequest() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createFirstYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriodPost(balanceSheet);

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
    @DisplayName("First Year Filer - POST - Balance Sheet - Failure - Throws ServiceException due to ApiErrorResponseException - Not Bad Request")
    void postFirstYearFilerBalanceSheetFailureNonBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createFirstYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriodPost(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Bad Request",new HttpHeaders()).build();
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
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createFirstYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriodPost(balanceSheet);

        when(currentPeriodCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("First Year Filer - PUT - Balance Sheet - Success Path")
    void putFirstYearFilerBalanceSheetSuccess() throws ServiceException, URIValidationException, ApiErrorResponseException {
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createFirstYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriodPut(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000");
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("First Year Filer - PUT - Balance Sheet - Failure - Throws ServiceException due to ApiErrorResponseException")
    void putFirstYearFilerBalanceSheetFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createFirstYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriodPut(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(currentPeriodUpdate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("First Year Filer - PUT - Balance Sheet - Failure - Throws ServiceException due to URIValidationException")
    void putFirstYearFilerBalanceSheetFailureURIException() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createFirstYearFilerCompanyProfile();
        createFirstYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createFirstYearFilerBalanceSheetTestObject();
        mockCurrentPeriodPut(balanceSheet);

        when(currentPeriodUpdate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - GET - Balance Sheet - Success Path")
    void getMultipleYearFilerBalanceSheetSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetMultipleYearFiler();
        createMultipleYearFilerCompanyProfile();

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        when(currentPeriodGet.execute()).thenReturn(currentPeriodApi);

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        when(previousPeriodGet.execute()).thenReturn(previousPeriodApi);
        when(transformer.getBalanceSheet(currentPeriodApi, previousPeriodApi)).thenReturn(createMultipleYearFilerBalanceSheetTestObject());

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertNotNull(balanceSheet.getFixedAssets());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertNotNull(balanceSheet.getBalanceSheetHeadings());
    }

    @Test
    @DisplayName("Multiple Year Filer - GET - Balance Sheet - Success - ApiErrorResponseException - Not found")
    void getMultipleYearFilerBalanceSheetSuccessNotFound() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetMultipleYearFiler();
        createMultipleYearFilerCompanyProfile();

        BalanceSheet mockBalanceSheet = new BalanceSheet();

        when(transformer.getBalanceSheet(isNull(), isNull())).thenReturn(mockBalanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(previousPeriodGet.execute()).thenThrow(apiErrorResponseException);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertAll("Should return a balance sheet instance",
                () -> assertEquals(mockBalanceSheet, balanceSheet),
                () -> assertNotNull(balanceSheet));
    }

    @Test
    @DisplayName("Multiple Year Filer - GET - Balance Sheet - Throws ApiErrorResponseException")
    void getMultipleYearFilerBalanceSheetThrowsApiErrorResponseException() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetMultipleYearFiler();
        createMultipleYearFilerCompanyProfile();

        when(previousPeriodGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () -> previousPeriodGet.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Multiple Year Filer - GET - Balance Sheet - Throws URIValidationException")
    void getMultipleYearFilerBalanceSheetThrowsURIValidationException() throws ServiceException, ApiErrorResponseException, URIValidationException {
        mockApiClientGetMultipleYearFiler();
        createMultipleYearFilerCompanyProfile();

        when(previousPeriodGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> previousPeriodGet.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER));
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Success Path")
    void postMultipleYearFilerBalanceSheetSuccess() throws ServiceException, URIValidationException, ApiErrorResponseException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPost(balanceSheet);
        mockCurrentPeriodPost(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000");
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance sheet - Remove notes when corresponding values aren't present")
    void postMultipleYearFilerBalanceSheetSuccessDeleteNotes() throws ServiceException, URIValidationException, ApiErrorResponseException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPostNotesPresent();

        BalanceSheet balanceSheet = new BalanceSheet();

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount((long)1000);
        calledUpShareCapitalNotPaid.setPreviousAmount((long)1000);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        mockPreviousPeriodPost(balanceSheet);
        mockCurrentPeriodPost(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            balanceSheet,
            "0064000");
        assertEquals(0, validationErrors.size());

        verify(debtorsService, times(1)).deleteDebtors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(creditorsWithinOneYearService, times(1)).deleteCreditorsWithinOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(creditorsAfterOneYearService, times(1)).deleteCreditorsAfterOneYear(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(tangibleAssetsNoteService, times(1)).deleteTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(stocksService, times(1)).deleteStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Previous Period Failure - Throws ServiceException due to ApiErrorResponseException - Bad Request")
    void postMultipleYearFilerBalanceSheetPreviousPeriodFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPost(balanceSheet);

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
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Previous Period Failure - Throws ServiceException due to ApiErrorResponseException - Non Bad Request")
    void postMultipleYearFilerBalanceSheetPreviousPeriodFailureNonBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPost(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Bad Request",new HttpHeaders()).build();
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
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPost(balanceSheet);

        when(previousPeriodCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> previousPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Current Period Failure - Throws ServiceException due to ApiErrorResponseException - Bad Request")
    void postMultipleYearFilerBalanceSheetCurrentPeriodFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPost(balanceSheet);
        mockCurrentPeriodPost(balanceSheet);

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
    @DisplayName("Multiple Year Filer - POST - Balance Sheet - Current Period Failure - Throws ServiceException due to ApiErrorResponseException - Non Bad Request")
    void postMultipleYearFilerBalanceSheetCurrentPeriodFailureNonBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPost(balanceSheet);
        mockCurrentPeriodPost(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Bad Request",new HttpHeaders()).build();
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
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPost();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPost(balanceSheet);
        mockCurrentPeriodPost(balanceSheet);

        when(currentPeriodCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodCreate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - PUT - Balance Sheet - Success Path")
    void putMultipleYearFilerBalanceSheetSuccess() throws ServiceException, URIValidationException, ApiErrorResponseException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPut(balanceSheet);
        mockCurrentPeriodPut(balanceSheet);

        List<ValidationError> validationErrors = balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000");
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("Multiple Year Filer - PUT - Balance Sheet - Previous Period Failure - Throws ServiceException due to ApiErrorResponseException - Bad Request")
    void putMultipleYearFilerBalanceSheetPreviousPeriodFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPut(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(previousPeriodUpdate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> previousPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - PUT - Balance Sheet - Previous Period Failure - Throws ServiceException due to ApiErrorResponseException - Non Bad Request")
    void putMultipleYearFilerBalanceSheetPreviousPeriodFailureNonBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPut(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(previousPeriodUpdate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> previousPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                balanceSheet,
                "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - PUT - Balance Sheet - Previous Period Failure - Throws ServiceException due to URIValidationException")
    void putMultipleYearFilerBalanceSheetPreviousPeriodFailureURIException() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPut(balanceSheet);

        when(previousPeriodUpdate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> previousPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - PUT - Balance Sheet - Current Period Failure - Throws ServiceException due to ApiErrorResponseException - Bad Request")
    void putMultipleYearFilerBalanceSheetCurrentPeriodFailureBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPut(balanceSheet);
        mockCurrentPeriodPut(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(currentPeriodUpdate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - PUT - Balance Sheet - Current Period Failure - Throws ServiceException due to ApiErrorResponseException - Non Bad Request")
    void putMultipleYearFilerBalanceSheetCurrentPeriodFailureNonBadRequestWithNoValidationErrors() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPut(balanceSheet);
        mockCurrentPeriodPut(balanceSheet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(currentPeriodUpdate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> currentPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                balanceSheet,
                "0064000"));
    }

    @Test
    @DisplayName("Multiple Year Filer - PUT - Balance Sheet - Current Period Failure - Throws ServiceException due to URIValidationException")
    void putMultipleYearFilerBalanceSheetCurrentPeriodFailureURIException() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClient();
        createMultipleYearFilerCompanyProfile();
        createMultipleYearFilerSmallFullAccountPut();

        BalanceSheet balanceSheet = createMultipleYearFilerBalanceSheetTestObject();
        mockPreviousPeriodPut(balanceSheet);
        mockCurrentPeriodPut(balanceSheet);

        when(currentPeriodUpdate.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> currentPeriodUpdate.execute());
        assertThrows(ServiceException.class, () -> balanceSheetService.postBalanceSheet(
                                                        TRANSACTION_ID,
                                                        COMPANY_ACCOUNTS_ID,
                                                        balanceSheet,
                                                        "0064000"));
    }

    @Test
    @DisplayName("First Year Filer - Get Balance Sheet - Headings")
    void getFirstYearFilerBalanceSheetSuccessHeadings() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientGetFirstYearFiler();
        createFirstYearFilerCompanyProfile();

        String currentPeriodHeading = "currentPeriodHeading";

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        when(currentPeriodGet.execute()).thenReturn(currentPeriodApi);

        when(transformer.getBalanceSheet(currentPeriodApi, null)).thenReturn(createFirstYearFilerBalanceSheetTestObject());

        when(accountsDatesHelper.generateBalanceSheetHeading(any(LocalDate.class), any(LocalDate.class), anyBoolean())).thenReturn(currentPeriodHeading);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getBalanceSheetHeadings());
        assertEquals(currentPeriodHeading, balanceSheet.getBalanceSheetHeadings().getCurrentPeriodHeading());
    }

    @Test
    @DisplayName("Multiple Year Filer - Get Balance Sheet - Headings")
    void getMultipleYearFilerBalanceSheetSuccessHeadings() throws ApiErrorResponseException, URIValidationException, ServiceException {
        mockApiClientGetMultipleYearFiler();
        createMultipleYearFilerCompanyProfile();

        String currentPeriodHeading = "currentPeriodHeading";
        String previousPeriodHeading = "previousPeriodHeading";

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        when(currentPeriodGet.execute()).thenReturn(currentPeriodApi);

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        when(previousPeriodGet.execute()).thenReturn(previousPeriodApi);

        when(transformer.getBalanceSheet(currentPeriodApi, previousPeriodApi)).thenReturn(createMultipleYearFilerBalanceSheetTestObject());

        when(accountsDatesHelper.generateBalanceSheetHeading(any(LocalDate.class), any(LocalDate.class), anyBoolean())).thenReturn(previousPeriodHeading, currentPeriodHeading);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getBalanceSheetHeadings());
        assertEquals(currentPeriodHeading, balanceSheet.getBalanceSheetHeadings().getCurrentPeriodHeading());
        assertEquals(previousPeriodHeading, balanceSheet.getBalanceSheetHeadings().getPreviousPeriodHeading());
    }

    @Test
    @DisplayName("Cached balance sheet returned if previously set")
    void cachedBalanceSheetReturnedIfPreviouslySet() throws ServiceException {

        BalanceSheet mockCachedBalanceSheet = new BalanceSheet();

        ReflectionTestUtils.setField(balanceSheetService, "cachedBalanceSheet", mockCachedBalanceSheet);

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(balanceSheet);
        assertEquals(mockCachedBalanceSheet, balanceSheet);
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

    private SmallFullApi createSmallFullAccountForPost() {
        SmallFullApi smallFullApi = new SmallFullApi();
        smallFullApi.setLinks(new SmallFullLinks());
        return smallFullApi;
    }

    private void createFirstYearFilerSmallFullAccountPost() throws ApiErrorResponseException, URIValidationException {
        SmallFullApi smallFullApi = createSmallFullAccountForPost();
        mockSmallFullAccountGet(smallFullApi);
    }

    private void createMultipleYearFilerSmallFullAccountPost() throws ApiErrorResponseException, URIValidationException {
        SmallFullApi smallFullApi = createSmallFullAccountForPost();
        mockSmallFullAccountGet(smallFullApi);
    }

    private void createMultipleYearFilerSmallFullAccountPostNotesPresent() throws ApiErrorResponseException, URIValidationException {
        SmallFullApi smallFullApi = createSmallFullAccountForPost();
        SmallFullLinks smallFullLinks = new SmallFullLinks();
        smallFullLinks.setDebtorsNote("DEBTORS_LINK");
        smallFullLinks.setCreditorsWithinOneYearNote("CREDITORS_WITHIN_ONE_YEAR_LINK");
        smallFullLinks.setCreditorsAfterMoreThanOneYearNote("CREDITORS_AFTER_ONE_YEAR_LINK");
        smallFullLinks.setTangibleAssetsNote("TANGIBLE_ASSETS_LINK");
        smallFullLinks.setStocksNote("STOCKS_LINK");
        smallFullApi.setLinks(smallFullLinks);
        mockSmallFullAccountGet(smallFullApi);
    }

    private SmallFullApi createSmallFullAccountForPut() {
        SmallFullApi smallFullApi = new SmallFullApi();

        SmallFullLinks smallFullLinks = new SmallFullLinks();
        smallFullLinks.setCurrentPeriod("CURRENT_PERIOD_LINK");

        smallFullApi.setLinks(smallFullLinks);

        return smallFullApi;
    }

    private void createFirstYearFilerSmallFullAccountPut() throws ApiErrorResponseException, URIValidationException {
        SmallFullApi smallFullApi = createSmallFullAccountForPut();

        mockSmallFullAccountGet(smallFullApi);
    }

    private void createMultipleYearFilerSmallFullAccountPut() throws ApiErrorResponseException, URIValidationException {
        SmallFullApi smallFullApi = createSmallFullAccountForPut();

        smallFullApi.getLinks().setPreviousPeriod("PREVIOUS_PERIOD_LINK");

        mockSmallFullAccountGet(smallFullApi);
    }

    private void mockSmallFullAccountGet(SmallFullApi smallFullApi) throws ApiErrorResponseException, URIValidationException {
        when(smallFullResourceHandler.get(anyString())).thenReturn(smallFullGetMock);
        when(smallFullGetMock.execute()).thenReturn(smallFullApi);
    }

    private BalanceSheet createFirstYearFilerBalanceSheetTestObject() {
        BalanceSheet balanceSheet = new BalanceSheet();

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount((long)1000);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        FixedAssets fixedAssets = new FixedAssets();
        TangibleAssets tangibleAssets = new TangibleAssets();
        CurrentAssets currentAssets = new CurrentAssets();
        Debtors debtors = new Debtors();
        tangibleAssets.setCurrentAmount((long)1000);
        fixedAssets.setTangibleAssets(tangibleAssets);
        balanceSheet.setFixedAssets(fixedAssets);

        debtors.setCurrentAmount((long)1000);
        currentAssets.setDebtors(debtors);
        balanceSheet.setCurrentAssets(currentAssets);

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
        LocalDate previousPeriodEnd = LocalDate.parse("2018-01-01");
        LocalDate previousPeriodStart = LocalDate.parse("2017-01-01");

        LastAccountsApi lastAccounts = new LastAccountsApi();
        lastAccounts.setPeriodStartOn(previousPeriodStart);
        lastAccounts.setPeriodEndOn(previousPeriodEnd);
        return lastAccounts;
    }

    private NextAccountsApi createCurrentPeriodObject() {
        LocalDate currentPeriodStart = LocalDate.parse("2018-01-01");
        LocalDate currentPeriodEnd = LocalDate.parse("2019-01-01");

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

    private void mockApiClientGetMultipleYearFiler() {
        mockApiClientGetFirstYearFiler();
        when(smallFullResourceHandler.previousPeriod()).thenReturn(previousPeriodResourceHandler);
        when(previousPeriodResourceHandler.get(PREVIOUS_PERIOD_URI)).thenReturn(previousPeriodGet);
    }

    private void mockApiClient() {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
    }

    private PreviousPeriodApi mockPreviousPeriod(BalanceSheet balanceSheet) {
        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        when(transformer.getPreviousPeriod(balanceSheet)).thenReturn(previousPeriodApi);
        return previousPeriodApi;
    }

    private void mockPreviousPeriodPost(BalanceSheet balanceSheet) {
        PreviousPeriodApi previousPeriodApi = mockPreviousPeriod(balanceSheet);
        when(smallFullResourceHandler.previousPeriod()).thenReturn(previousPeriodResourceHandler);
        when(previousPeriodResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriodApi)).thenReturn(previousPeriodCreate);
    }

    private void mockPreviousPeriodPut(BalanceSheet balanceSheet) {
        PreviousPeriodApi previousPeriodApi = mockPreviousPeriod(balanceSheet);
        when(smallFullResourceHandler.previousPeriod()).thenReturn(previousPeriodResourceHandler);
        when(previousPeriodResourceHandler.update(PREVIOUS_PERIOD_URI, previousPeriodApi)).thenReturn(previousPeriodUpdate);
    }

    private CurrentPeriodApi mockCurrentPeriod(BalanceSheet balanceSheet) {
        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();
        when(transformer.getCurrentPeriod(balanceSheet)).thenReturn(currentPeriod);
        return currentPeriod;
    }

    private void mockCurrentPeriodPost(BalanceSheet balanceSheet) {
        CurrentPeriodApi currentPeriod = mockCurrentPeriod(balanceSheet);
        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);
        when(currentPeriodResourceHandler.create(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodCreate);
    }

    private void mockCurrentPeriodPut(BalanceSheet balanceSheet) {
        CurrentPeriodApi currentPeriod = mockCurrentPeriod(balanceSheet);
        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);
        when(currentPeriodResourceHandler.update(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodUpdate);
    }
}