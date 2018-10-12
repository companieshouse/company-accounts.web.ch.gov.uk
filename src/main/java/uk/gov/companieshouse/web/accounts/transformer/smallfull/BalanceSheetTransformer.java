package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;

public interface BalanceSheetTransformer {

    BalanceSheet getBalanceSheet(CurrentPeriodApi currentPeriod);

    CurrentPeriodApi getCurrentPeriod(BalanceSheet balanceSheet);

    /**
     * Gets the previous period data on the balance sheet.
     *
     * @param balanceSheet populated balance sheet
     * @return populated previous period object
     */
    PreviousPeriodApi getPreviousPeriod(BalanceSheet balanceSheet);
}
