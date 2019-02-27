package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.FixedAssetsInvestmentsTransformer;

@Component
public class FixedAssetsInvestmentsTransformerImpl implements FixedAssetsInvestmentsTransformer {

    @Override
    public FixedAssetsInvestments getFixedAssetsInvestments(FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi) {

        if (fixedAssetsInvestmentsApi == null) {
            return new FixedAssetsInvestments();
        }

        FixedAssetsInvestments fixedAssetsInvestments = new FixedAssetsInvestments();
        fixedAssetsInvestments.setDetails(fixedAssetsInvestmentsApi.getDetails());

        return fixedAssetsInvestments;
    }

    @Override
    public FixedAssetsInvestmentsApi getFixedAssetsInvestmentsApi(FixedAssetsInvestments fixedAssetsInvestments) {

        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = new FixedAssetsInvestmentsApi();
        fixedAssetsInvestmentsApi.setDetails(fixedAssetsInvestments.getDetails());

        return fixedAssetsInvestmentsApi;
    }
}
