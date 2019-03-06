package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CurrentAssetsInvestmentsTransformer;

public class CurrentAssetsInvestmentsTransformerImpl implements CurrentAssetsInvestmentsTransformer {

    @Override
    public CurrentAssetsInvestments getCurrentAssetsInvestments(CurrentAssetsInvestmentsApi
        currentAssetsInvestmentsApi) {

        CurrentAssetsInvestments currentAssetsInvestments = new CurrentAssetsInvestments();

        if (currentAssetsInvestmentsApi == null) {
            return  currentAssetsInvestments;
        }

        currentAssetsInvestments.setDetails(currentAssetsInvestmentsApi.getDetails());

        return currentAssetsInvestments;
    }

    @Override
    public CurrentAssetsInvestmentsApi getCurrentAssetsInvestmentsApi(
        CurrentAssetsInvestments currentAssetsInvestments) {

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = new CurrentAssetsInvestmentsApi();

        currentAssetsInvestmentsApi.setDetails(currentAssetsInvestments.getDetails());

        return currentAssetsInvestmentsApi;
    }
}
