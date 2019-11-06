package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible;

import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;

public interface IntangibleAssetsTransformer {

    IntangibleAssets getIntangibleAssets(IntangibleApi intangibleApi);

    IntangibleApi getIntangibleApi(IntangibleAssets intangibleAssets);
}
