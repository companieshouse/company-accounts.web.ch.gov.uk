package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TangibleAssetsTransformerImplTest {

    @Mock
    TangibleAssetsTransformerFactory factory;

    @Mock
    TangibleAssetsResourceTransformer resourceTransformer;

    @InjectMocks
    TangibleAssetsTransformer transformer = new TangibleAssetsTransformerImpl();

    private static final String ADDITIONAL_INFORMATION = "additionalInformation";

    @Test
    @DisplayName("Asserts that the fixtures and fittings transformer is called when its api resource is not null")
    void getTangibleAssetsFixturesAndFittingsTransformerCalled() {

        TangibleApi tangibleApi = new TangibleApi();
        TangibleAssetsResource fixturesAndFittings = new TangibleAssetsResource();
        tangibleApi.setFixturesAndFittings(fixturesAndFittings);
        tangibleApi.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.FIXTURES_AND_FITTINGS))
                .thenReturn(resourceTransformer);

        TangibleAssets tangibleAssets = transformer.getTangibleAssets(tangibleApi);

        assertNotNull(tangibleAssets);
        assertEquals(ADDITIONAL_INFORMATION, tangibleAssets.getAdditionalInformation());

        verify(resourceTransformer, times(1))
                .mapTangibleAssetsResourceToWebModel(tangibleAssets, fixturesAndFittings);
    }

    @Test
    @DisplayName("Asserts that the land and buildings transformer is called when its api resource is not null")
    void getTangibleAssetsLandAndBuildingsTransformerCalled() {

        TangibleApi tangibleApi = new TangibleApi();
        TangibleAssetsResource landAndBuildings = new TangibleAssetsResource();
        tangibleApi.setLandAndBuildings(landAndBuildings);
        tangibleApi.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.LAND_AND_BUILDINGS))
                .thenReturn(resourceTransformer);

        TangibleAssets tangibleAssets = transformer.getTangibleAssets(tangibleApi);

        assertNotNull(tangibleAssets);
        assertEquals(ADDITIONAL_INFORMATION, tangibleAssets.getAdditionalInformation());

        verify(resourceTransformer, times(1))
                .mapTangibleAssetsResourceToWebModel(tangibleAssets, landAndBuildings);
    }

    @Test
    @DisplayName("Asserts that the motor vehicles transformer is called when its api resource is not null")
    void getTangibleAssetsMotorVehiclesTransformerCalled() {

        TangibleApi tangibleApi = new TangibleApi();
        TangibleAssetsResource motorVehicles = new TangibleAssetsResource();
        tangibleApi.setMotorVehicles(motorVehicles);
        tangibleApi.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.MOTOR_VEHICLES))
                .thenReturn(resourceTransformer);

        TangibleAssets tangibleAssets = transformer.getTangibleAssets(tangibleApi);

        assertNotNull(tangibleAssets);
        assertEquals(ADDITIONAL_INFORMATION, tangibleAssets.getAdditionalInformation());

        verify(resourceTransformer, times(1))
                .mapTangibleAssetsResourceToWebModel(tangibleAssets, motorVehicles);
    }

    @Test
    @DisplayName("Asserts that the office equipment transformer is called when its api resource is not null")
    void getTangibleAssetsOfficeEquipmentTransformerCalled() {

        TangibleApi tangibleApi = new TangibleApi();
        TangibleAssetsResource officeEquipment = new TangibleAssetsResource();
        tangibleApi.setOfficeEquipment(officeEquipment);
        tangibleApi.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.OFFICE_EQUIPMENT))
                .thenReturn(resourceTransformer);

        TangibleAssets tangibleAssets = transformer.getTangibleAssets(tangibleApi);

        assertNotNull(tangibleAssets);
        assertEquals(ADDITIONAL_INFORMATION, tangibleAssets.getAdditionalInformation());

        verify(resourceTransformer, times(1))
                .mapTangibleAssetsResourceToWebModel(tangibleAssets, officeEquipment);
    }

    @Test
    @DisplayName("Asserts that the plant and machinery transformer is called when its api resource is not null")
    void getTangibleAssetsPlantAndMachineryTransformerCalled() {

        TangibleApi tangibleApi = new TangibleApi();
        TangibleAssetsResource plantAndMachinery = new TangibleAssetsResource();
        tangibleApi.setPlantAndMachinery(plantAndMachinery);
        tangibleApi.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.PLANT_AND_MACHINERY))
                .thenReturn(resourceTransformer);

        TangibleAssets tangibleAssets = transformer.getTangibleAssets(tangibleApi);

        assertNotNull(tangibleAssets);
        assertEquals(ADDITIONAL_INFORMATION, tangibleAssets.getAdditionalInformation());

        verify(resourceTransformer, times(1))
                .mapTangibleAssetsResourceToWebModel(tangibleAssets, plantAndMachinery);
    }

    @Test
    @DisplayName("Asserts that the total transformer is called when its api resource is not null")
    void getTangibleAssetsTotalTransformerCalled() {

        TangibleApi tangibleApi = new TangibleApi();
        TangibleAssetsResource total = new TangibleAssetsResource();
        tangibleApi.setTotal(total);
        tangibleApi.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.TOTAL))
                .thenReturn(resourceTransformer);

        TangibleAssets tangibleAssets = transformer.getTangibleAssets(tangibleApi);

        assertNotNull(tangibleAssets);
        assertEquals(ADDITIONAL_INFORMATION, tangibleAssets.getAdditionalInformation());

        verify(resourceTransformer, times(1))
                .mapTangibleAssetsResourceToWebModel(tangibleAssets, total);
    }

    @Test
    @DisplayName("Tests that all resource transformers are called when the web model has resources to map")
    void getTangibleApiForWithResourcesToMapInWebModel() {

        TangibleAssets tangibleAssets = new TangibleAssets();
        tangibleAssets.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(any(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.class)))
                .thenReturn(resourceTransformer);

        when(resourceTransformer.hasTangibleAssetsToMapToApiResource(tangibleAssets))
                .thenReturn(true);

        TangibleApi tangibleApi = transformer.getTangibleApi(tangibleAssets);

        assertNotNull(tangibleApi);
        assertEquals(ADDITIONAL_INFORMATION, tangibleApi.getAdditionalInformation());

        verify(resourceTransformer, times(6))
                .mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);
    }

    @Test
    @DisplayName("Tests that no resource transformers are called when the web model doesn't have resources to map")
    void getTangibleApiForWithoutResourcesToMapInWebModel() {

        TangibleAssets tangibleAssets = new TangibleAssets();
        tangibleAssets.setAdditionalInformation(ADDITIONAL_INFORMATION);

        when(factory.getResourceTransformer(any(
                uk.gov.companieshouse.web.accounts.enumeration.TangibleAssetsResource.class)))
                .thenReturn(resourceTransformer);

        when(resourceTransformer.hasTangibleAssetsToMapToApiResource(tangibleAssets))
                .thenReturn(false);

        TangibleApi tangibleApi = transformer.getTangibleApi(tangibleAssets);

        assertNotNull(tangibleApi);
        assertEquals(ADDITIONAL_INFORMATION, tangibleApi.getAdditionalInformation());

        verify(resourceTransformer, never())
                .mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);
    }
}
