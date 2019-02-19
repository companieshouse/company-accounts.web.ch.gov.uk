package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.PaymentsOnAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.Total;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StocksTransformer;

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

    private void getStocksPreviousPeriodForWeb(StocksApi stocksApi, PaymentsOnAccount paymentsOnAccount,
                                               Stocks stocks, Total total) {

        if (stocksApi.getPreviousPeriod() != null) {
            paymentsOnAccount.setPreviousPaymentsOnAccount(stocksApi.getPreviousPeriod().getPaymentsOnAccount());
            stocks.setPreviousStocks(stocksApi.getPreviousPeriod().getStocks());
            total.setPreviousTotal(stocksApi.getPreviousPeriod().getTotal());
        }
    }

    private void getStocksCurrentPeriodForWeb(StocksApi stocksApi, PaymentsOnAccount paymentsOnAccount,
                                              Stocks stocks, Total total) {

        if (stocksApi.getCurrentPeriod() != null) {
            paymentsOnAccount.setCurrentPaymentsOnAccount(stocksApi.getCurrentPeriod().getPaymentsOnAccount());
            stocks.setCurrentStocks(stocksApi.getCurrentPeriod().getStocks());
            total.setCurrentTotal(stocksApi.getCurrentPeriod().getTotal());
        }
    }
}
