package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.FixedAssetsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FixedAssetsTransformerImplTests {

    private static final Long CURRENT_TANGIBLE_ASSETS = 1L;
    private static final Long CURRENT_TOTAL_FIXED_ASSETS = 2L;

    private static final Long PREVIOUS_TANGIBLE_ASSETS = 10L;
    private static final Long PREVIOUS_TOTAL_FIXED_ASSETS = 20L;

    private Transformer transformer = new FixedAssetsTransformerImpl();

    @Test
    @DisplayName("Current period values added to balance sheet web model")
    void currentPeriodValueAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForCurrentPeriod();

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getFixedAssets());

        assertNestedModelsNotNull(balanceSheet);

        assertCurrentPeriodFieldsNotNull(balanceSheet);
        assertPreviousPeriodFieldsNull(balanceSheet);

        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets());
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

        assertNotNull(balanceSheet.getFixedAssets());

        assertBothPeriodFieldsNotNull(balanceSheet);

        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets());
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

        assertNotNull(balanceSheet.getFixedAssets());

        assertNestedModelsNotNull(balanceSheet);

        assertCurrentPeriodFieldsNull(balanceSheet);
        assertPreviousPeriodFieldsNotNull(balanceSheet);

        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());

        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Previous period values added to balance sheet web model without affecting current period values")
    void previousPeriodValuesAddedToWebModelWithoutAffectingCurrentPeriodValues() {

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForPreviousPeriod();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getFixedAssets());

        assertBothPeriodFieldsNotNull(balanceSheet);

        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());

        assertCurrentPeriodValuesCorrect(balanceSheet);
        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Current period values added to balance sheet API model when present")
    void currentPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertCurrentPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Current period values not added to balance sheet API model when absent")
    void currentPeriodValueNotAddedToApiModel() {

        FixedAssets fixedAssets = new FixedAssets();
        fixedAssets.setTangibleAssets(new TangibleAssets());

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setFixedAssets(fixedAssets);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssetsApi());
        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Previous period values added to balance sheet API model when present")
    void previousPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForPreviousPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertPreviousPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Previous period values not added to balance sheet API model when absent")
    void previousPeriodValueNotAddedToApiModel() {

        FixedAssets fixedAssets = new FixedAssets();
        fixedAssets.setTangibleAssets(new TangibleAssets());

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setFixedAssets(fixedAssets);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssetsApi());
        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    private BalanceSheet mockBalanceSheetWithPeriods(Boolean currentPeriod, Boolean previousPeriod) {

        TangibleAssets tangibleAssets = new TangibleAssets();

        if (currentPeriod) {
            tangibleAssets.setCurrentAmount(CURRENT_TANGIBLE_ASSETS);
        }

        if (previousPeriod) {
            tangibleAssets.setPreviousAmount(PREVIOUS_TANGIBLE_ASSETS);
        }

        FixedAssets fixedAssets = new FixedAssets();
        fixedAssets.setTangibleAssets(tangibleAssets);

        if (currentPeriod) {
            fixedAssets.setCurrentTotal(CURRENT_TOTAL_FIXED_ASSETS);
        }

        if (previousPeriod) {
            fixedAssets.setPreviousTotal(PREVIOUS_TOTAL_FIXED_ASSETS);
        }

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setFixedAssets(fixedAssets);

        return balanceSheet;
    }

    private BalanceSheet mockBalanceSheetForCurrentPeriod() {
        return mockBalanceSheetWithPeriods(true, false);
    }

    private BalanceSheet mockBalanceSheetForPreviousPeriod() {
        return mockBalanceSheetWithPeriods(false, true);
    }

    private BalanceSheetApi mockBalanceSheetApiForCurrentPeriod() {

        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setTangibleApi(CURRENT_TANGIBLE_ASSETS);
        fixedAssetsApi.setTotal(CURRENT_TOTAL_FIXED_ASSETS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        return balanceSheetApi;
    }

    private BalanceSheetApi mockBalanceSheetApiForPreviousPeriod() {

        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setTangibleApi(PREVIOUS_TANGIBLE_ASSETS);
        fixedAssetsApi.setTotal(PREVIOUS_TOTAL_FIXED_ASSETS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        return balanceSheetApi;
    }

    private void assertBothPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNestedModelsNotNull(balanceSheet);
        assertCurrentPeriodFieldsNotNull(balanceSheet);
        assertPreviousPeriodFieldsNotNull(balanceSheet);
    }

    private void assertNestedModelsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets());
    }

    private void assertCurrentPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertNotNull(balanceSheet.getFixedAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertNotNull(balanceSheet.getFixedAssets().getPreviousTotal());
    }

    private void assertCurrentPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertNull(balanceSheet.getFixedAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertNull(balanceSheet.getFixedAssets().getPreviousTotal());
    }

    private void assertCurrentPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(CURRENT_TANGIBLE_ASSETS, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_FIXED_ASSETS, balanceSheet.getFixedAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(PREVIOUS_TANGIBLE_ASSETS, balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_FIXED_ASSETS, balanceSheet.getFixedAssets().getPreviousTotal());
    }

    private void assertApiModelFieldsNotNull(BalanceSheetApi balanceSheetApi) {
        assertNotNull(balanceSheetApi.getFixedAssetsApi());
        assertNotNull(balanceSheetApi.getFixedAssetsApi().getTotal());
        assertNotNull(balanceSheetApi.getFixedAssetsApi().getTangibleApi());
    }

    private void assertCurrentPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(CURRENT_TANGIBLE_ASSETS, balanceSheetApi.getFixedAssetsApi().getTangibleApi());
        assertEquals(CURRENT_TOTAL_FIXED_ASSETS, balanceSheetApi.getFixedAssetsApi().getTotal());
    }

    private void assertPreviousPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(PREVIOUS_TANGIBLE_ASSETS, balanceSheetApi.getFixedAssetsApi().getTangibleApi());
        assertEquals(PREVIOUS_TOTAL_FIXED_ASSETS, balanceSheetApi.getFixedAssetsApi().getTotal());
    }
}
