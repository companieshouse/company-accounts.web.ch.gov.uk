package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible;

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
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.Cost;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Additions;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Disposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Revaluations;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Transfers;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsTotalTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntangibleAssetsTotalTransformerImplTests {

    private static final Long COST_AT_PERIOD_START = 1L;
    private static final Long ADDITIONS = 2L;
    private static final Long DISPOSALS = 3L;
    private static final Long REVALUATIONS = 4L;
    private static final Long TRANSFERS = 5L;
    private static final Long COST_AT_PERIOD_END = 6L;

    private static final Long OTHER_COST_AT_PERIOD_START = 100L;
    private static final Long OTHER_ADDITIONS = 200L;
    private static final Long OTHER_DISPOSALS = 300L;
    private static final Long OTHER_REVALUATIONS = 400L;
    private static final Long OTHER_TRANSFERS = 500L;
    private static final Long OTHER_COST_AT_PERIOD_END = 600L;

    private IntangibleAssetsResourceTransformer transformer = new IntangibleAssetsTotalTransformerImpl();

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an empty web model")
    void mapFullApiResourceToEmptyWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        IntangibleAssetsResource total = createTotalApiResource(true);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, total);

        assertWebModelsMapped(intangibleAssets, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from an api resource which doesn't have a cost object to an empty web model")
    void mapApiResourceWithoutCostToEmptyWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        IntangibleAssetsResource total = createTotalApiResource(false);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, total);

        assertWebModelsMapped(intangibleAssets, false);
        assertNull(intangibleAssets.getCost());
    }

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an web model with other existing resources")
    void mapFullApiResourceToPopulatedWebModel() {

        IntangibleAssets intangibleAssets = createIntangibleAssetsWithPreExistingResources();

        IntangibleAssetsResource total = createTotalApiResource(true);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, total);

        assertWebModelsMapped(intangibleAssets, true);
        assertPreExistingFieldsUnaffected(intangibleAssets);
    }

    @Test
    @DisplayName("Tests all resources are mapped from a web model to an api resource")
    void mapFullWebModelToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(true);

        IntangibleApi intangibleApi = new IntangibleApi();
        transformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);

        assertApiFieldsMapped(intangibleApi.getTotal(), true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without cost to an api resource")
    void mapWebModelWithoutCostToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(false);

        IntangibleApi intangibleApi = new IntangibleApi();
        transformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);

        assertApiFieldsMapped(intangibleApi.getTotal(), false);
    }

    @Test
    @DisplayName("Tests intangible assets will be mapped for a fully populated web model")
    void hasIntangibleAssetsToMapToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(true);

        assertTrue(transformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets));
    }

    @Test
    @DisplayName("Tests intangible assets will be mapped for a populated web model without any cost values")
    void hasIntangibleAssetsToMapToApiResourceNoCost() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(false);

        assertFalse(transformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets));
    }

    private IntangibleAssetsResource createTotalApiResource(boolean includeCost) {

        IntangibleAssetsResource total = new IntangibleAssetsResource();

        if (includeCost) {
            total.setCost(createCostApiResource());
        }

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

    private IntangibleAssets createIntangibleAssetsWithPreExistingResources() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();

        IntangibleAssetsCost intangibleAssetsCost = new IntangibleAssetsCost();

        CostAtPeriodStart costAtPeriodStart = new CostAtPeriodStart();
        costAtPeriodStart.setGoodwill(OTHER_COST_AT_PERIOD_START);
        intangibleAssetsCost.setAtPeriodStart(costAtPeriodStart);

        Additions additions = new Additions();
        additions.setGoodwill(OTHER_ADDITIONS);
        intangibleAssetsCost.setAdditions(additions);

        Disposals disposals = new Disposals();
        disposals.setGoodwill(OTHER_DISPOSALS);
        intangibleAssetsCost.setDisposals(disposals);

        Revaluations revaluations = new Revaluations();
        revaluations.setGoodwill(OTHER_REVALUATIONS);
        intangibleAssetsCost.setRevaluations(revaluations);

        Transfers transfers = new Transfers();
        transfers.setGoodwill(OTHER_TRANSFERS);
        intangibleAssetsCost.setTransfers(transfers);

        CostAtPeriodEnd costAtPeriodEnd = new CostAtPeriodEnd();
        costAtPeriodEnd.setGoodwill(OTHER_COST_AT_PERIOD_END);
        intangibleAssetsCost.setAtPeriodEnd(costAtPeriodEnd);

        intangibleAssets.setCost(intangibleAssetsCost);

        return intangibleAssets;
    }

    private void assertPreExistingFieldsUnaffected(IntangibleAssets intangibleAssets) {

        assertEquals(OTHER_COST_AT_PERIOD_START, intangibleAssets.getCost().getAtPeriodStart().getGoodwill());
        assertEquals(OTHER_ADDITIONS, intangibleAssets.getCost().getAdditions().getGoodwill());
        assertEquals(OTHER_DISPOSALS, intangibleAssets.getCost().getDisposals().getGoodwill());
        assertEquals(OTHER_REVALUATIONS, intangibleAssets.getCost().getRevaluations().getGoodwill());
        assertEquals(OTHER_TRANSFERS, intangibleAssets.getCost().getTransfers().getGoodwill());
        assertEquals(OTHER_COST_AT_PERIOD_END, intangibleAssets.getCost().getAtPeriodEnd().getGoodwill());
    }

    private IntangibleAssets createIntangibleAssetsWithTotalResources(boolean includeCost) {

        IntangibleAssets intangibleAssets = new IntangibleAssets();

        IntangibleAssetsCost intangibleAssetsCost = new IntangibleAssetsCost();
        intangibleAssets.setCost(intangibleAssetsCost);

        CostAtPeriodStart costAtPeriodStart = new CostAtPeriodStart();
        intangibleAssetsCost.setAtPeriodStart(costAtPeriodStart);

        Additions additions = new Additions();
        intangibleAssetsCost.setAdditions(additions);

        Disposals disposals = new Disposals();
        intangibleAssetsCost.setDisposals(disposals);

        Revaluations revaluations = new Revaluations();
        intangibleAssetsCost.setRevaluations(revaluations);

        Transfers transfers = new Transfers();
        intangibleAssetsCost.setTransfers(transfers);

        CostAtPeriodEnd costAtPeriodEnd = new CostAtPeriodEnd();
        intangibleAssetsCost.setAtPeriodEnd(costAtPeriodEnd);

        if (includeCost) {

            costAtPeriodStart.setTotal(COST_AT_PERIOD_START);
            additions.setTotal(ADDITIONS);
            disposals.setTotal(DISPOSALS);
            revaluations.setTotal(REVALUATIONS);
            transfers.setTotal(TRANSFERS);
            costAtPeriodEnd.setTotal(COST_AT_PERIOD_END);
        }

        return intangibleAssets;
    }

    private void assertWebModelsMapped(IntangibleAssets intangibleAssets,
                                       boolean expectCostFieldsMapped) {

        if (expectCostFieldsMapped) {
            assertCostFieldsMappedToWebModel(intangibleAssets);
        }
    }

    private void assertCostFieldsMappedToWebModel(IntangibleAssets intangibleAssets) {

        assertEquals(COST_AT_PERIOD_START, intangibleAssets.getCost().getAtPeriodStart().getTotal());
        assertEquals(ADDITIONS, intangibleAssets.getCost().getAdditions().getTotal());
        assertEquals(DISPOSALS, intangibleAssets.getCost().getDisposals().getTotal());
        assertEquals(REVALUATIONS, intangibleAssets.getCost().getRevaluations().getTotal());
        assertEquals(TRANSFERS, intangibleAssets.getCost().getTransfers().getTotal());
        assertEquals(COST_AT_PERIOD_END, intangibleAssets.getCost().getAtPeriodEnd().getTotal());
    }

    private void assertApiFieldsMapped(IntangibleAssetsResource resource,
                                       boolean expectCostFieldsMapped) {

        if (expectCostFieldsMapped) {
            assertCostFieldsMappedToApiResource(resource.getCost());
        } else {
            assertNull(resource.getCost());
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
}
