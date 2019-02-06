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
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.stocks.StocksResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksCreate;
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.PreviousPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.PaymentsOnAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Total;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StocksTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StocksServiceImplTests {

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    @Mock
    private StocksResourceHandler mockStocksResourceHandler;

    @Mock
    private StocksGet mockStocksGet;

    @Mock
    private StocksCreate mockStocksCreate;

    @Mock
    private StocksTransformer mockStocksTransformer;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private BalanceSheet mockBalanceSheet;

    @Mock
    private SmallFullService mockSmallFullService;

    @Mock
    private ValidationContext mockValidationContext;

    @InjectMocks
    private StocksService stocksService = new StocksServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String STOCKS_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/notes/stocks";

    private static final Long FIVE = 5L;

    private static final Long TEN = 10L;

    @Test
    @DisplayName("GET - stocks successful path")
    void getStocksSuccess() throws Exception {

        StocksApi stocksApi = new StocksApi();
        getMockStocksApi(stocksApi);

        when(mockStocksTransformer.getStocks(stocksApi)).thenReturn(createStocks());
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER))
            .thenReturn(mockBalanceSheet);
        when(mockBalanceSheet.getBalanceSheetHeadings()).thenReturn(new BalanceSheetHeadings());

        StocksNote stocksNote = stocksService.getStocks(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        validateCreditorsAfterOneYear(stocksNote);
    }

    @Test
    @DisplayName("GET - stocks successful path when http status not found")
    void getStocksSuccessHttpStatusNotFound() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404, "Not" +
            " Found", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.get(STOCKS_URI)).
            thenReturn(mockStocksGet);
        when(mockStocksGet.execute()).thenThrow(apiErrorResponseException);
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER)).thenReturn(mockBalanceSheet);

        when(mockStocksTransformer.getStocks(null))
            .thenReturn(createStocks());

        StocksNote stocksNote =
            stocksService.getStocks(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        validateCreditorsAfterOneYear(stocksNote);
    }

    @Test
    @DisplayName("GET - stocks throws ServiceException due to " +
        "ApiErrorResponseException - 400 Bad Request")
    void getStocksApiResponseException() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400, "Bad" +
            " Request", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.get(STOCKS_URI))
            .thenReturn(mockStocksGet);
        when(mockStocksGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockStocksGet.execute());
        assertThrows(ServiceException.class,
            () -> stocksService.getStocks(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - stocks throws ServiceExcepiton due to " +
        "URIValidationException")
    void getStocksURIValidationException() throws Exception {

        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.get(STOCKS_URI))
            .thenReturn(mockStocksGet);
        when(mockStocksGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockStocksGet.execute());
        assertThrows(ServiceException.class,
            () -> stocksService.getStocks(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - stocks successful path")
    void postStocksSuccess() throws Exception {

        StocksNote stocksNote = createStocks();
        StocksApi stocksApi = createStocksApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);
        setLinksWithoutStocks(smallFullApi);

        when(mockStocksTransformer.getStocksApi(stocksNote)).thenReturn(stocksApi);

        stocksCreate(stocksApi);

        List<ValidationError> validationErrors = stocksService.submitStocks(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, stocksNote, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("POST - stocks throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void postStocksApiErrorResponseExceptionNotFound() throws Exception {

        getMockStocksResourceHandler();

        StocksNote stocksNote = createStocks();
        StocksApi stocksApi = createStocksApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutStocks(smallFullApi);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(mockStocksTransformer.getStocksApi(stocksNote)).thenReturn(stocksApi);
        when(mockStocksResourceHandler.create(STOCKS_URI, stocksApi)).thenReturn(mockStocksCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(
            404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockStocksCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockStocksCreate.execute());
        assertThrows(ServiceException.class, () -> stocksService.submitStocks(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            stocksNote,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - stocks throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void postStocksApiErrorResponseExceptionBadRequest() throws Exception {

        getMockStocksResourceHandler();

        StocksNote stocksNote = createStocks();
        StocksApi stocksApi = createStocksApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutStocks(smallFullApi);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(mockStocksTransformer.getStocksApi(stocksNote)).thenReturn(stocksApi);
        when(mockStocksResourceHandler.create(STOCKS_URI, stocksApi)).thenReturn(mockStocksCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(
            400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockStocksCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockStocksCreate.execute());
        assertThrows(ServiceException.class, () -> stocksService.submitStocks(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            stocksNote,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - stocks throws ServiceExcepiton getting Smallfull data")
    void postStocksGetSmallFullDataApiResponseException() throws Exception {

        StocksNote stocksNote = createStocks();
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> stocksService.submitStocks(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            stocksNote,
            COMPANY_NUMBER));
    }

    private void validateCreditorsAfterOneYear(StocksNote stocksNote) {

        assertNotNull(stocksNote);
        assertEquals(FIVE, stocksNote.getPaymentsOnAccount().getCurrentPaymentsOnAccount());
        assertEquals(FIVE, stocksNote.getPaymentsOnAccount().getPreviousPaymentsOnAccount());
        assertEquals(FIVE, stocksNote.getStocks().getCurrentStocks());
        assertEquals(FIVE, stocksNote.getStocks().getPreviousStocks());
        assertEquals(TEN, stocksNote.getTotal().getCurrentTotal());
        assertEquals(TEN, stocksNote.getTotal().getPreviousTotal());
    }

    private void getMockStocksApi(StocksApi stocksApi) throws Exception {
        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.get(STOCKS_URI)).thenReturn(mockStocksGet);
        when(mockStocksGet.execute()).thenReturn(stocksApi);
    }

    private void getMockStocksResourceHandler() {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.stocks()).thenReturn(mockStocksResourceHandler);
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private void stocksCreate(StocksApi stocksApi) throws Exception {
        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.create(STOCKS_URI, stocksApi)).thenReturn(mockStocksCreate);
        when(mockStocksCreate.execute()).thenReturn(stocksApi);
    }

    private void setLinksWithoutStocks(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setCreditorsAfterMoreThanOneYearNote("");

        smallFullApi.setLinks(links);
    }

    private StocksNote createStocks() {

        StocksNote stocksNote = new StocksNote();
        Stocks stocks = new Stocks();
        PaymentsOnAccount paymentsOnAccount = new PaymentsOnAccount();
        Total total = new Total();

        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();

        stocks.setCurrentStocks(FIVE);
        stocks.setPreviousStocks(FIVE);
        paymentsOnAccount.setCurrentPaymentsOnAccount(FIVE);
        paymentsOnAccount.setPreviousPaymentsOnAccount(FIVE);
        total.setCurrentTotal(TEN);
        total.setPreviousTotal(TEN);

        balanceSheetHeadings.setCurrentPeriodHeading("");
        balanceSheetHeadings.setPreviousPeriodHeading("");

        stocksNote.setStocks(stocks);
        stocksNote.setPaymentsOnAccount(paymentsOnAccount);
        stocksNote.setTotal(total);
        stocksNote.setBalanceSheetHeadings(balanceSheetHeadings);

        return stocksNote;
    }

    private StocksApi createStocksApi() {

        StocksApi stocksApi = new StocksApi();
        CurrentPeriod currentPeriod = new CurrentPeriod();
        PreviousPeriod previousPeriod = new PreviousPeriod();

        currentPeriod.setStocks(5L);
        currentPeriod.setTotal(5L);

        previousPeriod.setStocks(5L);
        previousPeriod.setTotal(5L);

        stocksApi.setCurrentPeriod(currentPeriod);
        stocksApi.setPreviousPeriod(previousPeriod);

        return stocksApi;
    }
}
