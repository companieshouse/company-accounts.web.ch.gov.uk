package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CurrentAssetsInvestmentsTransformer;

@Component
public class CurrentAssetsInvestmentsTransformerImpl implements CurrentAssetsInvestmentsTransformer {

    @Override
    public CurrentAssetsInvestments getCurrentAssetsInvestments(CurrentAssetsInvestmentsApi
        currentAssetsInvestmentsApi) {

        CurrentAssetsInvestments currentAssetsInvestments = new CurrentAssetsInvestments();

        if (currentAssetsInvestmentsApi == null) {
            return  currentAssetsInvestments;
        }

        currentAssetsInvestments.setCurrentAssetsInvestmentsDetails(currentAssetsInvestmentsApi.getDetails());

        return currentAssetsInvestments;
    }

    @Override
    public CurrentAssetsInvestmentsApi getCurrentAssetsInvestmentsApi(
        CurrentAssetsInvestments currentAssetsInvestments) {

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = new CurrentAssetsInvestmentsApi();

        currentAssetsInvestmentsApi.setDetails(currentAssetsInvestments.getCurrentAssetsInvestmentsDetails());

        return currentAssetsInvestmentsApi;
    }
}
