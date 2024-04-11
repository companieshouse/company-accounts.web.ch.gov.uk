package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

@Component
public class CurrentAssetsInvestmentsTransformerImpl implements NoteTransformer<CurrentAssetsInvestments, CurrentAssetsInvestmentsApi> {
    @Override
    public CurrentAssetsInvestments toWeb(CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi) {
        CurrentAssetsInvestments currentAssetsInvestments = new CurrentAssetsInvestments();

        if (currentAssetsInvestmentsApi == null) {
            return  currentAssetsInvestments;
        }

        currentAssetsInvestments.setCurrentAssetsInvestmentsDetails(currentAssetsInvestmentsApi.getDetails());

        return currentAssetsInvestments;
    }

    @Override
    public CurrentAssetsInvestmentsApi toApi(CurrentAssetsInvestments currentAssetsInvestments) {
        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = new CurrentAssetsInvestmentsApi();

        currentAssetsInvestmentsApi.setDetails(currentAssetsInvestments.getCurrentAssetsInvestmentsDetails());

        return currentAssetsInvestmentsApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS;
    }
}
