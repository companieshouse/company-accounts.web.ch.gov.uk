package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;

public interface FixedAssetsInvestmentsTransformer {

    FixedAssetsInvestments getFixedAssetsInvestments(FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi);

    FixedAssetsInvestmentsApi getFixedAssetsInvestmentsApi(FixedAssetsInvestments fixedAssetsInvestments);
}
