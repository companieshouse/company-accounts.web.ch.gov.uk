package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.OtherLiabilitiesOrAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.AccrualsAndDeferredIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.NetCurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.ProvisionForLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;
import uk.gov.companieshouse.web.accounts.model.smallfull.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.CashAtBankAndInHand;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalAssetsLessCurrentLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalNetAssets;
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

        if (balanceSheetApi.getOtherLiabilitiesOrAssetsApi() != null) {
            populateCurrentOtherLiabilitiesOrAssets(balanceSheet, balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
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

        if (balanceSheetApi.getOtherLiabilitiesOrAssetsApi() != null) {
            populatePreviousOtherLiabilitiesOrAssets(balanceSheet, balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
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
            fixedAssets.setCurrentTotal(fixedAssetsApi.getTotal());
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

    private OtherLiabilitiesOrAssets createOtherLiabilitiesOrAssets(BalanceSheet balanceSheet) {

        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets;

        if (balanceSheet.getOtherLiabilitiesOrAssets() == null) {
            otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();
            balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);
        } else {
            otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();
        }

        return otherLiabilitiesOrAssets;
    }

    private AccrualsAndDeferredIncome createAccrualsAndDeferredIncome(BalanceSheet balanceSheet) {

        AccrualsAndDeferredIncome accrualsAndDeferredIncome;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome() == null) {
            accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
            balanceSheet.getOtherLiabilitiesOrAssets().setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);
        } else {
            accrualsAndDeferredIncome = balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome();
        }

        return accrualsAndDeferredIncome;
    }

    private CreditorsAfterOneYear createCreditorsAfterOneYear(BalanceSheet balanceSheet) {

        CreditorsAfterOneYear creditorsAfterOneYear;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear() == null) {
            creditorsAfterOneYear = new CreditorsAfterOneYear();
            balanceSheet.getOtherLiabilitiesOrAssets().setCreditorsAfterOneYear(creditorsAfterOneYear);
        } else {
            creditorsAfterOneYear = balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear();
        }

        return creditorsAfterOneYear;
    }

    private CreditorsDueWithinOneYear createCreditorsDueWithinOneYear(BalanceSheet balanceSheet) {

        CreditorsDueWithinOneYear creditorsDueWithinOneYear;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear() == null) {
            creditorsDueWithinOneYear = new CreditorsDueWithinOneYear();
            balanceSheet.getOtherLiabilitiesOrAssets().setCreditorsDueWithinOneYear(creditorsDueWithinOneYear);
        } else {
            creditorsDueWithinOneYear = balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear();
        }

        return creditorsDueWithinOneYear;
    }

    private NetCurrentAssets createNetCurrentAssets(BalanceSheet balanceSheet) {

        NetCurrentAssets netCurrentAssets;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets() == null) {
            netCurrentAssets = new NetCurrentAssets();
            balanceSheet.getOtherLiabilitiesOrAssets().setNetCurrentAssets(netCurrentAssets);
        } else {
            netCurrentAssets = balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets();
        }

        return netCurrentAssets;
    }

    private PrepaymentsAndAccruedIncome createPrepaymentsAndAccruedIncome(BalanceSheet balanceSheet) {

        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome() == null) {
            prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
            balanceSheet.getOtherLiabilitiesOrAssets().setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);
        } else {
            prepaymentsAndAccruedIncome = balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome();
        }

        return prepaymentsAndAccruedIncome;
    }

    private ProvisionForLiabilities createProvisionForLiabilities(BalanceSheet balanceSheet) {

        ProvisionForLiabilities provisionForLiabilities;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities() == null) {
            provisionForLiabilities = new ProvisionForLiabilities();
            balanceSheet.getOtherLiabilitiesOrAssets().setProvisionForLiabilities(provisionForLiabilities);
        } else {
            provisionForLiabilities = balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities();
        }

        return provisionForLiabilities;
    }

    private TotalAssetsLessCurrentLiabilities createTotalAssetsLessCurrentLiabilities(BalanceSheet balanceSheet) {

        TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities() == null) {
            totalAssetsLessCurrentLiabilities = new TotalAssetsLessCurrentLiabilities();
            balanceSheet.getOtherLiabilitiesOrAssets().setTotalAssetsLessCurrentLiabilities(totalAssetsLessCurrentLiabilities);
        } else {
            totalAssetsLessCurrentLiabilities = balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities();
        }

        return totalAssetsLessCurrentLiabilities;
    }

    private TotalNetAssets createTotalNetAssets(BalanceSheet balanceSheet) {

        TotalNetAssets totalNetAssets;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets() == null) {
            totalNetAssets = new TotalNetAssets();
            balanceSheet.getOtherLiabilitiesOrAssets().setTotalNetAssets(totalNetAssets);
        } else {
            totalNetAssets = balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets();
        }

        return totalNetAssets;
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
            fixedAssets.setPreviousTotal(fixedAssetsApi.getTotal());
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
            currentAssets.setCurrentTotal(currentAssetsApi.getTotal());
        }
    }

    private void populateCurrentOtherLiabilitiesOrAssets(BalanceSheet balanceSheet, OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi) {

        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = createOtherLiabilitiesOrAssets(balanceSheet);

        if (otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome() != null) {
            AccrualsAndDeferredIncome accrualsAndDeferredIncome = createAccrualsAndDeferredIncome(balanceSheet);
            accrualsAndDeferredIncome.setCurrentAmount(otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear() != null) {
            CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear(balanceSheet);
            creditorsAfterOneYear.setCurrentAmount(otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear() != null) {
            CreditorsDueWithinOneYear creditorsDueWithinOneYear = createCreditorsDueWithinOneYear(balanceSheet);
            creditorsDueWithinOneYear.setCurrentAmount(otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getNetCurrentAssets() != null) {
            NetCurrentAssets netCurrentAssets = createNetCurrentAssets(balanceSheet);
            netCurrentAssets.setCurrentAmount(otherLiabilitiesOrAssetsApi.getNetCurrentAssets());
        }

        if (otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome() != null) {
            PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = createPrepaymentsAndAccruedIncome(balanceSheet);
            prepaymentsAndAccruedIncome.setCurrentAmount(otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getProvisionForLiabilities() != null) {
            ProvisionForLiabilities provisionForLiabilities = createProvisionForLiabilities(balanceSheet);
            provisionForLiabilities.setCurrentAmount(otherLiabilitiesOrAssetsApi.getProvisionForLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities() != null) {
            TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities = createTotalAssetsLessCurrentLiabilities(balanceSheet);
            totalAssetsLessCurrentLiabilities.setCurrentAmount(otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalNetAssets() != null) {
            TotalNetAssets totalNetAssets = createTotalNetAssets(balanceSheet);
            totalNetAssets.setCurrentAmount(otherLiabilitiesOrAssetsApi.getTotalNetAssets());
        }
    }

    private void populatePreviousOtherLiabilitiesOrAssets(BalanceSheet balanceSheet, OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi) {

        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = createOtherLiabilitiesOrAssets(balanceSheet);

        if (otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome() != null) {
            AccrualsAndDeferredIncome accrualsAndDeferredIncome = createAccrualsAndDeferredIncome(balanceSheet);
            accrualsAndDeferredIncome.setPreviousAmount(otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear() != null) {
            CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear(balanceSheet);
            creditorsAfterOneYear.setPreviousAmount(otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear() != null) {
            CreditorsDueWithinOneYear creditorsDueWithinOneYear = createCreditorsDueWithinOneYear(balanceSheet);
            creditorsDueWithinOneYear.setPreviousAmount(otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getNetCurrentAssets() != null) {
            NetCurrentAssets netCurrentAssets = createNetCurrentAssets(balanceSheet);
            netCurrentAssets.setPreviousAmount(otherLiabilitiesOrAssetsApi.getNetCurrentAssets());
        }

        if (otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome() != null) {
            PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = createPrepaymentsAndAccruedIncome(balanceSheet);
            prepaymentsAndAccruedIncome.setPreviousAmount(otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getProvisionForLiabilities() != null) {
            ProvisionForLiabilities provisionForLiabilities = createProvisionForLiabilities(balanceSheet);
            provisionForLiabilities.setPreviousAmount(otherLiabilitiesOrAssetsApi.getProvisionForLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities() != null) {
            TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities = createTotalAssetsLessCurrentLiabilities(balanceSheet);
            totalAssetsLessCurrentLiabilities.setPreviousAmount(otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalNetAssets() != null) {
            TotalNetAssets totalNetAssets = createTotalNetAssets(balanceSheet);
            totalNetAssets.setPreviousAmount(otherLiabilitiesOrAssetsApi.getTotalNetAssets());
        }
    }

    @Override
    public CurrentPeriodApi getCurrentPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        if (balanceSheet.getFixedAssets() != null) {

            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangibleApi(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getCurrentTotal());

            balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);
        }

        if (balanceSheet.getCalledUpShareCapitalNotPaid() != null) {
            balanceSheetApi.setCalledUpShareCapitalNotPaid(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        }

        if (balanceSheet.getCurrentAssets() != null) {
            addCurrentPeriodCurrentAssetsToBalanceSheet(balanceSheet, balanceSheetApi);
        }

        if (balanceSheet.getOtherLiabilitiesOrAssets() != null) {
            addCurrentPeriodOtherLiabilitiesOrAssets(balanceSheet, balanceSheetApi);
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
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getPreviousTotal());

            balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        }

        if (balanceSheet.getCalledUpShareCapitalNotPaid() != null) {
            balanceSheetApi.setCalledUpShareCapitalNotPaid(balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
        }

        if (balanceSheet.getCurrentAssets() != null) {
            addPreviousPeriodCurrentAssetsToBalanceSheet(balanceSheet, balanceSheetApi);
        }

        if (balanceSheet.getOtherLiabilitiesOrAssets() != null) {
            addPreviousPeriodOtherLiabilitiesOrAssets(balanceSheet, balanceSheetApi);
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
        currentAssetsApi.setTotal(balanceSheet.getCurrentAssets().getCurrentTotal());

        balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);
    }

    private void addPreviousPeriodCurrentAssetsToBalanceSheet(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {
        if (balanceSheet.getCurrentAssets() != null) {
            CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
            currentAssetsApi.setStocks(balanceSheet.getCurrentAssets().getStocks().getPreviousAmount());
            currentAssetsApi.setDebtors(balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount());
            currentAssetsApi.setCashInBankAndInHand(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getPreviousAmount());
            currentAssetsApi.setTotal(balanceSheet.getCurrentAssets().getPreviousTotal());

            balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);
        }
    }

    private void addCurrentPeriodOtherLiabilitiesOrAssets(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();

        if (otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome() != null) {
            otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome().getCurrentAmount());
        }

        if (otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear() != null) {
            otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear().getCurrentAmount());
        }

        if (otherLiabilitiesOrAssets.getCreditorsAfterOneYear() != null) {
            otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(otherLiabilitiesOrAssets.getCreditorsAfterOneYear().getCurrentAmount());
        }

        if (otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome() != null) {
            otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome().getCurrentAmount());
        }

        if (otherLiabilitiesOrAssets.getNetCurrentAssets() != null) {
            otherLiabilitiesOrAssetsApi.setNetCurrentAssets(otherLiabilitiesOrAssets.getNetCurrentAssets().getCurrentAmount());
        }

        if (otherLiabilitiesOrAssets.getProvisionForLiabilities() != null) {
            otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(otherLiabilitiesOrAssets.getProvisionForLiabilities().getCurrentAmount());
        }

        if (otherLiabilitiesOrAssets.getTotalNetAssets() != null) {
            otherLiabilitiesOrAssetsApi.setTotalNetAssets(otherLiabilitiesOrAssets.getTotalNetAssets().getCurrentAmount());
        }

        if (otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities() != null) {
            otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities().getCurrentAmount());
        }

        balanceSheetApi.setOtherLiabilitiesOrAssetsApi(otherLiabilitiesOrAssetsApi);
    }

    private void addPreviousPeriodOtherLiabilitiesOrAssets(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();

        if (otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome() != null) {
            otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome().getPreviousAmount());
        }

        if (otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear() != null) {
            otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear().getPreviousAmount());
        }

        if (otherLiabilitiesOrAssets.getCreditorsAfterOneYear() != null) {
            otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(otherLiabilitiesOrAssets.getCreditorsAfterOneYear().getPreviousAmount());
        }

        if (otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome() != null) {
            otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome().getPreviousAmount());
        }

        if (otherLiabilitiesOrAssets.getNetCurrentAssets() != null) {
            otherLiabilitiesOrAssetsApi.setNetCurrentAssets(otherLiabilitiesOrAssets.getNetCurrentAssets().getPreviousAmount());
        }

        if (otherLiabilitiesOrAssets.getProvisionForLiabilities() != null) {
            otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(otherLiabilitiesOrAssets.getProvisionForLiabilities().getPreviousAmount());
        }

        if (otherLiabilitiesOrAssets.getTotalNetAssets() != null) {
            otherLiabilitiesOrAssetsApi.setTotalNetAssets(otherLiabilitiesOrAssets.getTotalNetAssets().getPreviousAmount());
        }

        if (otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities() != null) {
            otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities().getPreviousAmount());
        }

        balanceSheetApi.setOtherLiabilitiesOrAssetsApi(otherLiabilitiesOrAssetsApi);
    }
}
