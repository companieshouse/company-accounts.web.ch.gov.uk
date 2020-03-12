package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.OffBalanceSheetArrangementsTransformer;

@Component
public class OffBalanceSheetArrangementsTransformerImpl implements
        OffBalanceSheetArrangementsTransformer {

    @Override
    public OffBalanceSheetArrangements getOffBalanceSheetArrangements(
            OffBalanceSheetApi offBalanceSheetApi) {

        OffBalanceSheetArrangements arrangements = new OffBalanceSheetArrangements();

        if (offBalanceSheetApi == null) {
            return arrangements;
        }

        arrangements.setOffBalanceSheetArrangementsDetails(offBalanceSheetApi.getDetails());
        return arrangements;
    }

    @Override
    public OffBalanceSheetApi getOffBalanceSheetArrangementsApi(
            OffBalanceSheetArrangements offBalanceSheetArrangements) {

        OffBalanceSheetApi offBalanceSheetApi = new OffBalanceSheetApi();

        offBalanceSheetApi.setDetails(offBalanceSheetArrangements.getOffBalanceSheetArrangementsDetails());

        return offBalanceSheetApi;
    }
}
