package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.OtherLiabilitiesOrAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.AccrualsAndDeferredIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.NetCurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.ProvisionForLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalAssetsLessCurrentLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalNetAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.OtherLiabilitiesOrAssetsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OtherLiabilitiesOrAssetsTransformerImplTests {

    private static final Long CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME = 1L;
    private static final Long CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR = 2L;
    private static final Long CURRENT_CREDITORS_AFTER_ONE_YEAR = 3L;
    private static final Long CURRENT_ACCRUALS_AND_DEFERRED_INCOME = 4L;
    private static final Long CURRENT_NET_CURRENT_ASSETS = 5L;
    private static final Long CURRENT_PROVISION_FOR_LIABILITIES = 6L;
    private static final Long CURRENT_TOTAL_NET_ASSETS = 7L;
    private static final Long CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES = 8L;

    private static final Long PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME = 10L;
    private static final Long PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR = 20L;
    private static final Long PREVIOUS_CREDITORS_AFTER_ONE_YEAR = 30L;
    private static final Long PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME = 40L;
    private static final Long PREVIOUS_NET_CURRENT_ASSETS = 50L;
    private static final Long PREVIOUS_PROVISION_FOR_LIABILITIES = 60L;
    private static final Long PREVIOUS_TOTAL_NET_ASSETS = 70L;
    private static final Long PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES = 80L;

    private Transformer transformer = new OtherLiabilitiesOrAssetsTransformerImpl();

    @Test
    @DisplayName("Current period values added to balance sheet web model")
    void currentPeriodValueAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForCurrentPeriod();

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets());

        assertNestedModelsNotNull(balanceSheet);

        assertCurrentPeriodFieldsNotNull(balanceSheet);
        assertPreviousPeriodFieldsNull(balanceSheet);

        assertNull(balanceSheet.getFixedAssets());
        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());

        assertCurrentPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Current period values added to balance sheet web model without affecting previous period values")
    void currentPeriodValuesAddedToWebModelWithoutAffectingPreviousPeriodValues() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForCurrentPeriod();

        BalanceSheet balanceSheet = mockBalanceSheetForPreviousPeriod();

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets());

        assertBothPeriodFieldsNotNull(balanceSheet);

        assertNull(balanceSheet.getFixedAssets());
        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());

        assertCurrentPeriodValuesCorrect(balanceSheet);
        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Previous period values added to balance sheet web model")
    void previousPeriodValuesAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForPreviousPeriod();

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets());

        assertNestedModelsNotNull(balanceSheet);

        assertCurrentPeriodFieldsNull(balanceSheet);
        assertPreviousPeriodFieldsNotNull(balanceSheet);

        assertNull(balanceSheet.getFixedAssets());
        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());

        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Previous period values added to balance sheet web model without affecting current period values")
    void previousPeriodValuesAddedToWebModelWithoutAffectingCurrentPeriodValues() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForPreviousPeriod();

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets());

        assertBothPeriodFieldsNotNull(balanceSheet);

        assertNull(balanceSheet.getFixedAssets());
        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());

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
        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertCurrentPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Current period value not added to balance sheet API model when absent")
    void currentPeriodValueNotAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetWithPeriods(false, false);

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
        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertPreviousPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Previous period value not added to balance sheet API model when absent")
    void previousPeriodValueNotAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetWithPeriods(false, false);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getFixedAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    private BalanceSheet mockBalanceSheetWithPeriods(Boolean currentPeriod, Boolean previousPeriod) {

        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
        CreditorsDueWithinOneYear creditorsDueWithinOneYear = new CreditorsDueWithinOneYear();
        CreditorsAfterOneYear creditorsAfterOneYear = new CreditorsAfterOneYear();
        AccrualsAndDeferredIncome accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
        NetCurrentAssets netCurrentAssets = new NetCurrentAssets();
        ProvisionForLiabilities provisionForLiabilities = new ProvisionForLiabilities();
        TotalNetAssets totalNetAssets = new TotalNetAssets();
        TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities = new TotalAssetsLessCurrentLiabilities();

        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();
        otherLiabilitiesOrAssets.setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);
        otherLiabilitiesOrAssets.setCreditorsDueWithinOneYear(creditorsDueWithinOneYear);
        otherLiabilitiesOrAssets.setCreditorsAfterOneYear(creditorsAfterOneYear);
        otherLiabilitiesOrAssets.setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);
        otherLiabilitiesOrAssets.setNetCurrentAssets(netCurrentAssets);
        otherLiabilitiesOrAssets.setProvisionForLiabilities(provisionForLiabilities);
        otherLiabilitiesOrAssets.setTotalNetAssets(totalNetAssets);
        otherLiabilitiesOrAssets.setTotalAssetsLessCurrentLiabilities(totalAssetsLessCurrentLiabilities);

        if (currentPeriod) {
            prepaymentsAndAccruedIncome.setCurrentAmount(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME);
            creditorsDueWithinOneYear.setCurrentAmount(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR);
            creditorsAfterOneYear.setCurrentAmount(CURRENT_CREDITORS_AFTER_ONE_YEAR);
            accrualsAndDeferredIncome.setCurrentAmount(CURRENT_ACCRUALS_AND_DEFERRED_INCOME);
            netCurrentAssets.setCurrentAmount(CURRENT_NET_CURRENT_ASSETS);
            provisionForLiabilities.setCurrentAmount(CURRENT_PROVISION_FOR_LIABILITIES);
            totalNetAssets.setCurrentAmount(CURRENT_TOTAL_NET_ASSETS);
            totalAssetsLessCurrentLiabilities.setCurrentAmount(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);
        }

        if (previousPeriod) {
            prepaymentsAndAccruedIncome.setPreviousAmount(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME);
            creditorsDueWithinOneYear.setPreviousAmount(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR);
            creditorsAfterOneYear.setPreviousAmount(PREVIOUS_CREDITORS_AFTER_ONE_YEAR);
            accrualsAndDeferredIncome.setPreviousAmount(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME);
            netCurrentAssets.setPreviousAmount(PREVIOUS_NET_CURRENT_ASSETS);
            provisionForLiabilities.setPreviousAmount(PREVIOUS_PROVISION_FOR_LIABILITIES);
            totalNetAssets.setPreviousAmount(PREVIOUS_TOTAL_NET_ASSETS);
            totalAssetsLessCurrentLiabilities.setPreviousAmount(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);
        }

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);

        return balanceSheet;
    }

    private BalanceSheet mockBalanceSheetForCurrentPeriod() {
        return mockBalanceSheetWithPeriods(true, false);
    }

    private BalanceSheet mockBalanceSheetForPreviousPeriod() {
        return mockBalanceSheetWithPeriods(false, true);
    }

    private BalanceSheetApi mockBalanceSheetApiForCurrentPeriod() {

        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
        otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME);
        otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(CURRENT_CREDITORS_AFTER_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(CURRENT_ACCRUALS_AND_DEFERRED_INCOME);
        otherLiabilitiesOrAssetsApi.setNetCurrentAssets(CURRENT_NET_CURRENT_ASSETS);
        otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(CURRENT_PROVISION_FOR_LIABILITIES);
        otherLiabilitiesOrAssetsApi.setTotalNetAssets(CURRENT_TOTAL_NET_ASSETS);
        otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setOtherLiabilitiesOrAssetsApi(otherLiabilitiesOrAssetsApi);

        return balanceSheetApi;
    }

    private BalanceSheetApi mockBalanceSheetApiForPreviousPeriod() {

        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
        otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME);
        otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(PREVIOUS_CREDITORS_AFTER_ONE_YEAR);
        otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME);
        otherLiabilitiesOrAssetsApi.setNetCurrentAssets(PREVIOUS_NET_CURRENT_ASSETS);
        otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(PREVIOUS_PROVISION_FOR_LIABILITIES);
        otherLiabilitiesOrAssetsApi.setTotalNetAssets(PREVIOUS_TOTAL_NET_ASSETS);
        otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setOtherLiabilitiesOrAssetsApi(otherLiabilitiesOrAssetsApi);

        return balanceSheetApi;
    }

    private void assertBothPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNestedModelsNotNull(balanceSheet);
        assertCurrentPeriodFieldsNotNull(balanceSheet);
        assertPreviousPeriodFieldsNotNull(balanceSheet);
    }

    private void assertNestedModelsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities());
    }

    private void assertCurrentPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getCurrentAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getCurrentAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getCurrentAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getCurrentAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getCurrentAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getCurrentAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getCurrentAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getCurrentAmount());
    }

    private void assertPreviousPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getPreviousAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getPreviousAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getPreviousAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getPreviousAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getPreviousAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getPreviousAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getPreviousAmount());
        assertNotNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getPreviousAmount());

    }

    private void assertCurrentPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getCurrentAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getCurrentAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getCurrentAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getCurrentAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getCurrentAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getCurrentAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getCurrentAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getCurrentAmount());
    }

    private void assertPreviousPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getPreviousAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getPreviousAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getPreviousAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getPreviousAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getPreviousAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getPreviousAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getPreviousAmount());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getPreviousAmount());
    }

    private void assertCurrentPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getCurrentAmount());
        assertEquals(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getCurrentAmount());
        assertEquals(CURRENT_CREDITORS_AFTER_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getCurrentAmount());
        assertEquals(CURRENT_ACCRUALS_AND_DEFERRED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getCurrentAmount());
        assertEquals(CURRENT_NET_CURRENT_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getCurrentAmount());
        assertEquals(CURRENT_PROVISION_FOR_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_NET_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getCurrentAmount());
    }

    private void assertPreviousPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome().getPreviousAmount());
        assertEquals(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear().getPreviousAmount());
        assertEquals(PREVIOUS_CREDITORS_AFTER_ONE_YEAR, balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear().getPreviousAmount());
        assertEquals(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME, balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome().getPreviousAmount());
        assertEquals(PREVIOUS_NET_CURRENT_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets().getPreviousAmount());
        assertEquals(PREVIOUS_PROVISION_FOR_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_NET_ASSETS, balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities().getPreviousAmount());
    }

    private void assertApiModelFieldsNotNull(BalanceSheetApi balanceSheetApi) {
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getPrepaymentsAndAccruedIncome());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getCreditorsDueWithinOneYear());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getCreditorsAfterOneYear());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getAccrualsAndDeferredIncome());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getNetCurrentAssets());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getProvisionForLiabilities());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getTotalNetAssets());
        assertNotNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getTotalAssetsLessCurrentLiabilities());
    }

    private void assertCurrentPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(CURRENT_PREPAYMENTS_AND_ACCRUED_INCOME, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getPrepaymentsAndAccruedIncome());
        assertEquals(CURRENT_CREDITORS_DUE_WITHIN_ONE_YEAR, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getCreditorsDueWithinOneYear());
        assertEquals(CURRENT_CREDITORS_AFTER_ONE_YEAR, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getCreditorsAfterOneYear());
        assertEquals(CURRENT_ACCRUALS_AND_DEFERRED_INCOME, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getAccrualsAndDeferredIncome());
        assertEquals(CURRENT_NET_CURRENT_ASSETS, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getNetCurrentAssets());
        assertEquals(CURRENT_PROVISION_FOR_LIABILITIES, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getProvisionForLiabilities());
        assertEquals(CURRENT_TOTAL_NET_ASSETS, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getTotalNetAssets());
        assertEquals(CURRENT_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getTotalAssetsLessCurrentLiabilities());
    }

    private void assertPreviousPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(PREVIOUS_PREPAYMENTS_AND_ACCRUED_INCOME, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getPrepaymentsAndAccruedIncome());
        assertEquals(PREVIOUS_CREDITORS_DUE_WITHIN_ONE_YEAR, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getCreditorsDueWithinOneYear());
        assertEquals(PREVIOUS_CREDITORS_AFTER_ONE_YEAR, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getCreditorsAfterOneYear());
        assertEquals(PREVIOUS_ACCRUALS_AND_DEFERRED_INCOME, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getAccrualsAndDeferredIncome());
        assertEquals(PREVIOUS_NET_CURRENT_ASSETS, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getNetCurrentAssets());
        assertEquals(PREVIOUS_PROVISION_FOR_LIABILITIES, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getProvisionForLiabilities());
        assertEquals(PREVIOUS_TOTAL_NET_ASSETS, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getTotalNetAssets());
        assertEquals(PREVIOUS_TOTAL_ASSETS_LESS_CURRENT_LIABILITIES, balanceSheetApi.getOtherLiabilitiesOrAssetsApi().getTotalAssetsLessCurrentLiabilities());
    }
}