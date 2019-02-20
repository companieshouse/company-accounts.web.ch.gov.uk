package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.PreviousPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.PaymentsOnAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Total;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StocksTransformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class StocksTransformerImpl implements StocksTransformer {

    @Override
    public StocksNote getStocks(StocksApi stocksApi) {

        if (stocksApi == null) {
            return new StocksNote();
        }

        StocksNote stocksNote = new StocksNote();

        PaymentsOnAccount paymentsOnAccount = new PaymentsOnAccount();
        Stocks stocks = new Stocks();
        Total total = new Total();

        getStocksCurrentPeriodForWeb(stocksApi, paymentsOnAccount, stocks, total);
        getStocksPreviousPeriodForWeb(stocksApi, paymentsOnAccount, stocks, total);

        stocksNote.setPaymentsOnAccount(paymentsOnAccount);
        stocksNote.setStocks(stocks);
        stocksNote.setTotal(total);

        return stocksNote;
    }

    @Override
    public StocksApi getStocksApi(StocksNote stocksNote) {

        StocksApi stocksApi = new StocksApi();

        setCurrentPeriodDebtorsOnApiModel(stocksNote, stocksApi);

        setPreviousPeriodDebtorsOnApiModel(stocksNote, stocksApi);

        return stocksApi;
    }

    private void setPreviousPeriodDebtorsOnApiModel(StocksNote stocksNote, StocksApi stocksApi) {

        PreviousPeriod previousPeriod = new PreviousPeriod();

        if (stocksNote.getPaymentsOnAccount() != null &&
            stocksNote.getPaymentsOnAccount().getPreviousPaymentsOnAccount() != null) {
            previousPeriod.setPaymentsOnAccount(
                stocksNote.getPaymentsOnAccount().getPreviousPaymentsOnAccount());
        }

        if (stocksNote.getStocks() != null && stocksNote.getStocks().getPreviousStocks() != null) {
            previousPeriod.setStocks(stocksNote.getStocks().getPreviousStocks());
        }

        if (stocksNote.getTotal() != null && stocksNote.getTotal().getPreviousTotal() != null) {
            previousPeriod.setTotal(stocksNote.getTotal().getPreviousTotal());
        }

        if (isPreviousPeriodPopulated(previousPeriod)) {
            stocksApi.setPreviousPeriod(previousPeriod);
        }
    }

    private void setCurrentPeriodDebtorsOnApiModel(StocksNote stocksNote, StocksApi stocksApi) {

        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (stocksNote.getPaymentsOnAccount() != null &&
            stocksNote.getPaymentsOnAccount().getCurrentPaymentsOnAccount() != null) {
            currentPeriod.setPaymentsOnAccount(
                stocksNote.getPaymentsOnAccount().getCurrentPaymentsOnAccount());
        }

        if (stocksNote.getStocks() != null && stocksNote.getStocks().getCurrentStocks() != null) {
            currentPeriod.setStocks(stocksNote.getStocks().getCurrentStocks());
        }

        if (stocksNote.getTotal() != null && stocksNote.getTotal().getCurrentTotal() != null) {
            currentPeriod.setTotal(stocksNote.getTotal().getCurrentTotal());
        }

        if (isCurrentPeriodPopulated(currentPeriod)) {
            stocksApi.setCurrentPeriod(currentPeriod);
        }
    }

    private void getStocksPreviousPeriodForWeb(StocksApi stocksApi,
                                               PaymentsOnAccount paymentsOnAccount,
                                               Stocks stocks, Total total) {

        if (stocksApi.getPreviousPeriod() != null) {
            paymentsOnAccount.setPreviousPaymentsOnAccount(
                stocksApi.getPreviousPeriod().getPaymentsOnAccount());
            stocks.setPreviousStocks(stocksApi.getPreviousPeriod().getStocks());
            total.setPreviousTotal(stocksApi.getPreviousPeriod().getTotal());
        }
    }

    private void getStocksCurrentPeriodForWeb(StocksApi stocksApi,
                                              PaymentsOnAccount paymentsOnAccount,
                                              Stocks stocks, Total total) {

        if (stocksApi.getCurrentPeriod() != null) {
            paymentsOnAccount.setCurrentPaymentsOnAccount(
                stocksApi.getCurrentPeriod().getPaymentsOnAccount());
            stocks.setCurrentStocks(stocksApi.getCurrentPeriod().getStocks());
            total.setCurrentTotal(stocksApi.getCurrentPeriod().getTotal());
        }
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
