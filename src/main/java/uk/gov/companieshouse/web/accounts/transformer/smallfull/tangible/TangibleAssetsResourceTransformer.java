package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible;

import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;

public interface TangibleAssetsResourceTransformer {

    void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource);
}
