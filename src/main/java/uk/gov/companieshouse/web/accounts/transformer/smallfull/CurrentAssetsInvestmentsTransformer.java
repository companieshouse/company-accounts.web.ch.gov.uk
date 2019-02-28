package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.CurrentAssetsInvestments;

public interface CurrentAssetsInvestmentsTransformer {

    CurrentAssetsInvestments getCurrentAssetsInvestments(CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi);

    CurrentAssetsInvestmentsApi getCurrentAssetsInvestmentsApi(CurrentAssetsInvestments currentAssetsInvestments);
}
