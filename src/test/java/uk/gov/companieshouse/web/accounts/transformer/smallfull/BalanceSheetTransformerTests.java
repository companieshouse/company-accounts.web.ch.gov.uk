package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.BalanceSheetTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceSheetTransformerTests {

    private Long CALLED_UP_SHARE_CAPITAL = Long.valueOf("1");

    private BalanceSheetTransformer transformer = new BalanceSheetTransformerImpl();

    @Test
    @DisplayName("Get Balance Sheet - Assert Called Up Share Capital is Correct")
    void getBalanceSheetCalledUpShareCapital() {

        BalanceSheetApi currentPeriodBalanceSheetApi = new BalanceSheetApi();
        currentPeriodBalanceSheetApi.setCalledUpShareCapitalNotPaid(CALLED_UP_SHARE_CAPITAL);

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();
        currentPeriod.setBalanceSheetApi(currentPeriodBalanceSheetApi);

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriod);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertEquals(CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
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
}
