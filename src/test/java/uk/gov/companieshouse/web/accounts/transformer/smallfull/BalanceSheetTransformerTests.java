package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.OtherLiabilitiesOrAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;

import uk.gov.companieshouse.web.accounts.model.smallfull.AccrualsAndDeferredIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.NetCurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.ProvisionForLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.CashAtBankAndInHand;
import uk.gov.companieshouse.web.accounts.model.smallfull.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;

import uk.gov.companieshouse.web.accounts.model.smallfull.TotalAssetsLessCurrentLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalNetAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.BalanceSheetTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceSheetTransformerTests {

    private static final Long CURRENT_CALLED_UP_SHARE_CAPITAL = 1L;
    private static final Long CURRENT_TANGIBLE = 5L;
    private static final Long CURRENT_FIXED_ASSETS_TOTAL = 5L;
    private static final Long CURRENT_STOCKS = 3L;
    private static final Long CURRENT_DEBTORS = 3L;
    private static final Long CURRENT_CASH_AT_BANK = 3L;
    private static final Long CURRENT_CURRENT_ASSETS_TOTAL = 9L;
    private static final Long CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME = 10L;
    private static final Long CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR = 11L;
    private static final Long CURRENT_CREDITORS_DUE_AFTER_ONE_YEAR = 12L;
    private static final Long CURRENT_ACCRUALS_AND_DEFERRED_INCOME = 13L;
    private static final Long CURRENT_NET_CURRENT_ASSETS = 14L;
    private static final Long CURRENT_PROVISION_FOR_LIABILITIES = 15L;
    private static final Long CURRENT_TOTAL_NET_ASSETS = 16L;
    private static final Long CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES = 17L;

    private static final Long PREVIOUS_CALLED_UP_SHARE_CAPITAL = 2L;
    private static final Long PREVIOUS_TANGIBLE = 10L;
    private static final Long PREVIOUS_FIXED_ASSETS_TOTAL = 10L;
    private static final Long PREVIOUS_STOCKS = 6L;
    private static final Long PREVIOUS_DEBTORS = 6L;
    private static final Long PREVIOUS_CASH_AT_BANK = 6L;
    private static final Long PREVIOUS_CURRENT_ASSETS_TOTAL = 18L;
    private static final Long PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME = 20L;
    private static final Long PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR = 21L;
    private static final Long PREVIOUS_CREDITORS_DUE_AFTER_ONE_YEAR = 22L;
    private static final Long PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME = 23L;
    private static final Long PREVIOUS_NET_CURRENT_ASSETS = 24L;
    private static final Long PREVIOUS_PROVISION_FOR_LIABILITIES = 25L;
    private static final Long PREVIOUS_TOTAL_NET_ASSETS = 26L;
    private static final Long PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES = 27L;

    private BalanceSheetTransformer transformer = new BalanceSheetTransformerImpl();

    @Test
    @DisplayName("First Year Filer - Get Balance Sheet")
    void getFirstYearFilerBalanceSheet() {

        CurrentPeriodApi currentPeriodApi = createMockCurrentPeriod();

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi, null);

        assertNotNull(balanceSheet);

        // Called up share capital not paid
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());

        // Fixed assets
        assertEquals(CURRENT_TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getCurrentTotal());

        // Current assets
        assertEquals(CURRENT_STOCKS, balanceSheet.getCurrentAssets().getStocks().getCurrentAmount());
        assertEquals(CURRENT_CASH_AT_BANK, balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getCurrentAmount());
        assertEquals(CURRENT_DEBTORS, balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount());
        assertEquals(CURRENT_CURRENT_ASSETS_TOTAL, balanceSheet.getCurrentAssets().getCurrentTotal());

        // Other Liabilities and Assets
        assertEquals(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getCurrentAmount());
        assertEquals(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getCurrentAmount());
        assertEquals(CURRENT_CREDITORS_DUE_AFTER_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getCurrentAmount());
        assertEquals(CURRENT_ACCRUALS_AND_DEFERRED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getCurrentAmount());
        assertEquals(CURRENT_NET_CURRENT_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getCurrentAmount());
        assertEquals(CURRENT_PROVISION_FOR_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_NET_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getCurrentAmount());
    }

    @Test
    @DisplayName("Get Current Period")
    void getCurrentPeriod() {

        BalanceSheet balanceSheet = createMockBalanceSheetWithCurrentPeriod();

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        assertNotNull(currentPeriod);
        assertNotNull(currentPeriod.getBalanceSheetApi());

        // Called up share capital not paid
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, currentPeriod.getBalanceSheetApi().getCalledUpShareCapitalNotPaid());

        // Fixed assets
        assertNotNull(currentPeriod.getBalanceSheetApi().getFixedAssetsApi());
        assertEquals(CURRENT_TANGIBLE, currentPeriod.getBalanceSheetApi().getFixedAssetsApi().getTangibleApi());
        assertEquals(CURRENT_FIXED_ASSETS_TOTAL, currentPeriod.getBalanceSheetApi().getFixedAssetsApi().getTotal());

        // Current assets
        assertEquals(CURRENT_STOCKS, currentPeriod.getBalanceSheetApi().getCurrentAssetsApi().getStocks());
        assertEquals(CURRENT_DEBTORS, currentPeriod.getBalanceSheetApi().getCurrentAssetsApi().getDebtors());
        assertEquals(CURRENT_CASH_AT_BANK, currentPeriod.getBalanceSheetApi().getCurrentAssetsApi().getCashAtBankAndInHand());
        assertEquals(CURRENT_CURRENT_ASSETS_TOTAL, currentPeriod.getBalanceSheetApi().getCurrentAssetsApi().getTotal());

        // Other Liabilities and Assets
        assertEquals(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getPrepaymentsAndAccruedIncome());
        assertEquals(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getCreditorsDueWithinOneYear());
        assertEquals(CURRENT_CREDITORS_DUE_AFTER_ONE_YEAR, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getCreditorsAfterOneYear());
        assertEquals(CURRENT_ACCRUALS_AND_DEFERRED_INCOME, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getAccrualsAndDeferredIncome());
        assertEquals(CURRENT_NET_CURRENT_ASSETS, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getNetCurrentAssets());
        assertEquals(CURRENT_PROVISION_FOR_LIABILITIES, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getProvisionForLiabilities());
        assertEquals(CURRENT_TOTAL_NET_ASSETS, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getTotalNetAssets());
        assertEquals(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, currentPeriod.getBalanceSheetApi().getOtherLiabilitiesOrAssetsApi().getTotalAssetsLessCurrentLiabilities());
    }

    @Test
    @DisplayName("Get Previous Period")
    void getPreviousPeriod() {

        BalanceSheet balanceSheet = createMockBalanceSheetWithPreviousPeriod();

        PreviousPeriodApi previousPeriodApi = transformer.getPreviousPeriod(balanceSheet);

        assertNotNull(previousPeriodApi);
        assertNotNull(previousPeriodApi.getBalanceSheet());

        // Fixed assets
        assertNotNull(previousPeriodApi.getBalanceSheet().getFixedAssetsApi());
        assertEquals(PREVIOUS_TANGIBLE, previousPeriodApi.getBalanceSheet().getFixedAssetsApi().getTangibleApi());
        assertEquals(PREVIOUS_FIXED_ASSETS_TOTAL, previousPeriodApi.getBalanceSheet().getFixedAssetsApi().getTotal());

        // Called up share capital not paid
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL, previousPeriodApi.getBalanceSheet().getCalledUpShareCapitalNotPaid());

        // Current Assets
        assertEquals(PREVIOUS_STOCKS, previousPeriodApi.getBalanceSheet().getCurrentAssetsApi().getStocks());
        assertEquals(PREVIOUS_DEBTORS, previousPeriodApi.getBalanceSheet().getCurrentAssetsApi().getDebtors());
        assertEquals(PREVIOUS_CASH_AT_BANK, previousPeriodApi.getBalanceSheet().getCurrentAssetsApi().getCashAtBankAndInHand());
        assertEquals(PREVIOUS_CURRENT_ASSETS_TOTAL, previousPeriodApi.getBalanceSheet().getCurrentAssetsApi().getTotal());

        // Other Liabilities and Assets
        assertEquals(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getPrepaymentsAndAccruedIncome());
        assertEquals(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getCreditorsDueWithinOneYear());
        assertEquals(PREVIOUS_CREDITORS_DUE_AFTER_ONE_YEAR, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getCreditorsAfterOneYear());
        assertEquals(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getAccrualsAndDeferredIncome());
        assertEquals(PREVIOUS_NET_CURRENT_ASSETS, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getNetCurrentAssets());
        assertEquals(PREVIOUS_PROVISION_FOR_LIABILITIES, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getProvisionForLiabilities());
        assertEquals(PREVIOUS_TOTAL_NET_ASSETS, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getTotalNetAssets());
        assertEquals(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, previousPeriodApi.getBalanceSheet().getOtherLiabilitiesOrAssetsApi().getTotalAssetsLessCurrentLiabilities());
    }

    @Test
    @DisplayName("Multiple Year Filer - Get Balance Sheet")
    void getMultipleYearFilerBalanceSheet() {

        CurrentPeriodApi currentPeriodApi = createMockCurrentPeriod();
        PreviousPeriodApi previousPeriodApi = createMockPreviousPeriod();

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi, previousPeriodApi);

        assertNotNull(balanceSheet);

        // Called up share capital not paid - current period
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());

        // Called up share capital not paid - previous period
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());

        // Fixed assets - current period
        assertEquals(CURRENT_TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getCurrentTotal());

        // Fixed assets - previous period
        assertEquals(PREVIOUS_TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertEquals(PREVIOUS_FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getPreviousTotal());

        // Current Assets - current period
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertEquals(CURRENT_TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getCurrentTotal());

        // Current Assets - previous period
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
        assertEquals(PREVIOUS_TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertEquals(PREVIOUS_FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getPreviousTotal());

        // Other Liabilities and Assets - current period
        assertEquals(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getCurrentAmount());
        assertEquals(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getCurrentAmount());
        assertEquals(CURRENT_CREDITORS_DUE_AFTER_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getCurrentAmount());
        assertEquals(CURRENT_ACCRUALS_AND_DEFERRED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getCurrentAmount());
        assertEquals(CURRENT_NET_CURRENT_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getCurrentAmount());
        assertEquals(CURRENT_PROVISION_FOR_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_NET_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getCurrentAmount());

        // Other Liabilities and Assets - previous period
        assertEquals(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getPreviousAmount());
        assertEquals(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getPreviousAmount());
        assertEquals(PREVIOUS_CREDITORS_DUE_AFTER_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getPreviousAmount());
        assertEquals(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getPreviousAmount());
        assertEquals(PREVIOUS_NET_CURRENT_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getPreviousAmount());
        assertEquals(PREVIOUS_PROVISION_FOR_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_NET_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getPreviousAmount());
    }

    private BalanceSheet createMockBalanceSheetWithCurrentPeriod() {

        BalanceSheet balanceSheet = new BalanceSheet();

        // Fixed assets
        FixedAssets fixedAssets = new FixedAssets();
        TangibleAssets tangible = new TangibleAssets();
        tangible.setCurrentAmount(CURRENT_TANGIBLE);
        fixedAssets.setTangibleAssets(tangible);
        fixedAssets.setCurrentTotal(CURRENT_FIXED_ASSETS_TOTAL);
        balanceSheet.setFixedAssets(fixedAssets);

        // Called up share capital not paid
        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(CURRENT_CALLED_UP_SHARE_CAPITAL);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        // Current assets
        CurrentAssets currentAssets = new CurrentAssets();
        Stocks stocks = new Stocks();
        Debtors debtors = new Debtors();
        CashAtBankAndInHand cashAtBankAndInHand = new CashAtBankAndInHand();

        stocks.setCurrentAmount(CURRENT_STOCKS);
        debtors.setCurrentAmount(CURRENT_DEBTORS);
        cashAtBankAndInHand.setCurrentAmount(CURRENT_CASH_AT_BANK);

        currentAssets.setStocks(stocks);
        currentAssets.setDebtors(debtors);
        currentAssets.setCashAtBankAndInHand(cashAtBankAndInHand);
        currentAssets.setCurrentTotal(CURRENT_CURRENT_ASSETS_TOTAL);
        balanceSheet.setCurrentAssets(currentAssets);

        // Other Liabilities and Assets
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();

        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
        prepaymentsAndAccruedIncome.setCurrentAmount(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME);
        otherLiabilitiesOrAssets.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);

        CreditorsDueWithinOneYear creditorsDueWithinOneYear = new CreditorsDueWithinOneYear();
        creditorsDueWithinOneYear.setCurrentAmount(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR);
        otherLiabilitiesOrAssets.setCreditorsDueWithinOneYear(creditorsDueWithinOneYear);

        CreditorsAfterOneYear creditorsAfterOneYear = new CreditorsAfterOneYear();
        creditorsAfterOneYear.setCurrentAmount(CURRENT_CREDITORS_DUE_AFTER_ONE_YEAR);
        otherLiabilitiesOrAssets.setCreditorsAfterOneYear(creditorsAfterOneYear);

        AccrualsAndDeferredIncome accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
        accrualsAndDeferredIncome.setCurrentAmount(CURRENT_ACCRUALS_AND_DEFERRED_INCOME);
        otherLiabilitiesOrAssets.setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);

        NetCurrentAssets netCurrentAssets = new NetCurrentAssets();
        netCurrentAssets.setCurrentAmount(CURRENT_NET_CURRENT_ASSETS);
        otherLiabilitiesOrAssets.setNetCurrentAssets(netCurrentAssets);

        ProvisionForLiabilities provisionForLiabilities = new ProvisionForLiabilities();
        provisionForLiabilities.setCurrentAmount(CURRENT_PROVISION_FOR_LIABILITIES);
        otherLiabilitiesOrAssets.setProvisionForLiabilities(provisionForLiabilities);

        TotalNetAssets totalNetAssets = new TotalNetAssets();
        totalNetAssets.setCurrentAmount(CURRENT_TOTAL_NET_ASSETS);
        otherLiabilitiesOrAssets.setTotalNetAssets(totalNetAssets);

        TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities = new TotalAssetsLessCurrentLiabilities();
        totalAssetsLessCurrentLiabilities.setCurrentAmount(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);
        otherLiabilitiesOrAssets.setTotalAssetsLessCurrentLiabilities(totalAssetsLessCurrentLiabilities);

        balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);

        return balanceSheet;
    }

    private BalanceSheet createMockBalanceSheetWithPreviousPeriod() {

        BalanceSheet balanceSheet = new BalanceSheet();

        // Fixed assets
        FixedAssets fixedAssets = new FixedAssets();
        TangibleAssets tangible = new TangibleAssets();
        tangible.setPreviousAmount(PREVIOUS_TANGIBLE);
        fixedAssets.setTangibleAssets(tangible);
        fixedAssets.setPreviousTotal(PREVIOUS_FIXED_ASSETS_TOTAL);
        balanceSheet.setFixedAssets(fixedAssets);

        // Called up share capital not paid
        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setPreviousAmount(PREVIOUS_CALLED_UP_SHARE_CAPITAL);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        // Current assets
        CurrentAssets currentAssets = new CurrentAssets();
        Stocks stocks = new Stocks();
        Debtors debtors = new Debtors();
        CashAtBankAndInHand cashAtBankAndInHand = new CashAtBankAndInHand();

        stocks.setPreviousAmount(PREVIOUS_STOCKS);
        debtors.setPreviousAmount(PREVIOUS_DEBTORS);
        cashAtBankAndInHand.setPreviousAmount(PREVIOUS_CASH_AT_BANK);

        currentAssets.setStocks(stocks);
        currentAssets.setDebtors(debtors);
        currentAssets.setCashAtBankAndInHand(cashAtBankAndInHand);
        currentAssets.setPreviousTotal(PREVIOUS_CURRENT_ASSETS_TOTAL);

        balanceSheet.setCurrentAssets(currentAssets);

        // Other Liabilities and Assets
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();

        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
        prepaymentsAndAccruedIncome.setPreviousAmount(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME);
        otherLiabilitiesOrAssets.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);

        CreditorsDueWithinOneYear creditorsDueWithinOneYear = new CreditorsDueWithinOneYear();
        creditorsDueWithinOneYear.setPreviousAmount(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR);
        otherLiabilitiesOrAssets.setCreditorsDueWithinOneYear(creditorsDueWithinOneYear);

        CreditorsAfterOneYear creditorsAfterOneYear = new CreditorsAfterOneYear();
        creditorsAfterOneYear.setPreviousAmount(PREVIOUS_CREDITORS_DUE_AFTER_ONE_YEAR);
        otherLiabilitiesOrAssets.setCreditorsAfterOneYear(creditorsAfterOneYear);

        AccrualsAndDeferredIncome accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
        accrualsAndDeferredIncome.setPreviousAmount(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME);
        otherLiabilitiesOrAssets.setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);

        NetCurrentAssets netCurrentAssets = new NetCurrentAssets();
        netCurrentAssets.setPreviousAmount(PREVIOUS_NET_CURRENT_ASSETS);
        otherLiabilitiesOrAssets.setNetCurrentAssets(netCurrentAssets);

        ProvisionForLiabilities provisionForLiabilities = new ProvisionForLiabilities();
        provisionForLiabilities.setPreviousAmount(PREVIOUS_PROVISION_FOR_LIABILITIES);
        otherLiabilitiesOrAssets.setProvisionForLiabilities(provisionForLiabilities);

        TotalNetAssets totalNetAssets = new TotalNetAssets();
        totalNetAssets.setPreviousAmount(PREVIOUS_TOTAL_NET_ASSETS);
        otherLiabilitiesOrAssets.setTotalNetAssets(totalNetAssets);

        TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities = new TotalAssetsLessCurrentLiabilities();
        totalAssetsLessCurrentLiabilities.setPreviousAmount(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);
        otherLiabilitiesOrAssets.setTotalAssetsLessCurrentLiabilities(totalAssetsLessCurrentLiabilities);

        balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);

        return balanceSheet;
    }

    private CurrentPeriodApi createMockCurrentPeriod() {
        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        // Called up share capital not paid
        balanceSheetApi.setCalledUpShareCapitalNotPaid(CURRENT_CALLED_UP_SHARE_CAPITAL);

        // Fixed assets
        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setTangibleApi(CURRENT_TANGIBLE);
        fixedAssetsApi.setTotal(CURRENT_FIXED_ASSETS_TOTAL);

        // Current assets
        CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
        currentAssetsApi.setStocks(CURRENT_STOCKS);
        currentAssetsApi.setDebtors(CURRENT_DEBTORS);
        currentAssetsApi.setCashInBankAndInHand(CURRENT_CASH_AT_BANK);
        currentAssetsApi.setTotal(CURRENT_CURRENT_ASSETS_TOTAL);

        // Other Liabilities and Assets
        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
        otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME);
        otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(CURRENT_CREDITORS_DUE_AFTER_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(CURRENT_ACCRUALS_AND_DEFERRED_INCOME);
        otherLiabilitiesOrAssetsApi.setNetCurrentAssets(CURRENT_NET_CURRENT_ASSETS);
        otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(CURRENT_PROVISION_FOR_LIABILITIES);
        otherLiabilitiesOrAssetsApi.setTotalNetAssets(CURRENT_TOTAL_NET_ASSETS);
        otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);

        balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);
        balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);
        balanceSheetApi.setOtherLiabilitiesOrAssetsApi(otherLiabilitiesOrAssetsApi);

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        currentPeriodApi.setBalanceSheetApi(balanceSheetApi);

        return currentPeriodApi;
    }

    private PreviousPeriodApi createMockPreviousPeriod() {
        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        // Called up share capital not paid
        balanceSheetApi.setCalledUpShareCapitalNotPaid(PREVIOUS_CALLED_UP_SHARE_CAPITAL);

        // Fixed assets
        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setTangibleApi(PREVIOUS_TANGIBLE);
        fixedAssetsApi.setTotal(PREVIOUS_FIXED_ASSETS_TOTAL);

        // Current assets
        CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
        currentAssetsApi.setStocks(PREVIOUS_STOCKS);
        currentAssetsApi.setDebtors(PREVIOUS_DEBTORS);
        currentAssetsApi.setCashInBankAndInHand(PREVIOUS_CASH_AT_BANK);
        currentAssetsApi.setTotal(PREVIOUS_CURRENT_ASSETS_TOTAL);

        // Other Liabilities and Assets
        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
        otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME);
        otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(PREVIOUS_CREDITORS_DUE_AFTER_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME);
        otherLiabilitiesOrAssetsApi.setNetCurrentAssets(PREVIOUS_NET_CURRENT_ASSETS);
        otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(PREVIOUS_PROVISION_FOR_LIABILITIES);
        otherLiabilitiesOrAssetsApi.setTotalNetAssets(PREVIOUS_TOTAL_NET_ASSETS);
        otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);

        balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);
        balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);
        balanceSheetApi.setOtherLiabilitiesOrAssetsApi(otherLiabilitiesOrAssetsApi);

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        previousPeriodApi.setBalanceSheet(balanceSheetApi);

        return previousPeriodApi;
    }
}
