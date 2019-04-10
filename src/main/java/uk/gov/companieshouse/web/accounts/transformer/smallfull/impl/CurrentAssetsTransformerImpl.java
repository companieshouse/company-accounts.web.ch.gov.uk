package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CashAtBankAndInHand;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component("currentAssetsTransformer")
public class CurrentAssetsTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (hasCurrentPeriodCurrentAssets(balanceSheet)) {
            CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
            currentAssetsApi.setStocks(balanceSheet.getCurrentAssets().getStocks().getCurrentAmount());
            currentAssetsApi.setDebtors(balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount());
            currentAssetsApi.setCashAtBankAndInHand(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getCurrentAmount());
            currentAssetsApi.setTotal(balanceSheet.getCurrentAssets().getCurrentTotal());

            balanceSheetApi.setCurrentAssets(currentAssetsApi);
        }
    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (hasPreviousPeriodCurrentAssets(balanceSheet)) {
            CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
            currentAssetsApi.setStocks(balanceSheet.getCurrentAssets().getStocks().getPreviousAmount());
            currentAssetsApi.setDebtors(balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount());
            currentAssetsApi.setCashAtBankAndInHand(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getPreviousAmount());
            currentAssetsApi.setTotal(balanceSheet.getCurrentAssets().getPreviousTotal());

            balanceSheetApi.setCurrentAssets(currentAssetsApi);
        }
    }

    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        CurrentAssets currentAssets = createCurrentAssets(balanceSheet);
        CurrentAssetsApi currentAssetsApi = balanceSheetApi.getCurrentAssets();

        // Stocks
        if (currentAssetsApi.getStocks() != null) {
            Stocks stocks = createStocks(balanceSheet);
            stocks.setCurrentAmount(currentAssetsApi.getStocks());
        }

        // Debtors
        if (currentAssetsApi.getDebtors() != null) {
            Debtors debtors = createDebtors(balanceSheet);
            debtors.setCurrentAmount(currentAssetsApi.getDebtors());
        }

        // Cash at bank and in hand
        if (currentAssetsApi.getCashAtBankAndInHand() != null) {
            CashAtBankAndInHand cashAtBankAndInHand = createCashAtBankAndInHand(balanceSheet);
            cashAtBankAndInHand.setCurrentAmount(currentAssetsApi.getCashAtBankAndInHand());
        }

        // Total
        if (currentAssetsApi.getTotal() != null) {
            currentAssets.setCurrentTotal(currentAssetsApi.getTotal());
        }
    }

    @Override
    public void addPreviousPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        CurrentAssets currentAssets = createCurrentAssets(balanceSheet);
        CurrentAssetsApi currentAssetsApi = balanceSheetApi.getCurrentAssets();

        // Stocks
        if (currentAssetsApi.getStocks() != null) {
            Stocks stocks = createStocks(balanceSheet);
            stocks.setPreviousAmount(currentAssetsApi.getStocks());
        }

        // Debtors
        if (currentAssetsApi.getDebtors() != null) {
            Debtors debtors = createDebtors(balanceSheet);
            debtors.setPreviousAmount(currentAssetsApi.getDebtors());
        }

        // Cash at bank and in hand
        if (currentAssetsApi.getCashAtBankAndInHand() != null) {
            CashAtBankAndInHand cashAtBankAndInHand = createCashAtBankAndInHand(balanceSheet);
            cashAtBankAndInHand.setPreviousAmount(currentAssetsApi.getCashAtBankAndInHand());
        }

        // Total
        if (currentAssetsApi.getTotal() != null) {
            currentAssets.setPreviousTotal(currentAssetsApi.getTotal());
        }
    }

    private CurrentAssets createCurrentAssets(BalanceSheet balanceSheet) {

        CurrentAssets currentAssets;

        if (balanceSheet.getCurrentAssets() == null) {
            currentAssets = new CurrentAssets();
            balanceSheet.setCurrentAssets(currentAssets);
        } else {
            currentAssets = balanceSheet.getCurrentAssets();
        }

        return currentAssets;
    }

    private Stocks createStocks(BalanceSheet balanceSheet) {

        Stocks stocks;

        if (balanceSheet.getCurrentAssets().getStocks() == null) {
            stocks = new Stocks();
            balanceSheet.getCurrentAssets().setStocks(stocks);
        } else {
            stocks = balanceSheet.getCurrentAssets().getStocks();
        }

        return stocks;
    }

    private Debtors createDebtors(BalanceSheet balanceSheet) {

        Debtors debtors;

        if (balanceSheet.getCurrentAssets().getDebtors() == null) {
            debtors = new Debtors();
            balanceSheet.getCurrentAssets().setDebtors(debtors);
        } else {
            debtors = balanceSheet.getCurrentAssets().getDebtors();
        }

        return debtors;
    }

    private CashAtBankAndInHand createCashAtBankAndInHand(BalanceSheet balanceSheet) {

        CashAtBankAndInHand cashAtBankAndInHand;

        if (balanceSheet.getCurrentAssets().getCashAtBankAndInHand() == null) {
            cashAtBankAndInHand = new CashAtBankAndInHand();
            balanceSheet.getCurrentAssets().setCashAtBankAndInHand(cashAtBankAndInHand);
        } else {
            cashAtBankAndInHand = balanceSheet.getCurrentAssets().getCashAtBankAndInHand();
        }

        return cashAtBankAndInHand;
    }

    private Boolean hasCurrentPeriodCurrentAssets(BalanceSheet balanceSheet) {

        CurrentAssets currentAssets = balanceSheet.getCurrentAssets();

        return Stream.of(currentAssets.getStocks().getCurrentAmount(),
                    currentAssets.getDebtors().getCurrentAmount(),
                    currentAssets.getCashAtBankAndInHand().getCurrentAmount(),
                    currentAssets.getCurrentTotal()).
                anyMatch(Objects::nonNull);
    }

    private Boolean hasPreviousPeriodCurrentAssets(BalanceSheet balanceSheet) {

        CurrentAssets currentAssets = balanceSheet.getCurrentAssets();

        return Stream.of(currentAssets.getStocks().getPreviousAmount(),
                    currentAssets.getDebtors().getPreviousAmount(),
                    currentAssets.getCashAtBankAndInHand().getPreviousAmount(),
                    currentAssets.getPreviousTotal()).
                anyMatch(Objects::nonNull);
    }
}
