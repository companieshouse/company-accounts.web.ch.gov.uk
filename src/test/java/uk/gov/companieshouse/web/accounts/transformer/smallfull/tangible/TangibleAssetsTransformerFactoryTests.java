package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsFixturesAndFittingsTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsLandAndBuildingsTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsMotorVehiclesTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsOfficeEquipmentTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsPlantAndMachineryTransformerImpl;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsTotalTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TangibleAssetsTransformerFactoryTests {

    TangibleAssetsTransformerFactory factory = new TangibleAssetsTransformerFactory();

    @Test
    @DisplayName("Asserts the factory returns a fixtures and fittings transformer when requesting "
            + "with the appropriate resource type")
    void getFixturesAndFittingsTransformer() {

        TangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                TangibleAssetsResource.FIXTURES_AND_FITTINGS);

        assertTrue(transformer instanceof TangibleAssetsFixturesAndFittingsTransformerImpl);
    }

    @Test
    @DisplayName("Asserts the factory returns a land and buildings transformer when requesting "
            + "with the appropriate resource type")
    void getLandAndBuildingsTransformer() {

        TangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                TangibleAssetsResource.LAND_AND_BUILDINGS);

        assertTrue(transformer instanceof TangibleAssetsLandAndBuildingsTransformerImpl);
    }

    @Test
    @DisplayName("Asserts the factory returns a motor vehicles transformer when requesting "
            + "with the appropriate resource type")
    void getMotorVehiclesTransformer() {

        TangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                TangibleAssetsResource.MOTOR_VEHICLES);

        assertTrue(transformer instanceof TangibleAssetsMotorVehiclesTransformerImpl);
    }

    @Test
    @DisplayName("Asserts the factory returns an office equipment transformer when requesting "
            + "with the appropriate resource type")
    void getOfficeEquipmentTransformer() {

        TangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                TangibleAssetsResource.OFFICE_EQUIPMENT);

        assertTrue(transformer instanceof TangibleAssetsOfficeEquipmentTransformerImpl);
    }

    @Test
    @DisplayName("Asserts the factory returns a plant and machinery transformer when requesting "
            + "with the appropriate resource type")
    void getPlantAndMachineryTransformer() {

        TangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                TangibleAssetsResource.PLANT_AND_MACHINERY);

            assertTrue(transformer instanceof TangibleAssetsPlantAndMachineryTransformerImpl);
    }

    @Test
    @DisplayName("Asserts the factory returns a total transformer when requesting "
            + "with the appropriate resource type")
    void getTotalTransformer() {

        TangibleAssetsResourceTransformer transformer = factory.getResourceTransformer(
                TangibleAssetsResource.TOTAL);

        assertTrue(transformer instanceof TangibleAssetsTotalTransformerImpl);
    }
}
