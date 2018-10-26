package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.BalanceSheetTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceSheetTransformerTests {

    private static final Long CURRENT_CALLED_UP_SHARE_CAPITAL = 1L;
    private static final Long CURRENT_TANGIBLE = 5L;
    private static final Long CURRENT_FIXED_ASSETS_TOTAL = 5L;

    private static final Long PREVIOUS_CALLED_UP_SHARE_CAPITAL = 2L;
    private static final Long PREVIOUS_TANGIBLE = 10L;
    private static final Long PREVIOUS_FIXED_ASSETS_TOTAL = 10L;

    private BalanceSheetTransformer transformer = new BalanceSheetTransformerImpl();

    @Test
    @DisplayName("First Year Filer - Get Balance Sheet")
    void getFirstYearFilerBalanceSheet() {

        CurrentPeriodApi currentPeriodApi = createMockCurrentPeriod();

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi, null);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertEquals(CURRENT_TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getTotalCurrentFixedAssets());
    }


    @Test
    @DisplayName("Get Current Period")
    void getCurrentPeriodFixedAssets() {

        BalanceSheet balanceSheet = createMockBalanceSheetWithCurrentPeriod();

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        assertNotNull(currentPeriod);
        assertNotNull(currentPeriod.getBalanceSheetApi());

        // Fixed assets
        assertNotNull(currentPeriod.getBalanceSheetApi().getFixedAssetsApi());
        assertEquals(CURRENT_TANGIBLE, currentPeriod.getBalanceSheetApi().getFixedAssetsApi().getTangible());
        assertEquals(CURRENT_FIXED_ASSETS_TOTAL, currentPeriod.getBalanceSheetApi().getFixedAssetsApi().getTotal());

        // Called up share capital not paid
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, currentPeriod.getBalanceSheetApi().getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Get Previous Period")
    void getPeriodPeriodFixedAssets() {

        BalanceSheet balanceSheet = createMockBalanceSheetWithPreviousPeriod();

        PreviousPeriodApi previousPeriodApi = transformer.getPreviousPeriod(balanceSheet);

        assertNotNull(previousPeriodApi);
        assertNotNull(previousPeriodApi.getBalanceSheet());

        assertNotNull(previousPeriodApi.getBalanceSheet().getFixedAssetsApi());
        assertEquals(PREVIOUS_TANGIBLE, previousPeriodApi.getBalanceSheet().getFixedAssetsApi().getTangible());
        assertEquals(PREVIOUS_FIXED_ASSETS_TOTAL, previousPeriodApi.getBalanceSheet().getFixedAssetsApi().getTotal());

        // Called up share capital not paid
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL, previousPeriodApi.getBalanceSheet().getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Multiple Year Filer - Get Balance Sheet")
    void getMultipleYearFilerBalanceSheet() {

        CurrentPeriodApi currentPeriodApi = createMockCurrentPeriod();
        PreviousPeriodApi previousPeriodApi = createMockPreviousPeriod();


        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriodApi, previousPeriodApi);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertEquals(CURRENT_TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(CURRENT_FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getTotalCurrentFixedAssets());
    }

    private BalanceSheet createMockBalanceSheetWithCurrentPeriod() {

        BalanceSheet balanceSheet = new BalanceSheet();

        // Fixed assets
        FixedAssets fixedAssets = new FixedAssets();
        TangibleAssets tangible = new TangibleAssets();
        tangible.setCurrentAmount(CURRENT_TANGIBLE);
        fixedAssets.setTangibleAssets(tangible);
        fixedAssets.setTotalCurrentFixedAssets(CURRENT_FIXED_ASSETS_TOTAL);
        balanceSheet.setFixedAssets(fixedAssets);

        // Called up share capital not paid
        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(CURRENT_CALLED_UP_SHARE_CAPITAL);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        return balanceSheet;
    }

    private BalanceSheet createMockBalanceSheetWithPreviousPeriod() {

        BalanceSheet balanceSheet = new BalanceSheet();

        // Fixed assets
        FixedAssets fixedAssets = new FixedAssets();
        TangibleAssets tangible = new TangibleAssets();
        tangible.setPreviousAmount(PREVIOUS_TANGIBLE);
        fixedAssets.setTangibleAssets(tangible);
        fixedAssets.setTotalPreviousFixedAssets(PREVIOUS_FIXED_ASSETS_TOTAL);
        balanceSheet.setFixedAssets(fixedAssets);

        // Called up share capital not paid
        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setPreviousAmount(PREVIOUS_CALLED_UP_SHARE_CAPITAL);
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        return balanceSheet;
    }

    private CurrentPeriodApi createMockCurrentPeriod() {
        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(CURRENT_CALLED_UP_SHARE_CAPITAL);

        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setTangible(CURRENT_TANGIBLE);
        fixedAssetsApi.setTotal(CURRENT_FIXED_ASSETS_TOTAL);

        balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        CurrentPeriodApi currentPeriodApi = new CurrentPeriodApi();
        currentPeriodApi.setBalanceSheetApi(balanceSheetApi);

        return currentPeriodApi;
    }

    private PreviousPeriodApi createMockPreviousPeriod() {
        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(PREVIOUS_CALLED_UP_SHARE_CAPITAL);

        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setTangible(PREVIOUS_TANGIBLE);
        fixedAssetsApi.setTotal(PREVIOUS_FIXED_ASSETS_TOTAL);

        balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        previousPeriodApi.setBalanceSheet(balanceSheetApi);

        return previousPeriodApi;
    }
}
