package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible;

import uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsGoodwillTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsOtherIntangibleAssetsTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsTotalTransformerImpl;

public class IntangibleAssetsTransformerFactory {

    public IntangibleAssetsResourceTransformer getResourceTransformer(IntangibleAssetsResource intangibleAssetsResource) {

        switch (intangibleAssetsResource) {
            case GOODWILL:
                return new IntangibleAssetsGoodwillTransformerImpl();
            case OTHER_INTANGIBLE_ASSETS:
                return new IntangibleAssetsOtherIntangibleAssetsTransformerImpl();
            case TOTAL:
                return new IntangibleAssetsTotalTransformerImpl();
            default:
                return null;
        }
    }
}
