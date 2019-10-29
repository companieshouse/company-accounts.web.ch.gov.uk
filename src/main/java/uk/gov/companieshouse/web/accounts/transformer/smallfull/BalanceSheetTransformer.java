package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;

public interface BalanceSheetTransformer {

    BalanceSheet getBalanceSheet(CurrentPeriodApi currentPeriod, PreviousPeriodApi previousPeriod);

    BalanceSheetApi getCurrentPeriodBalanceSheet(BalanceSheet balanceSheet);

    BalanceSheetApi getPreviousPeriodBalanceSheet(BalanceSheet balanceSheet);
}
