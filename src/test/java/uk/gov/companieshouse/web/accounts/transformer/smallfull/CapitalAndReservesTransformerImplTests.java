package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CapitalAndReservesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapital;
import uk.gov.companieshouse.web.accounts.model.smallfull.CapitalAndReserves;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherReserves;
import uk.gov.companieshouse.web.accounts.model.smallfull.ProfitAndLossAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.SharePremiumAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalShareholdersFunds;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CapitalAndReservesTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CapitalAndReservesTransformerImplTests {

    private static final Long CURRENT_CALLED_UP_SHARE_CAPITAL = 1L;
    private static final Long CURRENT_SHARE_PREMIUM_ACCOUNT = 2L;
    private static final Long CURRENT_OTHER_RESERVES = 3L;
    private static final Long CURRENT_PROFIT_AND_LOSS_ACCOUNT = 4L;
    private static final Long CURRENT_TOTAL_SHAREHOLDERS_FUNDS= 5L;

    private static final Long PREVIOUS_CALLED_UP_SHARE_CAPITAL = 10L;
    private static final Long PREVIOUS_SHARE_PREMIUM_ACCOUNT = 20L;
    private static final Long PREVIOUS_OTHER_RESERVES = 30L;
    private static final Long PREVIOUS_PROFIT_AND_LOSS_ACCOUNT = 40L;
    private static final Long PREVIOUS_TOTAL_SHAREHOLDERS_FUNDS= 50L;

    private Transformer transformer = new CapitalAndReservesTransformerImpl();

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

        assertNonCapitalAndReservesFieldsNull(balanceSheet);

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

        assertNonCapitalAndReservesFieldsNull(balanceSheet);

        assertCurrentPeriodValuesCorrect(balanceSheet);
        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Previous period values added to balance sheet web model")
    void previousPeriodValuesAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForPreviousPeriod();

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCapitalAndReserves());

        assertNestedModelsNotNull(balanceSheet);

        assertPreviousPeriodFieldsNotNull(balanceSheet);
        assertCurrentPeriodFieldsNull(balanceSheet);

        assertNonCapitalAndReservesFieldsNull(balanceSheet);

        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Previous period values added to balance sheet web model without affecting current period values")
    void previousPeriodValuesAddedToWebModelWithoutAffectingCurrentPeriodValues() {

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        BalanceSheetApi balanceSheetApi = mockBalanceSheetApiForPreviousPeriod();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCapitalAndReserves());

        assertBothPeriodFieldsNotNull(balanceSheet);

        assertNonCapitalAndReservesFieldsNull(balanceSheet);

        assertCurrentPeriodValuesCorrect(balanceSheet);
        assertPreviousPeriodValuesCorrect(balanceSheet);
    }

    @Test
    @DisplayName("Current period value added to balance sheet API model when present")
    void currentPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForCurrentPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertCurrentPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Current period value always added to balance sheet API model")
    void currentPeriodValueAlwaysAddedToApiModel() {

        CapitalAndReserves capitalAndReserves = new CapitalAndReserves();
        capitalAndReserves.setCalledUpShareCapital(new CalledUpShareCapital());
        capitalAndReserves.setOtherReserves(new OtherReserves());
        capitalAndReserves.setSharePremiumAccount(new SharePremiumAccount());
        capitalAndReserves.setProfitAndLossAccount(new ProfitAndLossAccount());
        capitalAndReserves.setTotalShareholdersFunds(new TotalShareholdersFunds());

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCapitalAndReserves(capitalAndReserves);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertNotNull(balanceSheetApi.getCapitalAndReserves());
    }

    @Test
    @DisplayName("Previous period value added to balance sheet API model when present")
    void previousPeriodValueAddedToApiModel() {

        BalanceSheet balanceSheet = mockBalanceSheetForPreviousPeriod();

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertApiModelFieldsNotNull(balanceSheetApi);

        assertPreviousPeriodApiModelValuesCorrect(balanceSheetApi);
    }

    @Test
    @DisplayName("Previous period value always added to balance sheet API model")
    void previousPeriodValueAlwaysAddedToApiModel() {

        CapitalAndReserves capitalAndReserves = new CapitalAndReserves();
        capitalAndReserves.setCalledUpShareCapital(new CalledUpShareCapital());
        capitalAndReserves.setOtherReserves(new OtherReserves());
        capitalAndReserves.setSharePremiumAccount(new SharePremiumAccount());
        capitalAndReserves.setProfitAndLossAccount(new ProfitAndLossAccount());
        capitalAndReserves.setTotalShareholdersFunds(new TotalShareholdersFunds());

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCapitalAndReserves(capitalAndReserves);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());

        assertNotNull(balanceSheetApi.getCapitalAndReserves());
    }

    private BalanceSheet mockBalanceSheetWithPeriods(Boolean currentPeriod, Boolean previousPeriod) {

        CalledUpShareCapital calledUpShareCapital = new CalledUpShareCapital();
        OtherReserves otherReserves = new OtherReserves();
        SharePremiumAccount sharePremiumAccount = new SharePremiumAccount();
        ProfitAndLossAccount profitAndLossAccount = new ProfitAndLossAccount();
        TotalShareholdersFunds totalShareholdersFunds = new TotalShareholdersFunds();

        CapitalAndReserves capitalAndReserves = new CapitalAndReserves();
        capitalAndReserves.setCalledUpShareCapital(calledUpShareCapital);
        capitalAndReserves.setOtherReserves(otherReserves);
        capitalAndReserves.setSharePremiumAccount(sharePremiumAccount);
        capitalAndReserves.setProfitAndLossAccount(profitAndLossAccount);
        capitalAndReserves.setTotalShareholdersFunds(totalShareholdersFunds);

        if (currentPeriod) {
            calledUpShareCapital.setCurrentAmount(CURRENT_CALLED_UP_SHARE_CAPITAL);
            otherReserves.setCurrentAmount(CURRENT_OTHER_RESERVES);
            sharePremiumAccount.setCurrentAmount(CURRENT_SHARE_PREMIUM_ACCOUNT);
            profitAndLossAccount.setCurrentAmount(CURRENT_PROFIT_AND_LOSS_ACCOUNT);
            totalShareholdersFunds.setCurrentAmount(CURRENT_TOTAL_SHAREHOLDERS_FUNDS);
        }

        if (previousPeriod) {
            calledUpShareCapital.setPreviousAmount(PREVIOUS_CALLED_UP_SHARE_CAPITAL);
            otherReserves.setPreviousAmount(PREVIOUS_OTHER_RESERVES);
            sharePremiumAccount.setPreviousAmount(PREVIOUS_SHARE_PREMIUM_ACCOUNT);
            profitAndLossAccount.setPreviousAmount(PREVIOUS_PROFIT_AND_LOSS_ACCOUNT);
            totalShareholdersFunds.setPreviousAmount(PREVIOUS_TOTAL_SHAREHOLDERS_FUNDS);
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
        capitalAndReservesApi.setCalledUpShareCapital(CURRENT_CALLED_UP_SHARE_CAPITAL);
        capitalAndReservesApi.setOtherReserves(CURRENT_OTHER_RESERVES);
        capitalAndReservesApi.setSharePremiumAccount(CURRENT_SHARE_PREMIUM_ACCOUNT);
        capitalAndReservesApi.setProfitAndLoss(CURRENT_PROFIT_AND_LOSS_ACCOUNT);
        capitalAndReservesApi.setTotalShareholdersFunds(CURRENT_TOTAL_SHAREHOLDERS_FUNDS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCapitalAndReserves(capitalAndReservesApi);

        return balanceSheetApi;
    }

    private BalanceSheetApi mockBalanceSheetApiForPreviousPeriod() {

        CapitalAndReservesApi capitalAndReservesApi = new CapitalAndReservesApi();
        capitalAndReservesApi.setCalledUpShareCapital(PREVIOUS_CALLED_UP_SHARE_CAPITAL);
        capitalAndReservesApi.setOtherReserves(PREVIOUS_OTHER_RESERVES);
        capitalAndReservesApi.setSharePremiumAccount(PREVIOUS_SHARE_PREMIUM_ACCOUNT);
        capitalAndReservesApi.setProfitAndLoss(PREVIOUS_PROFIT_AND_LOSS_ACCOUNT);
        capitalAndReservesApi.setTotalShareholdersFunds(PREVIOUS_TOTAL_SHAREHOLDERS_FUNDS);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCapitalAndReserves(capitalAndReservesApi);

        return balanceSheetApi;
    }

    private void assertBothPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNestedModelsNotNull(balanceSheet);
        assertCurrentPeriodFieldsNotNull(balanceSheet);
        assertPreviousPeriodFieldsNotNull(balanceSheet);
    }

    private void assertNestedModelsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital());
        assertNotNull(balanceSheet.getCapitalAndReserves().getSharePremiumAccount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getOtherReserves());
        assertNotNull(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds());
    }

    private void assertCurrentPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getCurrentAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getCurrentAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getOtherReserves().getCurrentAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getCurrentAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getCurrentAmount());
    }

    private void assertPreviousPeriodFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getPreviousAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getPreviousAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getOtherReserves().getPreviousAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getPreviousAmount());
        assertNull(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getPreviousAmount());
    }

    private void assertCurrentPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getOtherReserves().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getCurrentAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getCurrentAmount());
    }

    private void assertPreviousPeriodFieldsNotNull(BalanceSheet balanceSheet) {
        assertNotNull(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getOtherReserves().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getPreviousAmount());
        assertNotNull(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getPreviousAmount());
    }

    private void assertCurrentPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getCurrentAmount());
        assertEquals(CURRENT_SHARE_PREMIUM_ACCOUNT, balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getCurrentAmount());
        assertEquals(CURRENT_OTHER_RESERVES, balanceSheet.getCapitalAndReserves().getOtherReserves().getCurrentAmount());
        assertEquals(CURRENT_PROFIT_AND_LOSS_ACCOUNT, balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getCurrentAmount());
        assertEquals(CURRENT_TOTAL_SHAREHOLDERS_FUNDS, balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getCurrentAmount());
    }

    private void assertPreviousPeriodValuesCorrect(BalanceSheet balanceSheet) {
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL, balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getPreviousAmount());
        assertEquals(PREVIOUS_SHARE_PREMIUM_ACCOUNT, balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getPreviousAmount());
        assertEquals(PREVIOUS_OTHER_RESERVES, balanceSheet.getCapitalAndReserves().getOtherReserves().getPreviousAmount());
        assertEquals(PREVIOUS_PROFIT_AND_LOSS_ACCOUNT, balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getPreviousAmount());
        assertEquals(PREVIOUS_TOTAL_SHAREHOLDERS_FUNDS, balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getPreviousAmount());
    }

    private void assertApiModelFieldsNotNull(BalanceSheetApi balanceSheetApi) {
        assertNotNull(balanceSheetApi.getCapitalAndReserves());
        assertNotNull(balanceSheetApi.getCapitalAndReserves().getCalledUpShareCapital());
        assertNotNull(balanceSheetApi.getCapitalAndReserves().getSharePremiumAccount());
        assertNotNull(balanceSheetApi.getCapitalAndReserves().getOtherReserves());
        assertNotNull(balanceSheetApi.getCapitalAndReserves().getProfitAndLoss());
        assertNotNull(balanceSheetApi.getCapitalAndReserves().getTotalShareholdersFunds());
    }

    private void assertCurrentPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL, balanceSheetApi.getCapitalAndReserves().getCalledUpShareCapital());
        assertEquals(CURRENT_SHARE_PREMIUM_ACCOUNT, balanceSheetApi.getCapitalAndReserves().getSharePremiumAccount());
        assertEquals(CURRENT_OTHER_RESERVES, balanceSheetApi.getCapitalAndReserves().getOtherReserves());
        assertEquals(CURRENT_PROFIT_AND_LOSS_ACCOUNT, balanceSheetApi.getCapitalAndReserves().getProfitAndLoss());
        assertEquals(CURRENT_TOTAL_SHAREHOLDERS_FUNDS, balanceSheetApi.getCapitalAndReserves().getTotalShareholdersFunds());
    }

    private void assertPreviousPeriodApiModelValuesCorrect(BalanceSheetApi balanceSheetApi) {
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL, balanceSheetApi.getCapitalAndReserves().getCalledUpShareCapital());
        assertEquals(PREVIOUS_SHARE_PREMIUM_ACCOUNT, balanceSheetApi.getCapitalAndReserves().getSharePremiumAccount());
        assertEquals(PREVIOUS_OTHER_RESERVES, balanceSheetApi.getCapitalAndReserves().getOtherReserves());
        assertEquals(PREVIOUS_PROFIT_AND_LOSS_ACCOUNT, balanceSheetApi.getCapitalAndReserves().getProfitAndLoss());
        assertEquals(PREVIOUS_TOTAL_SHAREHOLDERS_FUNDS, balanceSheetApi.getCapitalAndReserves().getTotalShareholdersFunds());
    }

    private void assertNonCapitalAndReservesFieldsNull(BalanceSheet balanceSheet) {
        assertNull(balanceSheet.getFixedAssets());
        assertNull(balanceSheet.getCurrentAssets());
        assertNull(balanceSheet.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNull(balanceSheet.getBalanceSheetHeadings());
    }
}
