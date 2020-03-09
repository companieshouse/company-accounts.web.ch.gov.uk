package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;

public interface OffBalanceSheetArrangementsTransformer {

    OffBalanceSheetArrangements getOffBalanceSheetArrangements(OffBalanceSheetApi offBalanceSheetApi);

    OffBalanceSheetApi getOffBalanceSheetArrangementsApi(OffBalanceSheetArrangements offBalanceSheetArrangements);
}
