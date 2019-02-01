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
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksDelete;
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksGet;
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
import uk.gov.companieshouse.web.accounts.service.smallfull.StocksService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StocksTransformer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


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
    private StocksDelete mockStocksDelete;

    @Mock
    private StocksTransformer mockStocksTransformer;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private BalanceSheet mockBalanceSheet;

    @InjectMocks
    private StocksService stocksService = new StocksServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String STOCKS_URI = BASE_SMALL_FULL_URI + "/notes/stocks";

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
    @DisplayName("DELETE - stocks successful delete path")
    void deleteStocks() throws Exception {

        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.delete(STOCKS_URI)).thenReturn(mockStocksDelete);
        doNothing().when(mockStocksDelete).execute();

        stocksService.deleteStocks(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID);

        verify(mockStocksDelete, times(1)).execute();
    }

    @Test
    @DisplayName("DELETE - stocks throws ServiceException due to URIValidationException")
    void deleteStocksUriValidationException() throws Exception {

        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.delete(STOCKS_URI)).thenReturn(mockStocksDelete);
        when(mockStocksDelete.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockStocksDelete.execute());
        assertThrows(ServiceException.class, () -> stocksService.deleteStocks(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - stocks throws ServiceException due to ApiErrorResponseException - 404 Not Found")
    void deleteStocksApiErrorResponseExceptionNotFound() throws Exception {

        getMockStocksResourceHandler();
        when(mockStocksResourceHandler.delete(STOCKS_URI)).thenReturn(mockStocksDelete);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockStocksDelete.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockStocksDelete.execute());
        assertThrows(ServiceException.class, () -> stocksService.deleteStocks(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
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
}
