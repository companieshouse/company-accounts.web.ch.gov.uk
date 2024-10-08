package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.CalledUpShareCapitalNotPaidTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalledUpShareCapitalNotPaidTransformerImplTests {

    private static final Long CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID = 1L;
    private static final Long PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID = 10L;

    private final Transformer transformer = new CalledUpShareCapitalNotPaidTransformerImpl();

    @Test
    @DisplayName("Current period value added to balance sheet web model")
    void currentPeriodValueAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
    }

    @Test
    @DisplayName("Current period value added to balance sheet web model does not affect an existing previous period value")
    void currentPeriodValueAddedToWebModelDoesNotAffectPreviousPeriodValue() {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setPreviousAmount(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        transformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
    }

    @Test
    @DisplayName("Previous period value added to balance sheet web model")
    void previousPeriodValueAddedToWebModel() {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        BalanceSheet balanceSheet = new BalanceSheet();

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
        assertNull(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
    }

    @Test
    @DisplayName("Previous period value added to balance sheet web model does not affect an existing current period value")
    void previousPeriodValueAddedToWebModelDoesNotAffectCurrentPeriodValue() {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();
        balanceSheetApi.setCalledUpShareCapitalNotPaid(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        transformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);

        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
    }

    @Test
    @DisplayName("Current period value added to balance sheet API model when present")
    void currentPeriodValueAddedToApiModel() {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNotNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
        assertEquals(CURRENT_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }


    @Test
    @DisplayName("Current period value not added to balance sheet API model when absent")
    void currentPeriodValueNotAddedToApiModel() {

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(new CalledUpShareCapitalNotPaid());

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Previous period value added to balance sheet API model when present")
    void previousPeriodValueAddedToApiModel() {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setPreviousAmount(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID);

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNotNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
        assertEquals(PREVIOUS_CALLED_UP_SHARE_CAPITAL_NOT_PAID,
                balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    @Test
    @DisplayName("Previous period value not added to balance sheet API model when absent")
    void previousPeriodValueNotAddedToApiModel() {

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(new CalledUpShareCapitalNotPaid());

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        transformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        assertNull(balanceSheetApi.getFixedAssets());
        assertNull(balanceSheetApi.getCurrentAssets());
        assertNull(balanceSheetApi.getOtherLiabilitiesOrAssets());
        assertNull(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }
}
