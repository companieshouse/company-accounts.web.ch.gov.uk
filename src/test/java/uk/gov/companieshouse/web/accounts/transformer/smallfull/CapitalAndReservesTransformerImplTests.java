package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CapitalAndReservesApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.*;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CapitalAndReservesTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CapitalAndReservesTransformerImplTests {

    private static final Long CALLED_UP_SHARE_CAPITAL = 1L;
    private static final Long PROFIT_AND_LOSS =1L;
    private static final Long OTHER_RESERVES =1L;
    private static final Long SHARE_PREMIUM_ACCOUNT = 1L;
    private static final Long TOTAL_SHAREHOLDERS_FUNDS = 4L;

    private static final Long CALLED_UP_SHARE_CAPITAL_PREVIOUS = 2L;
    private static final Long PROFIT_AND_LOSS_PREVIOUS =2L;
    private static final Long OTHER_RESERVES_PREVIOUS =2L;
    private static final Long SHARE_PREMIUM_ACCOUNT_PREVIOUS = 2L;
    private static final Long TOTAL_SHAREHOLDERS_FUNDS_PREVIOUS = 8L;

    Transformer transformer = new CapitalAndReservesTransformerImpl();

    @Test
    @DisplayName("Current period values added to balance sheet web model")
    void currentPeriodValueAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForCurrentPeriod();

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCapitalAndReserves());

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

        assertNotNull(balanceSheet.getCapitalAndReserves());

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

        assertNull(balanceSheetApi.getCurrentAssetsApi());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssetsApi());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheetApi.getFixedAssetsApi());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertCurrentPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Current period values not added to balance sheet API model when absent")
    void currentPeriodValueNotAddedToApiModel() {

        CapitalAndReserves capitalAndReserves = new CapitalAndReserves();

        capitalAndReserves.setOtherReserves(new OtherReserves());

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCapitalAndReserves(capitalAndReserves);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCapitalAndReservesApi());
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

    private BalanceSheet mockBalanceSheetWithPeriods(Boolean currentPeriod, Boolean previousPeriod) {


       CalledUpShareCapital calledUpShareCapital = new CalledUpShareCapital();
       SharePremiumAccount sharePremiumAccount = new SharePremiumAccount();
       OtherReserves otherReserves = new OtherReserves();
       ProfitAndLossAccount profitAndLossAccount = new ProfitAndLossAccount();
       TotalShareholdersFunds totalShareholdersFunds = new TotalShareholdersFunds();

       CapitalAndReserves capitalAndReserves = new CapitalAndReserves();
       capitalAndReserves.setCalledUpShareCapital(calledUpShareCapital);
       capitalAndReserves.setSharePremiumAccount(sharePremiumAccount);
       capitalAndReserves.setOtherReserves(otherReserves);
       capitalAndReserves.setTotalShareholdersFunds(totalShareholdersFunds);

        if (currentPeriod) {
            calledUpShareCapital.setCurrentAmount(CALLED_UP_SHARE_CAPITAL);
            sharePremiumAccount.setCurrentAmount(SHARE_PREMIUM_ACCOUNT);
            otherReserves.setCurrentAmount(OTHER_RESERVES);
            profitAndLossAccount.setCurrentAmount(PROFIT_AND_LOSS);
            totalShareholdersFunds.setPreviousAmount(TOTAL_SHAREHOLDERS_FUNDS);
        }

        if (previousPeriod) {
            calledUpShareCapital.setPreviousAmount(CALLED_UP_SHARE_CAPITAL_PREVIOUS);
            sharePremiumAccount.setPreviousAmount(SHARE_PREMIUM_ACCOUNT_PREVIOUS);
            otherReserves.setPreviousAmount(OTHER_RESERVES_PREVIOUS);
            profitAndLossAccount.setPreviousAmount(PROFIT_AND_LOSS_PREVIOUS);
            totalShareholdersFunds.setPreviousAmount(TOTAL_SHAREHOLDERS_FUNDS_PREVIOUS);
        }


        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCapitalAndReserves(capitalAndReserves);

        return balanceSheet;
    }

    private BalanceSheet mockBalanceSheetForCurrentPeriod() {
        return mockBalanceSheetWithPeriods(true, false);
    }

    private BalanceSheet mockBalanceSheetForPreviousPeriod() {
        return mockBalanceSheetWithPeriods(false, true);
    }

    private BalanceSheetApi mockBalanceSheetApiForCurrentPeriod() {

        CapitalAndReservesApi capitalAndReservesApi = new CapitalAndReservesApi();
        capitalAndReservesApi.setCalledUpShareCapital(CALLED_UP_SHARE_CAPITAL);
        capitalAndReservesApi.setSharePremiumAccount(SHARE_PREMIUM_ACCOUNT);
        capitalAndReservesApi.setProfitAndLoss(PROFIT_AND_LOSS);
        capitalAndReservesApi.setOtherReserves(OTHER_RESERVES);
        capitalAndReservesApi.setTotalShareholdersFunds(TOTAL_SHAREHOLDERS_FUNDS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCapitalAndReservesApi(capitalAndReservesApi);

        return balanceSheetApi;
    }

    private BalanceSheetApi mockBalanceSheetApiForPreviousPeriod() {
        CapitalAndReservesApi capitalAndReservesApi = new CapitalAndReservesApi();
        capitalAndReservesApi.setCalledUpShareCapital(CALLED_UP_SHARE_CAPITAL_PREVIOUS);
        capitalAndReservesApi.setSharePremiumAccount(SHARE_PREMIUM_ACCOUNT_PREVIOUS);
        capitalAndReservesApi.setProfitAndLoss(PROFIT_AND_LOSS_PREVIOUS);
        capitalAndReservesApi.setOtherReserves(OTHER_RESERVES_PREVIOUS);
        capitalAndReservesApi.setTotalShareholdersFunds(TOTAL_SHAREHOLDERS_FUNDS_PREVIOUS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCapitalAndReservesApi(capitalAndReservesApi);

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
        assertNotNull(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getOtherReserves().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getCurrentAmount());
    }

    private void assertPreviousPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getOtherReserves().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getPreviousAmount());
    }

    private void assertCurrentPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(CALLED_UP_SHARE_CAPITAL,balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getCurrentAmount());
        assertEquals(OTHER_RESERVES,balanceSheet.getCapitalAndReserves().getOtherReserves().getCurrentAmount());
        assertEquals(PROFIT_AND_LOSS,balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getCurrentAmount());
        assertEquals(SHARE_PREMIUM_ACCOUNT,balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getCurrentAmount());
        assertEquals(TOTAL_SHAREHOLDERS_FUNDS, balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getCurrentAmount());
    }

    private void assertPreviousPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(CALLED_UP_SHARE_CAPITAL_PREVIOUS,balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getPreviousAmount());
        assertEquals(OTHER_RESERVES_PREVIOUS,balanceSheet.getCapitalAndReserves().getOtherReserves().getPreviousAmount());
        assertEquals(PROFIT_AND_LOSS_PREVIOUS,balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getPreviousAmount());
        assertEquals(SHARE_PREMIUM_ACCOUNT_PREVIOUS,balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getPreviousAmount());
        assertEquals(TOTAL_SHAREHOLDERS_FUNDS_PREVIOUS, balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getPreviousAmount());
    }

    private void assertApiModelFieldsNotNull(BalanceSheetApi balanceSheetApi) {
        assertNotNull(balanceSheetApi.getCapitalAndReservesApi());
        assertNotNull(balanceSheetApi.getCapitalAndReservesApi().getCalledUpShareCapital());
        assertNotNull(balanceSheetApi.getCapitalAndReservesApi().getOtherReserves());
        assertNotNull(balanceSheetApi.getCapitalAndReservesApi().getProfitAndLoss());
        assertNotNull(balanceSheetApi.getCapitalAndReservesApi().getSharePremiumAccount());
        assertNotNull(balanceSheetApi.getCapitalAndReservesApi().getTotalShareholdersFunds());
    }

    private void assertCurrentPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(CALLED_UP_SHARE_CAPITAL,balanceSheetApi.getCapitalAndReservesApi().getCalledUpShareCapital());
        assertEquals(OTHER_RESERVES,balanceSheetApi.getCapitalAndReservesApi().getOtherReserves());
        assertEquals(PROFIT_AND_LOSS,balanceSheetApi.getCapitalAndReservesApi().getProfitAndLoss());
        assertEquals(SHARE_PREMIUM_ACCOUNT,balanceSheetApi.getCapitalAndReservesApi().getSharePremiumAccount());
        assertEquals(TOTAL_SHAREHOLDERS_FUNDS,balanceSheetApi.getCapitalAndReservesApi().getTotalShareholdersFunds());
    }

    private void assertPreviousPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(CALLED_UP_SHARE_CAPITAL_PREVIOUS,balanceSheetApi.getCapitalAndReservesApi().getCalledUpShareCapital());
        assertEquals(OTHER_RESERVES_PREVIOUS,balanceSheetApi.getCapitalAndReservesApi().getOtherReserves());
        assertEquals(PROFIT_AND_LOSS_PREVIOUS,balanceSheetApi.getCapitalAndReservesApi().getProfitAndLoss());
        assertEquals(SHARE_PREMIUM_ACCOUNT_PREVIOUS,balanceSheetApi.getCapitalAndReservesApi().getSharePremiumAccount());
        assertEquals(TOTAL_SHAREHOLDERS_FUNDS_PREVIOUS,balanceSheetApi.getCapitalAndReservesApi().getTotalShareholdersFunds());
    }
}
