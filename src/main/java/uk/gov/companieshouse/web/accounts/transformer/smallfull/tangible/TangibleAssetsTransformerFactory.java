package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsFixturesAndFittingsTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsLandAndBuildingsTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsMotorVehiclesTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsOfficeEquipmentTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsPlantAndMachineryTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsTotalTransformerImpl;

@Component
public class TangibleAssetsTransformerFactory {
    public TangibleAssetsResourceTransformer getResourceTransformer(TangibleAssetsResource tangibleAssetsResource) {
        switch (tangibleAssetsResource) {
            case FIXTURES_AND_FITTINGS:
                return new TangibleAssetsFixturesAndFittingsTransformerImpl();
            case LAND_AND_BUILDINGS:
                return new TangibleAssetsLandAndBuildingsTransformerImpl();
            case MOTOR_VEHICLES:
                return new TangibleAssetsMotorVehiclesTransformerImpl();
            case OFFICE_EQUIPMENT:
                return new TangibleAssetsOfficeEquipmentTransformerImpl();
            case PLANT_AND_MACHINERY:
                return new TangibleAssetsPlantAndMachineryTransformerImpl();
            case TOTAL:
                return new TangibleAssetsTotalTransformerImpl();
            default:
                return null;
        }
    }
}
