package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CashAtBankAndInHand;
import uk.gov.companieshouse.web.accounts.model.smallfull.CurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.Stocks;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CurrentAssetsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrentAssetsTransformerImplTests {

    private static final Long CURRENT_CASH_IN_BANK_AND_IN_HAND = 1L;
    private static final Long CURRENT_DEBTORS = 2L;
    private static final Long CURRENT_STOCKS = 3L;
    private static final Long CURRENT_TOTAL = 4L;

    private static final Long PREVIOUS_CASH_IN_BANK_AND_IN_HAND = 10L;
    private static final Long PREVIOUS_DEBTORS = 20L;
    private static final Long PREVIOUS_STOCKS = 30L;
    private static final Long PREVIOUS_TOTAL = 40L;

    private Transformer transformer = new CurrentAssetsTransformerImpl();

    @Test
    @DisplayName("Current period values added to balance sheet web model")
    void currentPeriodValueAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForCurrentPeriod();

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCurrentAssets());

        assertNestedModelsNotNull(balanceSheet);

        assertCurrentPeriodFieldsNotNull(balanceSheet);
        assertPreviousPeriodFieldsNull(balanceSheet);

        assertOtherModelFieldsNull(balanceSheet);

        assertCurrentPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Current period values added to balance sheet web model without affecting previous period values")
    void currentPeriodValuesAddedToWebModelWithoutAffectingPreviousPeriodValues() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForCurrentPeriod();

        BalanceSheet balanceSheet = mockBalanceSheetForPreviousPeriod();

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCurrentAssets());

        assertBothPeriodFieldsNotNull(balanceSheet);

        assertOtherModelFieldsNull(balanceSheet);

        assertCurrentPeriodValuesCorrect(balanceSheet);
        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Previous period values added to balance sheet web model")
    void previousPeriodValuesAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForPreviousPeriod();

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCurrentAssets());

        assertNestedModelsNotNull(balanceSheet);

        assertPreviousPeriodFieldsNotNull(balanceSheet);
        assertCurrentPeriodFieldsNull(balanceSheet);

        assertOtherModelFieldsNull(balanceSheet);

        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Previous period values added to balance sheet web model without affecting current period values")
    void previousPeriodValuesAddedToWebModelWithoutAffectingCurrentPeriodValues() {

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForPreviousPeriod();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCurrentAssets());

        assertBothPeriodFieldsNotNull(balanceSheet);

        assertOtherModelFieldsNull(balanceSheet);

        assertCurrentPeriodValuesCorrect(balanceSheet);
        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Current period value added to balance sheet API model when present")
    void currentPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertCurrentPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Current period value not added to balance sheet API model when absent")
    void currentPeriodValueNotAddedToApiModel() {

        CurrentAssets currentAssets = new CurrentAssets();
        currentAssets.setCashAtBankAndInHand(new CashAtBankAndInHand());
        currentAssets.setDebtors(new Debtors());
        currentAssets.setStocks(new Stocks());

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCurrentAssets(currentAssets);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getFixedAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Previous period value added to balance sheet API model when present")
    void previousPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForPreviousPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertPreviousPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Previous period value not added to balance sheet API model when absent")
    void previousPeriodValueNotAddedToApiModel() {

        CurrentAssets currentAssets = new CurrentAssets();
        currentAssets.setCashAtBankAndInHand(new CashAtBankAndInHand());
        currentAssets.setDebtors(new Debtors());
        currentAssets.setStocks(new Stocks());

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCurrentAssets(currentAssets);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getFixedAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    private BalanceSheet mockBalanceSheetWithPeriods(Boolean currentPeriod, Boolean previousPeriod) {

        CashAtBankAndInHand cashAtBankAndInHand = new CashAtBankAndInHand();
        Debtors debtors = new Debtors();
        Stocks stocks = new Stocks();

        CurrentAssets currentAssets = new CurrentAssets();
        currentAssets.setCashAtBankAndInHand(cashAtBankAndInHand);
        currentAssets.setDebtors(debtors);
        currentAssets.setStocks(stocks);

        if (currentPeriod) {
            cashAtBankAndInHand.setCurrentAmount(CURRENT_CASH_IN_BANK_AND_IN_HAND);
            debtors.setCurrentAmount(CURRENT_DEBTORS);
            stocks.setCurrentAmount(CURRENT_STOCKS);
            currentAssets.setCurrentTotal(CURRENT_TOTAL);
        }

        if (previousPeriod) {
            cashAtBankAndInHand.setPreviousAmount(PREVIOUS_CASH_IN_BANK_AND_IN_HAND);
            debtors.setPreviousAmount(PREVIOUS_DEBTORS);
            stocks.setPreviousAmount(PREVIOUS_STOCKS);
            currentAssets.setPreviousTotal(PREVIOUS_TOTAL);
        }

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCurrentAssets(currentAssets);

        return balanceSheet;
    }

    private BalanceSheet mockBalanceSheetForCurrentPeriod() {
        return mockBalanceSheetWithPeriods(true, false);
    }

    private BalanceSheet mockBalanceSheetForPreviousPeriod() {
        return mockBalanceSheetWithPeriods(false, true);
    }

    private BalanceSheetApi mockBalanceSheetApiForCurrentPeriod() {

        CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
        currentAssetsApi.setCashInBankAndInHand(CURRENT_CASH_IN_BANK_AND_IN_HAND);
        currentAssetsApi.setDebtors(CURRENT_DEBTORS);
        currentAssetsApi.setStocks(CURRENT_STOCKS);
        currentAssetsApi.setTotal(CURRENT_TOTAL);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);

        return balanceSheetApi;
    }

    private BalanceSheetApi mockBalanceSheetApiForPreviousPeriod() {

        CurrentAssetsApi currentAssetsApi = new CurrentAssetsApi();
        currentAssetsApi.setCashInBankAndInHand(PREVIOUS_CASH_IN_BANK_AND_IN_HAND);
        currentAssetsApi.setDebtors(PREVIOUS_DEBTORS);
        currentAssetsApi.setStocks(PREVIOUS_STOCKS);
        currentAssetsApi.setTotal(PREVIOUS_TOTAL);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCurrentAssetsApi(currentAssetsApi);

        return balanceSheetApi;
    }

    private void assertBothPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNestedModelsNotNull(balanceSheet);
        assertCurrentPeriodFieldsNotNull(balanceSheet);
        assertPreviousPeriodFieldsNotNull(balanceSheet);
    }

    private void assertNestedModelsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getCurrentAssets().getCashAtBankAndInHand());
        assertNotNull(balanceSheet.getCurrentAssets().getDebtors());
        assertNotNull(balanceSheet.getCurrentAssets().getStocks());

    }

    private void assertCurrentPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getCurrentAmount());
        assertNull(balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount());
        assertNull(balanceSheet.getCurrentAssets().getStocks().getCurrentAmount());
        assertNull(balanceSheet.getCurrentAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getPreviousAmount());
        assertNull(balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount());
        assertNull(balanceSheet.getCurrentAssets().getStocks().getPreviousAmount());
        assertNull(balanceSheet.getCurrentAssets().getPreviousTotal());
    }

    private void assertCurrentPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getCurrentAmount());
        assertNotNull(balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount());
        assertNotNull(balanceSheet.getCurrentAssets().getStocks().getCurrentAmount());
        assertNotNull(balanceSheet.getCurrentAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getPreviousAmount());
        assertNotNull(balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount());
        assertNotNull(balanceSheet.getCurrentAssets().getStocks().getPreviousAmount());
        assertNotNull(balanceSheet.getCurrentAssets().getPreviousTotal());
    }

    private void assertCurrentPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(CURRENT_CASH_IN_BANK_AND_IN_HAND, balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getCurrentAmount());
        assertEquals(CURRENT_DEBTORS, balanceSheet.getCurrentAssets().getDebtors().getCurrentAmount());
        assertEquals(CURRENT_STOCKS, balanceSheet.getCurrentAssets().getStocks().getCurrentAmount());
        assertEquals(CURRENT_TOTAL, balanceSheet.getCurrentAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(PREVIOUS_CASH_IN_BANK_AND_IN_HAND, balanceSheet.getCurrentAssets().getCashAtBankAndInHand().getPreviousAmount());
        assertEquals(PREVIOUS_DEBTORS, balanceSheet.getCurrentAssets().getDebtors().getPreviousAmount());
        assertEquals(PREVIOUS_STOCKS, balanceSheet.getCurrentAssets().getStocks().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL, balanceSheet.getCurrentAssets().getPreviousTotal());
    }

    private void assertApiModelFieldsNotNull(BalanceSheetApi balanceSheetApi) {
        assertNotNull(balanceSheetApi.getCurrentAssetsApi());
        assertNotNull(balanceSheetApi.getCurrentAssetsApi().getCashAtBankAndInHand());
        assertNotNull(balanceSheetApi.getCurrentAssetsApi().getDebtors());
        assertNotNull(balanceSheetApi.getCurrentAssetsApi().getStocks());
        assertNotNull(balanceSheetApi.getCurrentAssetsApi().getTotal());
    }

    private void assertCurrentPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(CURRENT_CASH_IN_BANK_AND_IN_HAND, balanceSheetApi.getCurrentAssetsApi().getCashAtBankAndInHand());
        assertEquals(CURRENT_DEBTORS, balanceSheetApi.getCurrentAssetsApi().getDebtors());
        assertEquals(CURRENT_STOCKS, balanceSheetApi.getCurrentAssetsApi().getStocks());
        assertEquals(CURRENT_TOTAL, balanceSheetApi.getCurrentAssetsApi().getTotal());
    }

    private void assertPreviousPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(PREVIOUS_CASH_IN_BANK_AND_IN_HAND, balanceSheetApi.getCurrentAssetsApi().getCashAtBankAndInHand());
        assertEquals(PREVIOUS_DEBTORS, balanceSheetApi.getCurrentAssetsApi().getDebtors());
        assertEquals(PREVIOUS_STOCKS, balanceSheetApi.getCurrentAssetsApi().getStocks());
        assertEquals(PREVIOUS_TOTAL, balanceSheetApi.getCurrentAssetsApi().getTotal());
    }

    private void assertOtherModelFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getFixedAssets());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());
    }

}
