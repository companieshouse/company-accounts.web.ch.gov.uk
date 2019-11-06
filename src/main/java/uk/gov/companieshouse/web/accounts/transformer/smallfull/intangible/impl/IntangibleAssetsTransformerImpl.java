package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl;

import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.web.accounts.enumeration.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsResourceTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsTransformerFactory;

@Component
public class IntangibleAssetsTransformerImpl implements IntangibleAssetsTransformer {

    @Autowired
    private IntangibleAssetsTransformerFactory factory;

    @Override
    public IntangibleAssets getIntangibleAssets(IntangibleApi intangibleApi) {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        intangibleAssets.setAdditionalInformation(intangibleApi.getAdditionalInformation());

        IntangibleAssetsResourceTransformer intangibleAssetsResourceTransformer;

        if (intangibleApi.getGoodwill() != null) {

            intangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(IntangibleAssetsResource.GOODWILL);

            intangibleAssetsResourceTransformer
                    .mapIntangibleAssetsResourceToWebModel(intangibleAssets, intangibleApi.getGoodwill());
        }

        if (intangibleApi.getOtherIntangibleAssets() != null) {

            intangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(IntangibleAssetsResource.OTHER_INTANGIBLE_ASSETS);

            intangibleAssetsResourceTransformer
                    .mapIntangibleAssetsResourceToWebModel(intangibleAssets, intangibleApi.getOtherIntangibleAssets());
        }

        if (intangibleApi.getTotal() != null) {

            intangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(IntangibleAssetsResource.TOTAL);

            intangibleAssetsResourceTransformer
                    .mapIntangibleAssetsResourceToWebModel(intangibleAssets, intangibleApi.getTotal());
        }

        return intangibleAssets;
    }

    @Override
    public IntangibleApi getIntangibleApi(IntangibleAssets intangibleAssets) {

        IntangibleApi intangibleApi = new IntangibleApi();

        if (StringUtils.isNotBlank(intangibleAssets.getAdditionalInformation())) {
            intangibleApi.setAdditionalInformation(intangibleAssets.getAdditionalInformation());
        }

        Stream.of(IntangibleAssetsResource.values()).forEach(intangibleAssetsResource -> {

            IntangibleAssetsResourceTransformer resourceTransformer =
                    factory.getResourceTransformer(intangibleAssetsResource);

            if (resourceTransformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets)) {

                resourceTransformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);
            }
        });

        return intangibleApi;
    }
}
