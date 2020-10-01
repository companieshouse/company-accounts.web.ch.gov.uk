package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.FixedAssetsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FixedAssetsTransformerImplTests {

    private static final Long CURRENT_INTANGIBLE_ASSETS = 1L;
    private static final Long CURRENT_TANGIBLE_ASSETS = 1L;
    private static final Long CURRENT_FIXED_ASSETS_INVESTMENTS = 1L;
    private static final Long CURRENT_TOTAL_FIXED_ASSETS = 2L;

    private static final Long PREVIOUS_INTANGIBLE_ASSETS = 10L;
    private static final Long PREVIOUS_TANGIBLE_ASSETS = 10L;
    private static final Long PREVIOUS_FIXED_ASSETS_INVESTMENTS = 1L;
    private static final Long PREVIOUS_TOTAL_FIXED_ASSETS = 20L;

    private final Transformer transformer = new FixedAssetsTransformerImpl();

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

        assertNonFixedAssetsFieldsNull(balanceSheet);

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

        assertNonFixedAssetsFieldsNull(balanceSheet);

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

        assertNonFixedAssetsFieldsNull(balanceSheet);

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

        assertNonFixedAssetsFieldsNull(balanceSheet);

        assertCurrentPeriodValuesCorrect(balanceSheet);
        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Current period values added to balance sheet API model when present")
    void currentPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
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

        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Previous period values added to balance sheet API model when present")
    void previousPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForPreviousPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
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

        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    private BalanceSheet mockBalanceSheetWithPeriods(Boolean currentPeriod, Boolean previousPeriod) {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        TangibleAssets tangibleAssets = new TangibleAssets();
        FixedInvestments fixedInvestments = new FixedInvestments();

        if (currentPeriod) {
            intangibleAssets.setCurrentAmount(CURRENT_INTANGIBLE_ASSETS);
            tangibleAssets.setCurrentAmount(CURRENT_TANGIBLE_ASSETS);
            fixedInvestments.setCurrentAmount(CURRENT_FIXED_ASSETS_INVESTMENTS);
        }

        if (previousPeriod) {
            intangibleAssets.setPreviousAmount(PREVIOUS_INTANGIBLE_ASSETS);
            tangibleAssets.setPreviousAmount(PREVIOUS_TANGIBLE_ASSETS);
            fixedInvestments.setPreviousAmount(PREVIOUS_FIXED_ASSETS_INVESTMENTS);
        }

        FixedAssets fixedAssets = new FixedAssets();
        fixedAssets.setIntangibleAssets(intangibleAssets);
        fixedAssets.setTangibleAssets(tangibleAssets);
        fixedAssets.setInvestments(fixedInvestments);

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
        fixedAssetsApi.setIntangible(CURRENT_INTANGIBLE_ASSETS);
        fixedAssetsApi.setTangible(CURRENT_TANGIBLE_ASSETS);
        fixedAssetsApi.setInvestments(CURRENT_FIXED_ASSETS_INVESTMENTS);
        fixedAssetsApi.setTotal(CURRENT_TOTAL_FIXED_ASSETS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setFixedAssets(fixedAssetsApi);

        return balanceSheetApi;
    }

    private BalanceSheetApi mockBalanceSheetApiForPreviousPeriod() {

        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setIntangible(PREVIOUS_INTANGIBLE_ASSETS);
        fixedAssetsApi.setTangible(PREVIOUS_TANGIBLE_ASSETS);
        fixedAssetsApi.setInvestments(PREVIOUS_FIXED_ASSETS_INVESTMENTS);
        fixedAssetsApi.setTotal(PREVIOUS_TOTAL_FIXED_ASSETS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setFixedAssets(fixedAssetsApi);

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
        assertNotNull(balanceSheet.getFixedAssets().getIntangibleAssets().getCurrentAmount());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertNotNull(balanceSheet.getFixedAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getFixedAssets().getIntangibleAssets().getPreviousAmount());
        assertNotNull(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertNotNull(balanceSheet.getFixedAssets().getPreviousTotal());
    }

    private void assertCurrentPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getFixedAssets().getIntangibleAssets().getCurrentAmount());
        assertNull(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertNull(balanceSheet.getFixedAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getFixedAssets().getIntangibleAssets().getPreviousAmount());
        assertNull(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertNull(balanceSheet.getFixedAssets().getPreviousTotal());
    }

    private void assertCurrentPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(CURRENT_INTANGIBLE_ASSETS, balanceSheet.getFixedAssets().getIntangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_TANGIBLE_ASSETS, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_FIXED_ASSETS, balanceSheet.getFixedAssets().getCurrentTotal());
    }

    private void assertPreviousPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(PREVIOUS_INTANGIBLE_ASSETS, balanceSheet.getFixedAssets().getIntangibleAssets().getPreviousAmount());
        assertEquals(PREVIOUS_TANGIBLE_ASSETS, balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_FIXED_ASSETS, balanceSheet.getFixedAssets().getPreviousTotal());
    }

    private void assertApiModelFieldsNotNull(BalanceSheetApi balanceSheetApi) {
        assertNotNull(balanceSheetApi.getFixedAssets());
        assertNotNull(balanceSheetApi.getFixedAssets().getTotal());
        assertNotNull(balanceSheetApi.getFixedAssets().getTangible());
        assertNotNull(balanceSheetApi.getFixedAssets().getIntangible());
    }

    private void assertCurrentPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(CURRENT_INTANGIBLE_ASSETS, balanceSheetApi.getFixedAssets().getIntangible());
        assertEquals(CURRENT_TANGIBLE_ASSETS, balanceSheetApi.getFixedAssets().getTangible());
        assertEquals(CURRENT_TOTAL_FIXED_ASSETS, balanceSheetApi.getFixedAssets().getTotal());
    }

    private void assertPreviousPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(PREVIOUS_INTANGIBLE_ASSETS, balanceSheetApi.getFixedAssets().getIntangible());
        assertEquals(PREVIOUS_TANGIBLE_ASSETS, balanceSheetApi.getFixedAssets().getTangible());
        assertEquals(PREVIOUS_TOTAL_FIXED_ASSETS, balanceSheetApi.getFixedAssets().getTotal());
    }

    private void assertNonFixedAssetsFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());
    }
}
