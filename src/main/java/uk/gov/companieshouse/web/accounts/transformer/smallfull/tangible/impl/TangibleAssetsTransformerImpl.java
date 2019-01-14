package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsResourceTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsTransformerFactory;

@Component
public class TangibleAssetsTransformerImpl implements TangibleAssetsTransformer {

    @Autowired
    private TangibleAssetsTransformerFactory factory;

    @Override
    public TangibleAssets getTangibleAssets(TangibleApi tangibleApi) {

        TangibleAssets tangibleAssets = new TangibleAssets();
        tangibleAssets.setAdditionalInformation(tangibleApi.getAdditionalInformation());

        TangibleAssetsResourceTransformer tangibleAssetsResourceTransformer;

        if (tangibleApi.getFixturesAndFittings() != null) {

            tangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(TangibleAssetsResource.FIXTURES_AND_FITTINGS);

            tangibleAssetsResourceTransformer
                    .mapTangibleAssetsResourceToWebModel(tangibleAssets, tangibleApi.getFixturesAndFittings());
        }

        if (tangibleApi.getLandAndBuildings() != null) {

            tangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(TangibleAssetsResource.LAND_AND_BUILDINGS);

            tangibleAssetsResourceTransformer
                    .mapTangibleAssetsResourceToWebModel(tangibleAssets, tangibleApi.getLandAndBuildings());
        }

        if (tangibleApi.getMotorVehicles() != null) {

            tangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(TangibleAssetsResource.MOTOR_VEHICLES);

            tangibleAssetsResourceTransformer
                    .mapTangibleAssetsResourceToWebModel(tangibleAssets, tangibleApi.getMotorVehicles());
        }

        if (tangibleApi.getOfficeEquipment() != null) {

            tangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(TangibleAssetsResource.OFFICE_EQUIPMENT);

            tangibleAssetsResourceTransformer
                    .mapTangibleAssetsResourceToWebModel(tangibleAssets, tangibleApi.getOfficeEquipment());
        }

        if (tangibleApi.getPlantAndMachinery() != null) {

            tangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(TangibleAssetsResource.PLANT_AND_MACHINERY);

            tangibleAssetsResourceTransformer
                    .mapTangibleAssetsResourceToWebModel(tangibleAssets, tangibleApi.getPlantAndMachinery());
        }

        if (tangibleApi.getTotal() != null) {

            tangibleAssetsResourceTransformer =
                    factory.getResourceTransformer(TangibleAssetsResource.TOTAL);

            tangibleAssetsResourceTransformer
                    .mapTangibleAssetsResourceToWebModel(tangibleAssets, tangibleApi.getTotal());
        }

        return tangibleAssets;
    }
}
