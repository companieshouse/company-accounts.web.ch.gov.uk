package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsTransformerFactory;

public class IntangibleAssetsTransformerImpl implements IntangibleAssetsTransformer {


    @Autowired
    private IntangibleAssetsTransformerFactory factory;


    @Override
    public IntangibleAssets getIntangibleAssets(IntangibleApi intangibleApi) {
        return null;
    }

    @Override
    public IntangibleApi getIntangibleApi(IntangibleAssets intangibleAssets) {
        return null;
    }
}
