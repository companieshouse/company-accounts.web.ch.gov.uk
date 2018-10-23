package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.CashAtBankAndInHand;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Component
public class BalanceSheetTransformerImpl implements BalanceSheetTransformer {

    @Override
    public BalanceSheet getBalanceSheet(CurrentPeriodApi currentPeriodApi, PreviousPeriodApi previousPeriodApi) {

        BalanceSheet balanceSheet = new BalanceSheet();

        if (currentPeriodApi != null && currentPeriodApi.getBalanceSheetApi() != null) {
            populateCurrentPeriodValues(balanceSheet, currentPeriodApi.getBalanceSheetApi());
        }

        if (previousPeriodApi != null && previousPeriodApi.getBalanceSheet() != null) {
            populatePreviousPeriodValues(balanceSheet, previousPeriodApi.getBalanceSheet());
        }

        return balanceSheet;
    }

    private void populateCurrentPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssetsApi() != null) {
            populateCurrentPeriodFixedAssets(balanceSheet, balanceSheetApi.getFixedAssetsApi());
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            populateCurrentPeriodCalledUpShareCapitalNotPaid(balanceSheet, balanceSheetApi.getCalledUpShareCapitalNotPaid());
        }

        if (balanceSheetApi.getCurrentAssetsApi() != null) {
            populateCurrentPeriodCurrentAssets(balanceSheet, balanceSheetApi.getCurrentAssetsApi());
        }
    }

    private void populatePreviousPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssetsApi() != null) {
            populatePreviousPeriodFixedAssets(balanceSheet, balanceSheetApi.getFixedAssetsApi());
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            populatePreviousPeriodCalledUpShareCapitalNotPaid(balanceSheet, balanceSheetApi.getCalledUpShareCapitalNotPaid());
        }

        if (balanceSheetApi.getCurrentAssetsApi() != null) {
            populatePreviousPeriodCurrentAssets(balanceSheet, balanceSheetApi.getCurrentAssetsApi());
        }
    }

    private void populateCurrentPeriodCalledUpShareCapitalNotPaid(BalanceSheet balanceSheet, Long amount) {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

        if (balanceSheet.getCalledUpShareCapitalNotPaid() == null) {
            calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
            balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);
        } else {
            calledUpShareCapitalNotPaid = balanceSheet.getCalledUpShareCapitalNotPaid();
        }

        calledUpShareCapitalNotPaid.setCurrentAmount(amount);
    }

    private void populatePreviousPeriodCalledUpShareCapitalNotPaid(BalanceSheet balanceSheet, Long amount) {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

        if (balanceSheet.getCalledUpShareCapitalNotPaid() == null) {
            calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
            balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);
        } else {
            calledUpShareCapitalNotPaid = balanceSheet.getCalledUpShareCapitalNotPaid();
        }

        calledUpShareCapitalNotPaid.setPreviousAmount(amount);
    }

    private void populateCurrentPeriodFixedAssets(BalanceSheet balanceSheet, FixedAssetsApi fixedAssetsApi) {

        FixedAssets fixedAssets = createFixedAssets(balanceSheet);

        // Tangible assets
        if (fixedAssetsApi.getTangibleApi() != null) {

            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setCurrentAmount(fixedAssetsApi.getTangibleApi());
        }

        // Total fixed assets
        if (fixedAssetsApi.getTotal() != null) {
            fixedAssets.setTotalCurrentFixedAssets(fixedAssetsApi.getTotal());
        }
    }

    private FixedAssets createFixedAssets(BalanceSheet balanceSheet) {

        FixedAssets fixedAssets;

        if (balanceSheet.getFixedAssets() == null) {
            fixedAssets = new FixedAssets();
            balanceSheet.setFixedAssets(fixedAssets);
        } else {
            fixedAssets = balanceSheet.getFixedAssets();
        }

        return fixedAssets;
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


    private TangibleAssets createTangibleAssets(BalanceSheet balanceSheet) {

        TangibleAssets tangibleAssets;

        if (balanceSheet.getFixedAssets().getTangibleAssets() == null) {
            tangibleAssets = new TangibleAssets();
            balanceSheet.getFixedAssets().setTangibleAssets(tangibleAssets);
        } else {
            tangibleAssets = balanceSheet.getFixedAssets().getTangibleAssets();
        }

        return tangibleAssets;
    }

    private void populatePreviousPeriodFixedAssets(BalanceSheet balanceSheet, FixedAssetsApi fixedAssetsApi) {

        FixedAssets fixedAssets = createFixedAssets(balanceSheet);

        // Tangible assets
        if (fixedAssetsApi.getTangibleApi() != null) {
            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setPreviousAmount(fixedAssetsApi.getTangibleApi());
        }

        // Total fixed assets
        if (fixedAssetsApi.getTotal() != null) {
            fixedAssets.setTotalPreviousFixedAssets(fixedAssetsApi.getTotal());
        }
    }

    private void populatePreviousPeriodCurrentAssets(BalanceSheet balanceSheet, CurrentAssetsApi currentAssetsApi) {

        CurrentAssets currentAssets = createCurrentAssets(balanceSheet);

        // Stocks
        if (currentAssetsApi.getStocks() != null) {
            Stocks stocks = createStocks(balanceSheet);
            stocks.setPreviousAmount(currentAssetsApi.getStocks());
        }

        // Debtors
        if (currentAssetsApi.getStocks() != null) {
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
            currentAssets.setPreviousCurrentAssetsTotal(currentAssetsApi.getTotal());
        }
    }

    private void populateCurrentPeriodCurrentAssets(BalanceSheet balanceSheet, CurrentAssetsApi currentAssetsApi) {

        CurrentAssets currentAssets = createCurrentAssets(balanceSheet);

        // Stocks
        if (currentAssetsApi.getStocks() != null) {
            Stocks stocks = createStocks(balanceSheet);
            stocks.setCurrentAmount(currentAssetsApi.getStocks());
        }

        // Debtors
        if (currentAssetsApi.getStocks() != null) {
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
            currentAssets.setCurrentCurrentAssetsTotal(currentAssetsApi.getTotal());
        }
    }


    @Override
    public CurrentPeriodApi getCurrentPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        if (balanceSheet.getFixedAssets() != null) {

            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangibleApi(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getTotalCurrentFixedAssets());

            balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);
        }

        if (balanceSheet.getCalledUpShareCapitalNotPaid() != null) {
            balanceSheetApi.setCalledUpShareCapitalNotPaid(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        }

        if (balanceSheet.getCurrentAssets() != null) {
            addCurrentPeriodCurrentAssetsToBalanceSheet(balanceSheet, balanceSheetApi);
        }

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();
        currentPeriod.setBalanceSheetApi(balanceSheetApi);
        return currentPeriod;

    }

    @Override
    public PreviousPeriodApi getPreviousPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        if (balanceSheet.getFixedAssets() != null) {

            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangibleApi(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getTotalPreviousFixedAssets());

            balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        }

        if (balanceSheet.getCalledUpShareCapitalNotPaid() != null) {
            balanceSheetApi.setCalledUpShareCapitalNotPaid(balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());

        }

        if (balanceSheet.getCurrentAssets() != null) {
            addPreviousPeriodCurrentAssetsToBalanceSheet(balanceSheet, balanceSheetApi);
        }

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        previousPeriodApi.setBalanceSheet(balanceSheetApi);

        return previousPeriodApi;
    }


    private void addCurrentPeriodCurrentAssetsToBalanceSheet(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {
        CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
        currentAssetsApi.setStocks(balanceSheet.getCurrentAssets().getStocks().getCurrentAmount());
        currentAssetsApi.setDebtors(balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount());
        currentAssetsApi.setCashInBankAndInHand(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getCurrentAmount());
        currentAssetsApi.setTotal(balanceSheet.getCurrentAssets().getCurrentCurrentAssetsTotal());

        balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);
    }

    private void addPreviousPeriodCurrentAssetsToBalanceSheet(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {
        CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
        currentAssetsApi.setStocks(balanceSheet.getCurrentAssets().getStocks().getPreviousAmount());
        currentAssetsApi.setDebtors(balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount());
        currentAssetsApi.setCashInBankAndInHand(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getPreviousAmount());
        currentAssetsApi.setTotal(balanceSheet.getCurrentAssets().getCurrentCurrentAssetsTotal());

        balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);
    }
}
