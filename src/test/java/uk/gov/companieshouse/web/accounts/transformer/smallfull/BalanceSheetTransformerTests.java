package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.BalanceSheetTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceSheetTransformerTests {

    private Integer CALLED_UP_SHARE_CAPITAL = 1;

    private BalanceSheetTransformer transformer = new BalanceSheetTransformerImpl();

    private CurrentPeriod currentPeriod;

    @BeforeEach
    private void init() {

        uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheet apiCurrentPeriodBalanceSheet
                = new uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheet();

        apiCurrentPeriodBalanceSheet.setCalledUpShareCapitalNotPaid(CALLED_UP_SHARE_CAPITAL);

        currentPeriod = new CurrentPeriod();

        currentPeriod.setBalanceSheet(apiCurrentPeriodBalanceSheet);
    }

    @Test
    @DisplayName("Get Balance Sheet - Assert Called Up Share Capital is Correct")
    void getBalanceSheetCalledUpShareCapital() {

        BalanceSheet balanceSheet = transformer.getBalanceSheet(currentPeriod);

        assertNotNull(balanceSheet);
        assertNotNull(balanceSheet.getCalledUpShareCapitalNotPaid());
        assertEquals(CALLED_UP_SHARE_CAPITAL, balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
    }
}
