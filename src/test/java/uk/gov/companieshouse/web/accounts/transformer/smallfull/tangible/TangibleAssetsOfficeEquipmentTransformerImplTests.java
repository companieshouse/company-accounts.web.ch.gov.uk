package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.Cost;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.Depreciation;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsColumns;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsOfficeEquipmentTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TangibleAssetsOfficeEquipmentTransformerImplTests {

    private static final Long COST_AT_PERIOD_START = 1L;
    private static final Long ADDITIONS = 2L;
    private static final Long DISPOSALS = 3L;
    private static final Long REVALUATIONS = 4L;
    private static final Long TRANSFERS = 5L;
    private static final Long COST_AT_PERIOD_END = 6L;
    private static final Long DEPRECIATION_AT_PERIOD_START = 7L;
    private static final Long CHARGE_FOR_YEAR = 8L;
    private static final Long ON_DISPOSALS = 9L;
    private static final Long OTHER_ADJUSTMENTS = 10L;
    private static final Long DEPRECIATION_AT_PERIOD_END = 11L;
    private static final Long CURRENT_PERIOD = 12L;
    private static final Long PREVIOUS_PERIOD = 13L;

    private static final Long OTHER_COST_AT_PERIOD_START = 100L;
    private static final Long OTHER_ADDITIONS = 200L;
    private static final Long OTHER_DISPOSALS = 300L;
    private static final Long OTHER_REVALUATIONS = 400L;
    private static final Long OTHER_TRANSFERS = 500L;
    private static final Long OTHER_COST_AT_PERIOD_END = 600L;
    private static final Long OTHER_DEPRECIATION_AT_PERIOD_START = 700L;
    private static final Long OTHER_CHARGE_FOR_YEAR = 800L;
    private static final Long OTHER_ON_DISPOSALS = 900L;
    private static final Long OTHER_OTHER_ADJUSTMENTS = 1000L;
    private static final Long OTHER_DEPRECIATION_AT_PERIOD_END = 1100L;
    private static final Long OTHER_CURRENT_PERIOD = 1200L;
    private static final Long OTHER_PREVIOUS_PERIOD = 1300L;

    private TangibleAssetsResourceTransformer transformer = new TangibleAssetsOfficeEquipmentTransformerImpl();

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an empty web model")
    void mapFullApiResourceToEmptyWebModel() {

        TangibleAssets tangibleAssets = new TangibleAssets();
        TangibleAssetsResource officeEquipment = createOfficeEquipmentApiResource(true, true);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, officeEquipment);

        assertWebModelsMapped(tangibleAssets, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from an api resource which doesn't have a cost object to an empty web model")
    void mapApiResourceWithoutCostToEmptyWebModel() {

        TangibleAssets tangibleAssets = new TangibleAssets();
        TangibleAssetsResource officeEquipment = createOfficeEquipmentApiResource(false, true);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, officeEquipment);

        assertWebModelsMapped(tangibleAssets, false, true);
        assertNull(tangibleAssets.getCost());
    }

    @Test
    @DisplayName("Tests resources are mapped from an api resource which doesn't have a depreciation object to an empty web model")
    void mapApiResourceWithoutDepreciationToEmptyWebModel() {

        TangibleAssets tangibleAssets = new TangibleAssets();
        TangibleAssetsResource officeEquipment = createOfficeEquipmentApiResource(true, false);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, officeEquipment);

        assertWebModelsMapped(tangibleAssets, true, false);
        assertNull(tangibleAssets.getDepreciation());
    }

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an web model with other existing resources")
    void mapFullApiResourceToPopulatedWebModel() {

        TangibleAssets tangibleAssets = createTangibleAssetsWithPreExistingResources();

        TangibleAssetsResource officeEquipment = createOfficeEquipmentApiResource(true, true);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, officeEquipment);

        assertWebModelsMapped(tangibleAssets, true, true);
        assertPreExistingFieldsUnaffected(tangibleAssets);
    }

    @Test
    @DisplayName("Tests all resources are mapped from a web model to an api resource")
    void mapFullWebModelToApiResource() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(true, true, true);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getOfficeEquipment(), true, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without cost to an api resource")
    void mapWebModelWithoutCostToApiResource() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(false, true, true);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getOfficeEquipment(), false, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without depreciation to an api resource")
    void mapWebModelWithoutDepreciationToApiResource() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(true, false, true);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getOfficeEquipment(), true, false, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without net book values to an api resource")
    void mapWebModelWithoutNetBookValuesToApiResource() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(true, true, false);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getOfficeEquipment(), true, true, false);
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a fully populated web model")
    void hasTangibleAssetsToMapToApiResource() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(true, true, true);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a populated web model without any cost values")
    void hasTangibleAssetsToMapToApiResourceNoCost() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(false, true, true);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a populated web model without any depreciation values")
    void hasTangibleAssetsToMapToApiResourceNoDepreciation() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(true, false, true);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a populated web model without any net book values")
    void hasTangibleAssetsToMapToApiResourceNoNetBookValues() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(true, true, false);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a web model without any office equipment values")
    void hasTangibleAssetsToMapToApiResourceNoResourceValues() {

        TangibleAssets tangibleAssets =
                createTangibleAssetsWithOfficeEquipmentResources(false, false, false);

        assertFalse(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    private TangibleAssetsResource createOfficeEquipmentApiResource(boolean includeCost,
                                                                        boolean includeDepreciation) {

        TangibleAssetsResource officeEquipment = new TangibleAssetsResource();

        if (includeCost) {
            officeEquipment.setCost(createCostApiResource());
        }
        if (includeDepreciation) {
            officeEquipment.setDepreciation(createDepreciationApiResource());
        }
        officeEquipment.setNetBookValueAtEndOfCurrentPeriod(CURRENT_PERIOD);
        officeEquipment.setNetBookValueAtEndOfPreviousPeriod(PREVIOUS_PERIOD);

        return officeEquipment;
    }

    private Cost createCostApiResource() {

        Cost cost = new Cost();
        cost.setAtPeriodStart(COST_AT_PERIOD_START);
        cost.setAdditions(ADDITIONS);
        cost.setDisposals(DISPOSALS);
        cost.setRevaluations(REVALUATIONS);
        cost.setTransfers(TRANSFERS);
        cost.setAtPeriodEnd(COST_AT_PERIOD_END);
        return cost;
    }

    private Depreciation createDepreciationApiResource() {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart(DEPRECIATION_AT_PERIOD_START);
        depreciation.setChargeForYear(CHARGE_FOR_YEAR);
        depreciation.setOnDisposals(ON_DISPOSALS);
        depreciation.setOtherAdjustments(OTHER_ADJUSTMENTS);
        depreciation.setAtPeriodEnd(DEPRECIATION_AT_PERIOD_END);
        return depreciation;
    }

    private TangibleAssets createTangibleAssetsWithPreExistingResources() {

        TangibleAssets tangibleAssets = new TangibleAssets();

        TangibleAssetsCost tangibleAssetsCost = new TangibleAssetsCost();

        TangibleAssetsColumns costAtPeriodStart = new TangibleAssetsColumns();
        costAtPeriodStart.setPlantAndMachinery(OTHER_COST_AT_PERIOD_START);
        tangibleAssetsCost.setAtPeriodStart(costAtPeriodStart);

        TangibleAssetsColumns additions = new TangibleAssetsColumns();
        additions.setPlantAndMachinery(OTHER_ADDITIONS);
        tangibleAssetsCost.setAdditions(additions);

        TangibleAssetsColumns disposals = new TangibleAssetsColumns();
        disposals.setPlantAndMachinery(OTHER_DISPOSALS);
        tangibleAssetsCost.setDisposals(disposals);

        TangibleAssetsColumns revaluations = new TangibleAssetsColumns();
        revaluations.setPlantAndMachinery(OTHER_REVALUATIONS);
        tangibleAssetsCost.setRevaluations(revaluations);

        TangibleAssetsColumns transfers = new TangibleAssetsColumns();
        transfers.setPlantAndMachinery(OTHER_TRANSFERS);
        tangibleAssetsCost.setTransfers(transfers);

        TangibleAssetsColumns costAtPeriodEnd = new TangibleAssetsColumns();
        costAtPeriodEnd.setPlantAndMachinery(OTHER_COST_AT_PERIOD_END);
        tangibleAssetsCost.setAtPeriodEnd(costAtPeriodEnd);

        tangibleAssets.setCost(tangibleAssetsCost);

        TangibleAssetsDepreciation tangibleAssetsDepreciation = new TangibleAssetsDepreciation();

        TangibleAssetsColumns depreciationAtPeriodStart = new TangibleAssetsColumns();
        depreciationAtPeriodStart.setPlantAndMachinery(OTHER_DEPRECIATION_AT_PERIOD_START);
        tangibleAssetsDepreciation.setAtPeriodStart(depreciationAtPeriodStart);

        TangibleAssetsColumns chargeForYear = new TangibleAssetsColumns();
        chargeForYear.setPlantAndMachinery(OTHER_CHARGE_FOR_YEAR);
        tangibleAssetsDepreciation.setChargeForYear(chargeForYear);

        TangibleAssetsColumns onDisposals = new TangibleAssetsColumns();
        onDisposals.setPlantAndMachinery(OTHER_ON_DISPOSALS);
        tangibleAssetsDepreciation.setOnDisposals(onDisposals);

        TangibleAssetsColumns otherAdjustments = new TangibleAssetsColumns();
        otherAdjustments.setPlantAndMachinery(OTHER_OTHER_ADJUSTMENTS);
        tangibleAssetsDepreciation.setOtherAdjustments(otherAdjustments);

        TangibleAssetsColumns depreciationAtPeriodEnd = new TangibleAssetsColumns();
        depreciationAtPeriodEnd.setPlantAndMachinery(OTHER_DEPRECIATION_AT_PERIOD_END);
        tangibleAssetsDepreciation.setAtPeriodEnd(depreciationAtPeriodEnd);

        tangibleAssets.setDepreciation(tangibleAssetsDepreciation);

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = new TangibleAssetsNetBookValue();

        TangibleAssetsColumns currentPeriod = new TangibleAssetsColumns();
        currentPeriod.setPlantAndMachinery(OTHER_CURRENT_PERIOD);
        tangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);

        TangibleAssetsColumns previousPeriod = new TangibleAssetsColumns();
        previousPeriod.setPlantAndMachinery(OTHER_PREVIOUS_PERIOD);
        tangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);

        tangibleAssets.setNetBookValue(tangibleAssetsNetBookValue);

        return tangibleAssets;
    }

    private TangibleAssets createTangibleAssetsWithOfficeEquipmentResources(boolean includeCost,
                                                                                boolean includeDepreciation,
                                                                                boolean includeNetBookValue) {

        TangibleAssets tangibleAssets = new TangibleAssets();

        TangibleAssetsCost tangibleAssetsCost = new TangibleAssetsCost();
        tangibleAssets.setCost(tangibleAssetsCost);

        TangibleAssetsColumns costAtPeriodStart = new TangibleAssetsColumns();
        tangibleAssetsCost.setAtPeriodStart(costAtPeriodStart);

        TangibleAssetsColumns additions = new TangibleAssetsColumns();
        tangibleAssetsCost.setAdditions(additions);

        TangibleAssetsColumns disposals = new TangibleAssetsColumns();
        tangibleAssetsCost.setDisposals(disposals);

        TangibleAssetsColumns revaluations = new TangibleAssetsColumns();
        tangibleAssetsCost.setRevaluations(revaluations);

        TangibleAssetsColumns transfers = new TangibleAssetsColumns();
        tangibleAssetsCost.setTransfers(transfers);

        TangibleAssetsColumns costAtPeriodEnd = new TangibleAssetsColumns();
        tangibleAssetsCost.setAtPeriodEnd(costAtPeriodEnd);

        TangibleAssetsDepreciation tangibleAssetsDepreciation = new TangibleAssetsDepreciation();
        tangibleAssets.setDepreciation(tangibleAssetsDepreciation);

        TangibleAssetsColumns depreciationAtPeriodStart = new TangibleAssetsColumns();
        tangibleAssetsDepreciation.setAtPeriodStart(depreciationAtPeriodStart);

        TangibleAssetsColumns chargeForYear = new TangibleAssetsColumns();
        tangibleAssetsDepreciation.setChargeForYear(chargeForYear);

        TangibleAssetsColumns onDisposals = new TangibleAssetsColumns();
        tangibleAssetsDepreciation.setOnDisposals(onDisposals);

        TangibleAssetsColumns otherAdjustments = new TangibleAssetsColumns();
        tangibleAssetsDepreciation.setOtherAdjustments(otherAdjustments);

        TangibleAssetsColumns depreciationAtPeriodEnd = new TangibleAssetsColumns();
        tangibleAssetsDepreciation.setAtPeriodEnd(depreciationAtPeriodEnd);

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = new TangibleAssetsNetBookValue();
        tangibleAssets.setNetBookValue(tangibleAssetsNetBookValue);

        TangibleAssetsColumns currentPeriod = new TangibleAssetsColumns();
        tangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);

        TangibleAssetsColumns previousPeriod = new TangibleAssetsColumns();
        tangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);

        if (includeCost) {

            costAtPeriodStart.setOfficeEquipment(COST_AT_PERIOD_START);
            additions.setOfficeEquipment(ADDITIONS);
            disposals.setOfficeEquipment(DISPOSALS);
            revaluations.setOfficeEquipment(REVALUATIONS);
            transfers.setOfficeEquipment(TRANSFERS);
            costAtPeriodEnd.setOfficeEquipment(COST_AT_PERIOD_END);
        }

        if (includeDepreciation) {

            depreciationAtPeriodStart.setOfficeEquipment(DEPRECIATION_AT_PERIOD_START);
            chargeForYear.setOfficeEquipment(CHARGE_FOR_YEAR);
            onDisposals.setOfficeEquipment(ON_DISPOSALS);
            otherAdjustments.setOfficeEquipment(OTHER_ADJUSTMENTS);
            depreciationAtPeriodEnd.setOfficeEquipment(DEPRECIATION_AT_PERIOD_END);
        }

        if (includeNetBookValue) {

            currentPeriod.setOfficeEquipment(CURRENT_PERIOD);
            previousPeriod.setOfficeEquipment(PREVIOUS_PERIOD);
        }

        return tangibleAssets;
    }

    private void assertWebModelsMapped(TangibleAssets tangibleAssets,
                                       boolean expectCostFieldsMapped,
                                       boolean expectDepreciationFieldsMapped) {

        if (expectCostFieldsMapped) {
            assertCostFieldsMappedToWebModel(tangibleAssets);
        }
        if (expectDepreciationFieldsMapped) {
            assertDepreciationFieldsMappedToWebModel(tangibleAssets);
        }
        assertEquals(CURRENT_PERIOD, tangibleAssets.getNetBookValue().getCurrentPeriod().getOfficeEquipment());
        assertEquals(PREVIOUS_PERIOD, tangibleAssets.getNetBookValue().getPreviousPeriod().getOfficeEquipment());
    }

    private void assertCostFieldsMappedToWebModel(TangibleAssets tangibleAssets) {

        assertEquals(COST_AT_PERIOD_START, tangibleAssets.getCost().getAtPeriodStart().getOfficeEquipment());
        assertEquals(ADDITIONS, tangibleAssets.getCost().getAdditions().getOfficeEquipment());
        assertEquals(DISPOSALS, tangibleAssets.getCost().getDisposals().getOfficeEquipment());
        assertEquals(REVALUATIONS, tangibleAssets.getCost().getRevaluations().getOfficeEquipment());
        assertEquals(TRANSFERS, tangibleAssets.getCost().getTransfers().getOfficeEquipment());
        assertEquals(COST_AT_PERIOD_END, tangibleAssets.getCost().getAtPeriodEnd().getOfficeEquipment());
    }

    private void assertDepreciationFieldsMappedToWebModel(TangibleAssets tangibleAssets) {

        assertEquals(DEPRECIATION_AT_PERIOD_START, tangibleAssets.getDepreciation().getAtPeriodStart().getOfficeEquipment());
        assertEquals(CHARGE_FOR_YEAR, tangibleAssets.getDepreciation().getChargeForYear().getOfficeEquipment());
        assertEquals(ON_DISPOSALS, tangibleAssets.getDepreciation().getOnDisposals().getOfficeEquipment());
        assertEquals(OTHER_ADJUSTMENTS, tangibleAssets.getDepreciation().getOtherAdjustments().getOfficeEquipment());
        assertEquals(DEPRECIATION_AT_PERIOD_END, tangibleAssets.getDepreciation().getAtPeriodEnd().getOfficeEquipment());
    }

    private void assertApiFeildsMapped(TangibleAssetsResource resource,
                                       boolean expectCostFieldsMapped,
                                       boolean expectDepreciationFieldsMapped,
                                       boolean expectNetBookValueFieldsMapped) {

        if (expectCostFieldsMapped) {
            assertCostFieldsMappedToApiResource(resource.getCost());
        } else {
            assertNull(resource.getCost());
        }

        if (expectDepreciationFieldsMapped) {
            assertDepreciationFieldsMappedToApiResource(resource.getDepreciation());
        } else {
            assertNull(resource.getDepreciation());
        }

        if (expectNetBookValueFieldsMapped) {
            assertNetBookValueFieldsMappedToApiResource(resource);
        } else {
            assertNull(resource.getNetBookValueAtEndOfPreviousPeriod());
            assertNull(resource.getNetBookValueAtEndOfCurrentPeriod());
        }
    }

    private void assertCostFieldsMappedToApiResource(Cost cost) {
        assertNotNull(cost);
        assertEquals(COST_AT_PERIOD_START, cost.getAtPeriodStart());
        assertEquals(ADDITIONS, cost.getAdditions());
        assertEquals(DISPOSALS, cost.getDisposals());
        assertEquals(REVALUATIONS, cost.getRevaluations());
        assertEquals(TRANSFERS, cost.getTransfers());
    }

    private void assertDepreciationFieldsMappedToApiResource(Depreciation depreciation) {
        assertNotNull(depreciation);
        assertEquals(DEPRECIATION_AT_PERIOD_START, depreciation.getAtPeriodStart());
        assertEquals(CHARGE_FOR_YEAR, depreciation.getChargeForYear());
        assertEquals(ON_DISPOSALS, depreciation.getOnDisposals());
        assertEquals(OTHER_ADJUSTMENTS, depreciation.getOtherAdjustments());
        assertEquals(DEPRECIATION_AT_PERIOD_END, depreciation.getAtPeriodEnd());
    }

    private void assertNetBookValueFieldsMappedToApiResource(TangibleAssetsResource resource) {
        assertEquals(CURRENT_PERIOD, resource.getNetBookValueAtEndOfCurrentPeriod());
        assertEquals(PREVIOUS_PERIOD, resource.getNetBookValueAtEndOfPreviousPeriod());
    }

    private void assertPreExistingFieldsUnaffected(TangibleAssets tangibleAssets) {

        assertEquals(OTHER_COST_AT_PERIOD_START, tangibleAssets.getCost().getAtPeriodStart().getPlantAndMachinery());
        assertEquals(OTHER_ADDITIONS, tangibleAssets.getCost().getAdditions().getPlantAndMachinery());
        assertEquals(OTHER_DISPOSALS, tangibleAssets.getCost().getDisposals().getPlantAndMachinery());
        assertEquals(OTHER_REVALUATIONS, tangibleAssets.getCost().getRevaluations().getPlantAndMachinery());
        assertEquals(OTHER_TRANSFERS, tangibleAssets.getCost().getTransfers().getPlantAndMachinery());
        assertEquals(OTHER_COST_AT_PERIOD_END, tangibleAssets.getCost().getAtPeriodEnd().getPlantAndMachinery());
        assertEquals(OTHER_DEPRECIATION_AT_PERIOD_START, tangibleAssets.getDepreciation().getAtPeriodStart().getPlantAndMachinery());
        assertEquals(OTHER_CHARGE_FOR_YEAR, tangibleAssets.getDepreciation().getChargeForYear().getPlantAndMachinery());
        assertEquals(OTHER_ON_DISPOSALS, tangibleAssets.getDepreciation().getOnDisposals().getPlantAndMachinery());
        assertEquals(OTHER_OTHER_ADJUSTMENTS, tangibleAssets.getDepreciation().getOtherAdjustments().getPlantAndMachinery());
        assertEquals(OTHER_DEPRECIATION_AT_PERIOD_END, tangibleAssets.getDepreciation().getAtPeriodEnd().getPlantAndMachinery());
        assertEquals(OTHER_CURRENT_PERIOD, tangibleAssets.getNetBookValue().getCurrentPeriod().getPlantAndMachinery());
        assertEquals(OTHER_PREVIOUS_PERIOD, tangibleAssets.getNetBookValue().getPreviousPeriod().getPlantAndMachinery());
    }
}
