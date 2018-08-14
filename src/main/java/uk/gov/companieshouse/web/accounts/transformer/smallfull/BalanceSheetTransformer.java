package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;

public interface BalanceSheetTransformer {

    BalanceSheet getBalanceSheet(CurrentPeriod currentPeriod);
}
