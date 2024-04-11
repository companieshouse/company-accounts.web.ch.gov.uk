package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible;

import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;

public interface IntangibleAssetsResourceTransformer {
    void mapIntangibleAssetsResourceToWebModel(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource);

    boolean hasIntangibleAssetsToMapToApiResource(IntangibleAssets intangibleAssets);

    void mapIntangibleAssetsToApiResource(IntangibleAssets intangibleAssets, IntangibleApi intangibleApi);

}
