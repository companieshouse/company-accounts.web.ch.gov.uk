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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.Additions;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.CostAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.CostAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.Disposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.Revaluations;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.TangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.Transfers;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.ChargeForYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.DepreciationAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.DepreciationAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.OnDisposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.OtherAdjustments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.netbookvalue.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.netbookvalue.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.netbookvalue.TangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl.TangibleAssetsTotalTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TangibleAssetsTotalTransformerImplTests {
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

    private TangibleAssetsResourceTransformer transformer = new TangibleAssetsTotalTransformerImpl();

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an empty web model")
    void mapFullApiResourceToEmptyWebModel() {
        TangibleAssets tangibleAssets = new TangibleAssets();
        TangibleAssetsResource total = createTotalApiResource(true, true);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, total);

        assertWebModelsMapped(tangibleAssets, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from an api resource which doesn't have a cost object to an empty web model")
    void mapApiResourceWithoutCostToEmptyWebModel() {
        TangibleAssets tangibleAssets = new TangibleAssets();
        TangibleAssetsResource total = createTotalApiResource(false, true);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, total);

        assertWebModelsMapped(tangibleAssets, false, true);
        assertNull(tangibleAssets.getCost());
    }

    @Test
    @DisplayName("Tests resources are mapped from an api resource which doesn't have a depreciation object to an empty web model")
    void mapApiResourceWithoutDepreciationToEmptyWebModel() {
        TangibleAssets tangibleAssets = new TangibleAssets();
        TangibleAssetsResource total = createTotalApiResource(true, false);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, total);

        assertWebModelsMapped(tangibleAssets, true, false);
        assertNull(tangibleAssets.getDepreciation());
    }

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an web model with other existing resources")
    void mapFullApiResourceToPopulatedWebModel() {
        TangibleAssets tangibleAssets = createTangibleAssetsWithPreExistingResources();

        TangibleAssetsResource total = createTotalApiResource(true, true);
        transformer.mapTangibleAssetsResourceToWebModel(tangibleAssets, total);

        assertWebModelsMapped(tangibleAssets, true, true);
        assertPreExistingFieldsUnaffected(tangibleAssets);
    }

    @Test
    @DisplayName("Tests all resources are mapped from a web model to an api resource")
    void mapFullWebModelToApiResource() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(true, true, true);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getTotal(), true, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without cost to an api resource")
    void mapWebModelWithoutCostToApiResource() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(false, true, true);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getTotal(), false, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without depreciation to an api resource")
    void mapWebModelWithoutDepreciationToApiResource() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(true, false, true);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getTotal(), true, false, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without net book values to an api resource")
    void mapWebModelWithoutNetBookValuesToApiResource() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(true, true, false);

        TangibleApi tangibleApi = new TangibleApi();
        transformer.mapTangibleAssetsToApiResource(tangibleAssets, tangibleApi);

        assertApiFeildsMapped(tangibleApi.getTotal(), true, true, false);
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a fully populated web model")
    void hasTangibleAssetsToMapToApiResource() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(true, true, true);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a populated web model without any cost values")
    void hasTangibleAssetsToMapToApiResourceNoCost() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(false, true, true);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a populated web model without any depreciation values")
    void hasTangibleAssetsToMapToApiResourceNoDepreciation() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(true, false, true);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a populated web model without any net book values")
    void hasTangibleAssetsToMapToApiResourceNoNetBookValues() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(true, true, false);

        assertTrue(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    @Test
    @DisplayName("Tests tangible assets will be mapped for a web model without any total values")
    void hasTangibleAssetsToMapToApiResourceNoResourceValues() {
        TangibleAssets tangibleAssets =
                createTangibleAssetsWithTotalResources(false, false, false);

        assertFalse(transformer.hasTangibleAssetsToMapToApiResource(tangibleAssets));
    }

    private TangibleAssetsResource createTotalApiResource(boolean includeCost,
                                                                        boolean includeDepreciation) {
        TangibleAssetsResource total = new TangibleAssetsResource();

        if (includeCost) {
            total.setCost(createCostApiResource());
        }
        if (includeDepreciation) {
            total.setDepreciation(createDepreciationApiResource());
        }
        total.setNetBookValueAtEndOfCurrentPeriod(CURRENT_PERIOD);
        total.setNetBookValueAtEndOfPreviousPeriod(PREVIOUS_PERIOD);

        return total;
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

        CostAtPeriodStart costAtPeriodStart = new CostAtPeriodStart();
        costAtPeriodStart.setFixturesAndFittings(OTHER_COST_AT_PERIOD_START);
        tangibleAssetsCost.setAtPeriodStart(costAtPeriodStart);

        Additions additions = new Additions();
        additions.setFixturesAndFittings(OTHER_ADDITIONS);
        tangibleAssetsCost.setAdditions(additions);

        Disposals disposals = new Disposals();
        disposals.setFixturesAndFittings(OTHER_DISPOSALS);
        tangibleAssetsCost.setDisposals(disposals);

        Revaluations revaluations = new Revaluations();
        revaluations.setFixturesAndFittings(OTHER_REVALUATIONS);
        tangibleAssetsCost.setRevaluations(revaluations);

        Transfers transfers = new Transfers();
        transfers.setFixturesAndFittings(OTHER_TRANSFERS);
        tangibleAssetsCost.setTransfers(transfers);

        CostAtPeriodEnd costAtPeriodEnd = new CostAtPeriodEnd();
        costAtPeriodEnd.setFixturesAndFittings(OTHER_COST_AT_PERIOD_END);
        tangibleAssetsCost.setAtPeriodEnd(costAtPeriodEnd);

        tangibleAssets.setCost(tangibleAssetsCost);

        TangibleAssetsDepreciation tangibleAssetsDepreciation = new TangibleAssetsDepreciation();

        DepreciationAtPeriodStart depreciationAtPeriodStart = new DepreciationAtPeriodStart();
        depreciationAtPeriodStart.setFixturesAndFittings(OTHER_DEPRECIATION_AT_PERIOD_START);
        tangibleAssetsDepreciation.setAtPeriodStart(depreciationAtPeriodStart);

        ChargeForYear chargeForYear = new ChargeForYear();
        chargeForYear.setFixturesAndFittings(OTHER_CHARGE_FOR_YEAR);
        tangibleAssetsDepreciation.setChargeForYear(chargeForYear);

        OnDisposals onDisposals = new OnDisposals();
        onDisposals.setFixturesAndFittings(OTHER_ON_DISPOSALS);
        tangibleAssetsDepreciation.setOnDisposals(onDisposals);

        OtherAdjustments otherAdjustments = new OtherAdjustments();
        otherAdjustments.setFixturesAndFittings(OTHER_OTHER_ADJUSTMENTS);
        tangibleAssetsDepreciation.setOtherAdjustments(otherAdjustments);

        DepreciationAtPeriodEnd depreciationAtPeriodEnd = new DepreciationAtPeriodEnd();
        depreciationAtPeriodEnd.setFixturesAndFittings(OTHER_DEPRECIATION_AT_PERIOD_END);
        tangibleAssetsDepreciation.setAtPeriodEnd(depreciationAtPeriodEnd);

        tangibleAssets.setDepreciation(tangibleAssetsDepreciation);

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = new TangibleAssetsNetBookValue();

        CurrentPeriod currentPeriod = new CurrentPeriod();
        currentPeriod.setFixturesAndFittings(OTHER_CURRENT_PERIOD);
        tangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);

        PreviousPeriod previousPeriod = new PreviousPeriod();
        previousPeriod.setFixturesAndFittings(OTHER_PREVIOUS_PERIOD);
        tangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);

        tangibleAssets.setNetBookValue(tangibleAssetsNetBookValue);

        return tangibleAssets;
    }

    private TangibleAssets createTangibleAssetsWithTotalResources(boolean includeCost,
            boolean includeDepreciation,
            boolean includeNetBookValue) {
        TangibleAssets tangibleAssets = new TangibleAssets();

        TangibleAssetsCost tangibleAssetsCost = new TangibleAssetsCost();
        tangibleAssets.setCost(tangibleAssetsCost);

        CostAtPeriodStart costAtPeriodStart = new CostAtPeriodStart();
        tangibleAssetsCost.setAtPeriodStart(costAtPeriodStart);

        Additions additions = new Additions();
        tangibleAssetsCost.setAdditions(additions);

        Disposals disposals = new Disposals();
        tangibleAssetsCost.setDisposals(disposals);

        Revaluations revaluations = new Revaluations();
        tangibleAssetsCost.setRevaluations(revaluations);

        Transfers transfers = new Transfers();
        tangibleAssetsCost.setTransfers(transfers);

        CostAtPeriodEnd costAtPeriodEnd = new CostAtPeriodEnd();
        tangibleAssetsCost.setAtPeriodEnd(costAtPeriodEnd);

        TangibleAssetsDepreciation tangibleAssetsDepreciation = new TangibleAssetsDepreciation();
        tangibleAssets.setDepreciation(tangibleAssetsDepreciation);

        DepreciationAtPeriodStart depreciationAtPeriodStart = new DepreciationAtPeriodStart();
        tangibleAssetsDepreciation.setAtPeriodStart(depreciationAtPeriodStart);

        ChargeForYear chargeForYear = new ChargeForYear();
        tangibleAssetsDepreciation.setChargeForYear(chargeForYear);

        OnDisposals onDisposals = new OnDisposals();
        tangibleAssetsDepreciation.setOnDisposals(onDisposals);

        OtherAdjustments otherAdjustments = new OtherAdjustments();
        tangibleAssetsDepreciation.setOtherAdjustments(otherAdjustments);

        DepreciationAtPeriodEnd depreciationAtPeriodEnd = new DepreciationAtPeriodEnd();
        tangibleAssetsDepreciation.setAtPeriodEnd(depreciationAtPeriodEnd);

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = new TangibleAssetsNetBookValue();
        tangibleAssets.setNetBookValue(tangibleAssetsNetBookValue);

        CurrentPeriod currentPeriod = new CurrentPeriod();
        tangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);

        PreviousPeriod previousPeriod = new PreviousPeriod();
        tangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);

        if (includeCost) {
            costAtPeriodStart.setTotal(COST_AT_PERIOD_START);
            additions.setTotal(ADDITIONS);
            disposals.setTotal(DISPOSALS);
            revaluations.setTotal(REVALUATIONS);
            transfers.setTotal(TRANSFERS);
            costAtPeriodEnd.setTotal(COST_AT_PERIOD_END);
        }

        if (includeDepreciation) {
            depreciationAtPeriodStart.setTotal(DEPRECIATION_AT_PERIOD_START);
            chargeForYear.setTotal(CHARGE_FOR_YEAR);
            onDisposals.setTotal(ON_DISPOSALS);
            otherAdjustments.setTotal(OTHER_ADJUSTMENTS);
            depreciationAtPeriodEnd.setTotal(DEPRECIATION_AT_PERIOD_END);
        }

        if (includeNetBookValue) {
            currentPeriod.setTotal(CURRENT_PERIOD);
            previousPeriod.setTotal(PREVIOUS_PERIOD);
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
        assertEquals(CURRENT_PERIOD, tangibleAssets.getNetBookValue().getCurrentPeriod().getTotal());
        assertEquals(PREVIOUS_PERIOD, tangibleAssets.getNetBookValue().getPreviousPeriod().getTotal());
    }

    private void assertCostFieldsMappedToWebModel(TangibleAssets tangibleAssets) {
        assertEquals(COST_AT_PERIOD_START, tangibleAssets.getCost().getAtPeriodStart().getTotal());
        assertEquals(ADDITIONS, tangibleAssets.getCost().getAdditions().getTotal());
        assertEquals(DISPOSALS, tangibleAssets.getCost().getDisposals().getTotal());
        assertEquals(REVALUATIONS, tangibleAssets.getCost().getRevaluations().getTotal());
        assertEquals(TRANSFERS, tangibleAssets.getCost().getTransfers().getTotal());
        assertEquals(COST_AT_PERIOD_END, tangibleAssets.getCost().getAtPeriodEnd().getTotal());
    }

    private void assertDepreciationFieldsMappedToWebModel(TangibleAssets tangibleAssets) {
        assertEquals(DEPRECIATION_AT_PERIOD_START, tangibleAssets.getDepreciation().getAtPeriodStart().getTotal());
        assertEquals(CHARGE_FOR_YEAR, tangibleAssets.getDepreciation().getChargeForYear().getTotal());
        assertEquals(ON_DISPOSALS, tangibleAssets.getDepreciation().getOnDisposals().getTotal());
        assertEquals(OTHER_ADJUSTMENTS, tangibleAssets.getDepreciation().getOtherAdjustments().getTotal());
        assertEquals(DEPRECIATION_AT_PERIOD_END, tangibleAssets.getDepreciation().getAtPeriodEnd().getTotal());
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
        assertEquals(OTHER_COST_AT_PERIOD_START, tangibleAssets.getCost().getAtPeriodStart().getFixturesAndFittings());
        assertEquals(OTHER_ADDITIONS, tangibleAssets.getCost().getAdditions().getFixturesAndFittings());
        assertEquals(OTHER_DISPOSALS, tangibleAssets.getCost().getDisposals().getFixturesAndFittings());
        assertEquals(OTHER_REVALUATIONS, tangibleAssets.getCost().getRevaluations().getFixturesAndFittings());
        assertEquals(OTHER_TRANSFERS, tangibleAssets.getCost().getTransfers().getFixturesAndFittings());
        assertEquals(OTHER_COST_AT_PERIOD_END, tangibleAssets.getCost().getAtPeriodEnd().getFixturesAndFittings());
        assertEquals(OTHER_DEPRECIATION_AT_PERIOD_START, tangibleAssets.getDepreciation().getAtPeriodStart().getFixturesAndFittings());
        assertEquals(OTHER_CHARGE_FOR_YEAR, tangibleAssets.getDepreciation().getChargeForYear().getFixturesAndFittings());
        assertEquals(OTHER_ON_DISPOSALS, tangibleAssets.getDepreciation().getOnDisposals().getFixturesAndFittings());
        assertEquals(OTHER_OTHER_ADJUSTMENTS, tangibleAssets.getDepreciation().getOtherAdjustments().getFixturesAndFittings());
        assertEquals(OTHER_DEPRECIATION_AT_PERIOD_END, tangibleAssets.getDepreciation().getAtPeriodEnd().getFixturesAndFittings());
        assertEquals(OTHER_CURRENT_PERIOD, tangibleAssets.getNetBookValue().getCurrentPeriod().getFixturesAndFittings());
        assertEquals(OTHER_PREVIOUS_PERIOD, tangibleAssets.getNetBookValue().getPreviousPeriod().getFixturesAndFittings());
    }
}
