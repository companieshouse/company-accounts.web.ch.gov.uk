package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.PreviousPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.PaymentsOnAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Total;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.StocksTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StocksTransformerImplTests {

    private final NoteTransformer<StocksNote, StocksApi> transformer = new StocksTransformerImpl();

    private static final Long PAYMENT_ON_ACCOUNT_VALUE = 5L;
    private static final Long STOCKS_VALUE = 10L;
    private static final Long TOTAL_VALUE = 15L;

    @Test
    @DisplayName("All current period values added to stock sheet web model")
    void transformStocksForCurrentPeriodApiToWeb() {

        StocksApi stocksApi = new StocksApi();

        CurrentPeriod stocksCurrentPeriod = new CurrentPeriod();

        stocksCurrentPeriod.setPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksCurrentPeriod.setStocks(STOCKS_VALUE);
        stocksCurrentPeriod.setTotal(TOTAL_VALUE);

        stocksApi.setCurrentPeriod(stocksCurrentPeriod);

        StocksNote stocksNote = transformer.toWeb(stocksApi);

        assertNotNull(stocksNote);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksNote.getPaymentsOnAccount().getCurrentPaymentsOnAccount());
        assertEquals(STOCKS_VALUE, stocksNote.getStocks().getCurrentStocks());
        assertEquals(TOTAL_VALUE, stocksNote.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("Only populated Current period values added to stock sheet web model")
    void transformCurrentPeriodPopulatedValuesApiToWeb() {

        StocksApi stocksApi = new StocksApi();

        CurrentPeriod stocksCurrentPeriod = new CurrentPeriod();

        stocksCurrentPeriod.setPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksCurrentPeriod.setTotal(TOTAL_VALUE);

        stocksApi.setCurrentPeriod(stocksCurrentPeriod);

        StocksNote stocksNote = transformer.toWeb(stocksApi);

        assertNotNull(stocksNote);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksNote.getPaymentsOnAccount().getCurrentPaymentsOnAccount());
        assertEquals(TOTAL_VALUE, stocksNote.getTotal().getCurrentTotal());
    }

    @Test
    @DisplayName("All previous period values added to stocks sheet web model")
    void transformStocksForPreviousPeriodApiToWeb() {

        StocksApi stocksApi = new StocksApi();

        PreviousPeriod stocksPreviousPeriod = new PreviousPeriod();

        stocksPreviousPeriod.setPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksPreviousPeriod.setStocks(STOCKS_VALUE);
        stocksPreviousPeriod.setTotal(TOTAL_VALUE);

        stocksApi.setPreviousPeriod(stocksPreviousPeriod);

        StocksNote stocksNote = transformer.toWeb(stocksApi);

        assertNotNull(stocksNote);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksNote.getPaymentsOnAccount().getPreviousPaymentsOnAccount());
        assertEquals(STOCKS_VALUE, stocksNote.getStocks().getPreviousStocks());
        assertEquals(TOTAL_VALUE, stocksNote.getTotal().getPreviousTotal());
    }

    @Test
    @DisplayName("Only populated Previous period values added to stock sheet web model")
    void transformPreviousPeriodPopulatedValuesApiToWeb() {

        StocksApi stocksApi = new StocksApi();

        PreviousPeriod stocksPreviousPeriod = new PreviousPeriod();

        stocksPreviousPeriod.setPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksPreviousPeriod.setTotal(TOTAL_VALUE);

        stocksApi.setPreviousPeriod(stocksPreviousPeriod);

        StocksNote stocksNote = transformer.toWeb(stocksApi);

        assertNotNull(stocksNote);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksNote.getPaymentsOnAccount().getPreviousPaymentsOnAccount());
        assertEquals(TOTAL_VALUE, stocksNote.getTotal().getPreviousTotal());
    }

    @Test
    @DisplayName("All Current period value added to stocks API model when all present")
    void currentPeriodValueAddedToApiModel() {

        StocksNote stocksNote = new StocksNote();
        createFullCurrentDebtors(stocksNote);

        StocksApi stocksApi = transformer.toApi(stocksNote);

        assertNotNull(stocksApi);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksApi.getCurrentPeriod().getPaymentsOnAccount());
        assertEquals(STOCKS_VALUE, stocksApi.getCurrentPeriod().getStocks());
        assertEquals(TOTAL_VALUE, stocksApi.getCurrentPeriod().getTotal());
    }

    @Test
    @DisplayName("All previous period values added to stocks API model when present")
    void previousPeriodValueAddedToApiModel() {

        StocksNote stocksNote = new StocksNote();
        createFullPreviousDebtors(stocksNote);

        StocksApi stocksApi = transformer.toApi(stocksNote);

        assertNotNull(stocksApi);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksApi.getPreviousPeriod().getPaymentsOnAccount());
        assertEquals(STOCKS_VALUE, stocksApi.getPreviousPeriod().getStocks());
        assertEquals(TOTAL_VALUE, stocksApi.getPreviousPeriod().getTotal());
    }

    @Test
    @DisplayName("Only populated current period values added to the Stocks API model when present")
    void onlyPopulatedCurrentPeriodValuesAddedToApiModel() {

        StocksNote stocksNote = new StocksNote();

        PaymentsOnAccount paymentsOnAccount = new PaymentsOnAccount();
        paymentsOnAccount.setCurrentPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksNote.setPaymentsOnAccount(paymentsOnAccount);

        stocksNote.setStocks(new Stocks());

        Total total = new Total();
        total.setCurrentTotal(TOTAL_VALUE);
        stocksNote.setTotal(total);

        StocksApi stocksApi = transformer.toApi(stocksNote);

        assertNotNull(stocksApi);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksApi.getCurrentPeriod().getPaymentsOnAccount());
        assertEquals(TOTAL_VALUE, stocksApi.getCurrentPeriod().getTotal());
    }

    @Test
    @DisplayName("Only populated previous period values added to the Stocks API model when present")
    void onlyPopulatedPreviousPeriodValuesAddedToApiModel() {

        StocksNote stocksNote = new StocksNote();

        PaymentsOnAccount paymentsOnAccount = new PaymentsOnAccount();
        paymentsOnAccount.setPreviousPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksNote.setPaymentsOnAccount(paymentsOnAccount);

        stocksNote.setStocks(new Stocks());

        Total total = new Total();
        total.setPreviousTotal(TOTAL_VALUE);
        stocksNote.setTotal(total);

        StocksApi stocksApi = transformer.toApi(stocksNote);

        assertNotNull(stocksApi);
        assertEquals(PAYMENT_ON_ACCOUNT_VALUE, stocksApi.getPreviousPeriod().getPaymentsOnAccount());
        assertEquals(TOTAL_VALUE, stocksApi.getPreviousPeriod().getTotal());
    }

    @Test
    @DisplayName("Get note type")
    void getNoteType() {

        assertEquals(NoteType.SMALL_FULL_STOCKS, transformer.getNoteType());
    }

    private void createFullCurrentDebtors(StocksNote stocksNote) {

        PaymentsOnAccount paymentsOnAccount = new PaymentsOnAccount();
        paymentsOnAccount.setCurrentPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksNote.setPaymentsOnAccount(paymentsOnAccount);

        Stocks stocks = new Stocks();
        stocks.setCurrentStocks(STOCKS_VALUE);
        stocksNote.setStocks(stocks);

        Total total = new Total();
        total.setCurrentTotal(TOTAL_VALUE);
        stocksNote.setTotal(total);
    }

    private void createFullPreviousDebtors(StocksNote stocksNote) {

        PaymentsOnAccount paymentsOnAccount = new PaymentsOnAccount();
        paymentsOnAccount.setPreviousPaymentsOnAccount(PAYMENT_ON_ACCOUNT_VALUE);
        stocksNote.setPaymentsOnAccount(paymentsOnAccount);

        Stocks stocks = new Stocks();
        stocks.setPreviousStocks(STOCKS_VALUE);
        stocksNote.setStocks(stocks);

        Total total = new Total();
        total.setPreviousTotal(TOTAL_VALUE);
        stocksNote.setTotal(total);
    }
}
