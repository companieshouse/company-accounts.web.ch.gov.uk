package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.PreviousPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.PaymentsOnAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Total;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class StocksTransformerImpl implements NoteTransformer<StocksNote, StocksApi> {
    @Override
    public StocksNote toWeb(StocksApi stocksApi) {
        StocksNote stocksNote = new StocksNote();

        if (stocksApi == null) {
            return stocksNote;
        }

        populateCurrentPeriodForWeb(stocksApi, stocksNote);
        populatePreviousPeriodForWeb(stocksApi, stocksNote);

        return stocksNote;
    }

    @Override
    public StocksApi toApi(StocksNote stocksNote) {
        StocksApi stocksApi = new StocksApi();

        setCurrentPeriodDebtorsOnApiModel(stocksNote, stocksApi);
        setPreviousPeriodDebtorsOnApiModel(stocksNote, stocksApi);

        return stocksApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_STOCKS;
    }

    private void setPreviousPeriodDebtorsOnApiModel(StocksNote stocksNote, StocksApi stocksApi) {
        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setPaymentsOnAccount(
                stocksNote.getPaymentsOnAccount().getPreviousPaymentsOnAccount());

        previousPeriod.setStocks(stocksNote.getStocks().getPreviousStocks());

        previousPeriod.setTotal(stocksNote.getTotal().getPreviousTotal());

        if (isPreviousPeriodPopulated(previousPeriod)) {
            stocksApi.setPreviousPeriod(previousPeriod);
        }
    }

    private void setCurrentPeriodDebtorsOnApiModel(StocksNote stocksNote, StocksApi stocksApi) {
        CurrentPeriod currentPeriod = new CurrentPeriod();

        currentPeriod.setPaymentsOnAccount(
                stocksNote.getPaymentsOnAccount().getCurrentPaymentsOnAccount());

        currentPeriod.setStocks(stocksNote.getStocks().getCurrentStocks());

        currentPeriod.setTotal(stocksNote.getTotal().getCurrentTotal());

        if (isCurrentPeriodPopulated(currentPeriod)) {
            stocksApi.setCurrentPeriod(currentPeriod);
        }
    }

    private void populatePreviousPeriodForWeb(StocksApi stocksApi,
                                               StocksNote stocksNote) {
        PreviousPeriod previousPeriod = stocksApi.getPreviousPeriod();

        if (previousPeriod != null) {
            if (previousPeriod.getPaymentsOnAccount() != null) {
                PaymentsOnAccount paymentsOnAccount = createPaymentsOnAccount(stocksNote);
                paymentsOnAccount.setPreviousPaymentsOnAccount(previousPeriod.getPaymentsOnAccount());
            }

            if (previousPeriod.getStocks() != null) {
                Stocks stocks = createStocks(stocksNote);
                stocks.setPreviousStocks(previousPeriod.getStocks());
            }

            if (previousPeriod.getTotal() != null) {
                Total total = createTotal(stocksNote);
                total.setPreviousTotal(previousPeriod.getTotal());
            }
        }
    }

    private void populateCurrentPeriodForWeb(StocksApi stocksApi,
                                              StocksNote stocksNote) {
        CurrentPeriod currentPeriod = stocksApi.getCurrentPeriod();

        if (currentPeriod != null) {
            if (currentPeriod.getPaymentsOnAccount() != null) {
                PaymentsOnAccount paymentsOnAccount = createPaymentsOnAccount(stocksNote);
                paymentsOnAccount.setCurrentPaymentsOnAccount(currentPeriod.getPaymentsOnAccount());
            }

            if (currentPeriod.getStocks() != null) {
                Stocks stocks = createStocks(stocksNote);
                stocks.setCurrentStocks(currentPeriod.getStocks());
            }

            if (currentPeriod.getTotal() != null) {
                Total total = createTotal(stocksNote);
                total.setCurrentTotal(currentPeriod.getTotal());
            }
        }
    }

    private PaymentsOnAccount createPaymentsOnAccount(StocksNote stocksNote) {
        PaymentsOnAccount paymentsOnAccount;

        if (stocksNote.getPaymentsOnAccount() != null) {
            paymentsOnAccount = stocksNote.getPaymentsOnAccount();
        } else {
            paymentsOnAccount = new PaymentsOnAccount();
            stocksNote.setPaymentsOnAccount(paymentsOnAccount);
        }

        return paymentsOnAccount;
    }

    private Stocks createStocks(StocksNote stocksNote) {
        Stocks stocks;

        if (stocksNote.getStocks() != null) {
            stocks = stocksNote.getStocks();
        } else {
            stocks = new Stocks();
            stocksNote.setStocks(stocks);
        }

        return stocks;
    }

    private Total createTotal(StocksNote stocksNote) {
        Total total;

        if (stocksNote.getTotal() != null) {
            total = stocksNote.getTotal();
        } else {
            total = new Total();
            stocksNote.setTotal(total);
        }

        return total;
    }

    private boolean isCurrentPeriodPopulated(CurrentPeriod currentPeriod) {
        return Stream.of(currentPeriod.getPaymentsOnAccount(),
                currentPeriod.getStocks(),
                currentPeriod.getTotal()).anyMatch(Objects::nonNull);
    }

    private boolean isPreviousPeriodPopulated(PreviousPeriod previousPeriod) {
        return Stream.of(previousPeriod.getPaymentsOnAccount(),
                previousPeriod.getStocks(),
                previousPeriod.getTotal()).anyMatch(Objects::nonNull);
    }
}
