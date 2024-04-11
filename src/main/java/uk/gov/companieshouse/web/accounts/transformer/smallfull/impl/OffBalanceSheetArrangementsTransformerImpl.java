package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

@Component
public class OffBalanceSheetArrangementsTransformerImpl implements
        NoteTransformer<OffBalanceSheetArrangements, OffBalanceSheetApi> {
    @Override
    public OffBalanceSheetArrangements toWeb(OffBalanceSheetApi offBalanceSheetApi) {
        OffBalanceSheetArrangements arrangements = new OffBalanceSheetArrangements();

        if (offBalanceSheetApi == null) {
            return arrangements;
        }

        arrangements.setOffBalanceSheetArrangementsDetails(offBalanceSheetApi.getDetails());
        return arrangements;
    }

    @Override
    public OffBalanceSheetApi toApi(OffBalanceSheetArrangements offBalanceSheetArrangements) {
        OffBalanceSheetApi offBalanceSheetApi = new OffBalanceSheetApi();
        offBalanceSheetApi.setDetails(offBalanceSheetArrangements.getOffBalanceSheetArrangementsDetails());
        return offBalanceSheetApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS;
    }
}
