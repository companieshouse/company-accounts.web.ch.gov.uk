package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible;

import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;

public interface TangibleAssetsTransformer {

    TangibleAssets getTangibleAssets(TangibleApi tangibleApi);
}
