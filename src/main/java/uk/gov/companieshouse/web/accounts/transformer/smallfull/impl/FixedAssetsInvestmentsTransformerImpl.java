package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

@Component
public class FixedAssetsInvestmentsTransformerImpl implements NoteTransformer<FixedAssetsInvestments, FixedAssetsInvestmentsApi> {
    @Override
    public FixedAssetsInvestments toWeb(FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi) {
        if (fixedAssetsInvestmentsApi == null) {
            return new FixedAssetsInvestments();
        }

        FixedAssetsInvestments fixedAssetsInvestments = new FixedAssetsInvestments();
        fixedAssetsInvestments.setFixedAssetsDetails(fixedAssetsInvestmentsApi.getDetails());

        return fixedAssetsInvestments;
    }

    @Override
    public FixedAssetsInvestmentsApi toApi(FixedAssetsInvestments fixedAssetsInvestments) {
        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = new FixedAssetsInvestmentsApi();
        fixedAssetsInvestmentsApi.setDetails(fixedAssetsInvestments.getFixedAssetsDetails());

        return fixedAssetsInvestmentsApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT;
    }
}
