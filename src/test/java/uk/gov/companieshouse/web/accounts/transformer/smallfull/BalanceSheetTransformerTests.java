package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.BalanceSheetTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceSheetTransformerTests {

    private Long CALLED_UP_SHARE_CAPITAL = Long.valueOf("1");
    private Long TANGIBLE = Long.valueOf("5");
    private Long FIXED_ASSETS_TOTAL = Long.valueOf("5");

    private BalanceSheetTransformer transformer = new BalanceSheetTransformerImpl();

    @Test
    @DisplayName("Get Balance Sheet - Assert Called Up Share Capital is Correct")
    void getBalanceSheetCalledUpShareCapital() {

        BalanceSheetApi currentPeriodBalanceSheetApi = new BalanceSheetApi();
        currentPeriodBalanceSheetApi.setCalledUpShareCapitalNotPaid(CALLED_UP_SHARE_CAPITAL);

        FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
        fixedAssetsApi.setTangibleApi(TANGIBLE);
        fixedAssetsApi.setTotal(FIXED_ASSETS_TOTAL);

        currentPeriodBalanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();
        currentPeriod.setBalanceSheetApi(currentPeriodBalanceSheetApi);

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriod);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertEquals(CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertEquals(TANGIBLE, balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
        assertEquals(FIXED_ASSETS_TOTAL, balanceSheet.getFixedAssets().getTotalCurrentFixedAssets());
    }

    @Test
    @DisplayName("Get Current Period - Assert Called Up Share Capital is Correct")
    void getCurrentPeriodCalledUpShareCapital() {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(CALLED_UP_SHARE_CAPITAL);

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        assertNotNull(currentPeriod);
        assertNotNull(currentPeriod.getBalanceSheetApi());
        assertEquals(CALLED_UP_SHARE_CAPITAL, currentPeriod.getBalanceSheetApi().getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Get Current Period - Assert Fixed Assets Are Correct")
    void getCurrentPeriodFixedAssets() {

        FixedAssets fixedAssets = new FixedAssets();
        TangibleAssets tangible = new TangibleAssets();
        tangible.setCurrentAmount(TANGIBLE);
        fixedAssets.setTangibleAssets(tangible);
        fixedAssets.setTotalCurrentFixedAssets(FIXED_ASSETS_TOTAL);

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setFixedAssets(fixedAssets);

        CurrentPeriodApi currentPeriod = transformer.getCurrentPeriod(balanceSheet);

        assertNotNull(currentPeriod);
        assertNotNull(currentPeriod.getBalanceSheetApi());
        assertEquals(TANGIBLE, currentPeriod.getBalanceSheetApi().getFixedAssetsApi().getTangibleApi());
        assertEquals(FIXED_ASSETS_TOTAL, currentPeriod.getBalanceSheetApi().getFixedAssetsApi().getTotal());
    }
}
