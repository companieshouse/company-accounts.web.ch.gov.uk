package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.Amortisation;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.Cost;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.AmortisationAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.AmortisationAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.ChargeForYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.IntangibleAssetsAmortisation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.OnDisposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.OtherAdjustments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Additions;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Disposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Revaluations;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Transfers;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsOtherIntangibleAssetsTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntangibleAssetsOtherIntangibleAssetsTransformerImplTests {

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

    private static final Long AMORTISATION_AT_PERIOD_START = 1L;
    private static final Long AMORTISATION_CHARGE_FOR_YEAR = 6L;
    private static final Long AMORTISATION_ON_DISPOSALS = 6L;
    private static final Long AMORTISATION_OTHER_ADJUSTMENTS = 6L;
    private static final Long AMORTISATION_AT_PERIOD_END = 7L;

    private static final Long OTHER_AMORTISATION_AT_PERIOD_START = 100L;
    private static final Long OTHER_AMORTISATION_CHARGE_FOR_YEAR = 200L;
    private static final Long OTHER_AMORTISATION_ON_DISPOSALS = 200L;
    private static final Long OTHER_AMORTISATION_OTHER_ADJUSTMENTS = 200L;
    private static final Long OTHER_AMORTISATION_AT_PERIOD_END = 300L;


    private IntangibleAssetsResourceTransformer transformer = new IntangibleAssetsOtherIntangibleAssetsTransformerImpl();

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an empty web model")
    void mapFullApiResourceToEmptyWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        IntangibleAssetsResource otherIntangibleAssets = createOtherIntangibleAssetsApiResource(true, true);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, otherIntangibleAssets);

        assertWebModelsMapped(intangibleAssets, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from an api resource which doesn't have a cost or amortisation object to an empty web model")
    void mapApiResourceWithoutCostOrAmortisationToEmptyWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        IntangibleAssetsResource otherIntangibleAssets = createOtherIntangibleAssetsApiResource(false, false);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, otherIntangibleAssets);

        assertWebModelsMapped(intangibleAssets, false, false);

        assertNull(intangibleAssets.getCost());
        assertNull(intangibleAssets.getAmortisation());
    }

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an web model with other existing resources")
    void mapFullApiResourceToPopulatedWebModel() {

        IntangibleAssets intangibleAssets = createIntangibleAssetsWithPreExistingResources();

        IntangibleAssetsResource otherIntangibleAssets = createOtherIntangibleAssetsApiResource(true, true);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, otherIntangibleAssets);

        assertWebModelsMapped(intangibleAssets, true, true);
        assertPreExistingFieldsUnaffected(intangibleAssets);
    }

    @Test
    @DisplayName("Tests all resources are mapped from a web model to an api resource")
    void mapFullWebModelToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithOtherIntangibleAssetsResources(true, true);

        IntangibleApi intangibleApi = new IntangibleApi();
        transformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);

        assertApiFieldsMapped(intangibleApi.getOtherIntangibleAssets(), true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without cost or amortisation to an api resource")
    void mapWebModelWithoutCostOrAmortisationToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithOtherIntangibleAssetsResources(false, false);

        IntangibleApi intangibleApi = new IntangibleApi();
        transformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);

        assertApiFieldsMapped(intangibleApi.getOtherIntangibleAssets(), false, false);
    }

    @Test
    @DisplayName("Tests intangible assets will be mapped for a fully populated web model")
    void hasIntangibleAssetsToMapToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithOtherIntangibleAssetsResources(true, true);

        assertTrue(transformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets));
    }

    @Test
    @DisplayName("Tests intangible assets will be mapped for a populated web model without any cost and amortisation values")
    void hasIntangibleAssetsToMapToApiResourceNoCostNoAmortisation() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithOtherIntangibleAssetsResources(false, false);

        assertFalse(transformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets));
    }

    private IntangibleAssetsResource createOtherIntangibleAssetsApiResource(boolean includeCost, boolean includeAmortisation) {

        IntangibleAssetsResource otherIntangibleAssets = new IntangibleAssetsResource();

        if (includeCost) {
            otherIntangibleAssets.setCost(createCostApiResource());
        }

        if(includeAmortisation) {
            otherIntangibleAssets.setAmortisation(createAmortisationApiResource());
        }

        return otherIntangibleAssets;
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

    private Amortisation createAmortisationApiResource() {

        Amortisation amortisation = new Amortisation();
        amortisation.setAtPeriodStart(AMORTISATION_AT_PERIOD_START);
        amortisation.setChargeForYear(AMORTISATION_CHARGE_FOR_YEAR);
        amortisation.setOnDisposals(AMORTISATION_ON_DISPOSALS);
        amortisation.setOtherAdjustments(AMORTISATION_OTHER_ADJUSTMENTS);
        amortisation.setAtPeriodEnd(AMORTISATION_AT_PERIOD_END);
        return amortisation;
    }

    private IntangibleAssets createIntangibleAssetsWithPreExistingResources() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();

        IntangibleAssetsCost intangibleAssetsCost = new IntangibleAssetsCost();
        IntangibleAssetsAmortisation intangibleAssetsAmortisation = new IntangibleAssetsAmortisation();

        CostAtPeriodStart costAtPeriodStart = new CostAtPeriodStart();
        costAtPeriodStart.setOtherIntangibleAssets(OTHER_COST_AT_PERIOD_START);
        intangibleAssetsCost.setAtPeriodStart(costAtPeriodStart);

        Additions additions = new Additions();
        additions.setOtherIntangibleAssets(OTHER_ADDITIONS);
        intangibleAssetsCost.setAdditions(additions);

        Disposals disposals = new Disposals();
        disposals.setOtherIntangibleAssets(OTHER_DISPOSALS);
        intangibleAssetsCost.setDisposals(disposals);

        Revaluations revaluations = new Revaluations();
        revaluations.setOtherIntangibleAssets(OTHER_REVALUATIONS);
        intangibleAssetsCost.setRevaluations(revaluations);

        Transfers transfers = new Transfers();
        transfers.setOtherIntangibleAssets(OTHER_TRANSFERS);
        intangibleAssetsCost.setTransfers(transfers);

        CostAtPeriodEnd costAtPeriodEnd = new CostAtPeriodEnd();
        costAtPeriodEnd.setOtherIntangibleAssets(OTHER_COST_AT_PERIOD_END);
        intangibleAssetsCost.setAtPeriodEnd(costAtPeriodEnd);

        AmortisationAtPeriodStart amortisationAtPeriodStart = new AmortisationAtPeriodStart();
        amortisationAtPeriodStart.setOtherIntangibleAssets(OTHER_AMORTISATION_AT_PERIOD_START);
        intangibleAssetsAmortisation.setAtPeriodStart(amortisationAtPeriodStart);

        ChargeForYear chargeForYear = new ChargeForYear();
        chargeForYear.setOtherIntangibleAssets(OTHER_AMORTISATION_CHARGE_FOR_YEAR);
        intangibleAssetsAmortisation.setChargeForYear(chargeForYear);

        OnDisposals onDisposals = new OnDisposals();
        onDisposals.setOtherIntangibleAssets(OTHER_AMORTISATION_ON_DISPOSALS);
        intangibleAssetsAmortisation.setOnDisposals(onDisposals);

        OtherAdjustments otherAdjustments = new OtherAdjustments();
        otherAdjustments.setOtherIntangibleAssets(OTHER_AMORTISATION_OTHER_ADJUSTMENTS);
        intangibleAssetsAmortisation.setOtherAdjustments(otherAdjustments);

        AmortisationAtPeriodEnd amortisationAtPeriodEnd = new AmortisationAtPeriodEnd();
        amortisationAtPeriodEnd.setOtherIntangibleAssets(OTHER_AMORTISATION_AT_PERIOD_END);
        intangibleAssetsAmortisation.setAtPeriodEnd(amortisationAtPeriodEnd);

        intangibleAssets.setCost(intangibleAssetsCost);
        intangibleAssets.setAmortisation(intangibleAssetsAmortisation);

        return intangibleAssets;
    }

    private void assertPreExistingFieldsUnaffected(IntangibleAssets intangibleAssets) {

        assertEquals(COST_AT_PERIOD_START, intangibleAssets.getCost().getAtPeriodStart().getOtherIntangibleAssets());
        assertEquals(ADDITIONS, intangibleAssets.getCost().getAdditions().getOtherIntangibleAssets());
        assertEquals(DISPOSALS, intangibleAssets.getCost().getDisposals().getOtherIntangibleAssets());
        assertEquals(REVALUATIONS, intangibleAssets.getCost().getRevaluations().getOtherIntangibleAssets());
        assertEquals(TRANSFERS, intangibleAssets.getCost().getTransfers().getOtherIntangibleAssets());
        assertEquals(COST_AT_PERIOD_END, intangibleAssets.getCost().getAtPeriodEnd().getOtherIntangibleAssets());

        assertEquals(AMORTISATION_AT_PERIOD_START, intangibleAssets.getAmortisation().getAtPeriodStart().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_CHARGE_FOR_YEAR, intangibleAssets.getAmortisation().getChargeForYear().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_ON_DISPOSALS, intangibleAssets.getAmortisation().getOnDisposals().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_OTHER_ADJUSTMENTS, intangibleAssets.getAmortisation().getOtherAdjustments().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_AT_PERIOD_END, intangibleAssets.getAmortisation().getAtPeriodEnd().getOtherIntangibleAssets());
    }

    private IntangibleAssets createIntangibleAssetsWithOtherIntangibleAssetsResources(boolean includeCost, boolean includeAmortisation) {

        IntangibleAssets intangibleAssets = new IntangibleAssets();

        IntangibleAssetsCost intangibleAssetsCost = new IntangibleAssetsCost();
        intangibleAssets.setCost(intangibleAssetsCost);

        IntangibleAssetsAmortisation intangibleAssetsAmortisation = new IntangibleAssetsAmortisation();
        intangibleAssets.setAmortisation(intangibleAssetsAmortisation);

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

            costAtPeriodStart.setOtherIntangibleAssets(COST_AT_PERIOD_START);
            additions.setOtherIntangibleAssets(ADDITIONS);
            disposals.setOtherIntangibleAssets(DISPOSALS);
            revaluations.setOtherIntangibleAssets(REVALUATIONS);
            transfers.setOtherIntangibleAssets(TRANSFERS);
            costAtPeriodEnd.setOtherIntangibleAssets(COST_AT_PERIOD_END);
        }

        AmortisationAtPeriodStart amortisationAtPeriodStart = new AmortisationAtPeriodStart();
        intangibleAssetsAmortisation.setAtPeriodStart(amortisationAtPeriodStart);

        ChargeForYear chargeForYear = new ChargeForYear();
        intangibleAssetsAmortisation.setChargeForYear(chargeForYear);

        OnDisposals onDisposals = new OnDisposals();
        intangibleAssetsAmortisation.setOnDisposals(onDisposals);

        OtherAdjustments otherAdjustments = new OtherAdjustments();
        intangibleAssetsAmortisation.setOtherAdjustments(otherAdjustments);

        AmortisationAtPeriodEnd amortisationAtPeriodEnd = new AmortisationAtPeriodEnd();
        intangibleAssetsAmortisation.setAtPeriodEnd(amortisationAtPeriodEnd);

        if (includeAmortisation) {

            amortisationAtPeriodStart.setOtherIntangibleAssets(AMORTISATION_AT_PERIOD_START);
            chargeForYear.setOtherIntangibleAssets(AMORTISATION_CHARGE_FOR_YEAR);
            onDisposals.setOtherIntangibleAssets(AMORTISATION_ON_DISPOSALS);
            otherAdjustments.setOtherIntangibleAssets(AMORTISATION_OTHER_ADJUSTMENTS);
            amortisationAtPeriodEnd.setOtherIntangibleAssets(AMORTISATION_AT_PERIOD_END);
        }

        return intangibleAssets;
    }

    private void assertWebModelsMapped(IntangibleAssets intangibleAssets,
                                       boolean expectCostFieldsMapped, boolean expectAmortisationFieldsMapped) {

        if (expectCostFieldsMapped) {
            assertCostFieldsMappedToWebModel(intangibleAssets);
        }

        if(expectAmortisationFieldsMapped) {
            assertAmortisationFieldsMappedToWebModel(intangibleAssets);
        }
    }

    private void assertCostFieldsMappedToWebModel(IntangibleAssets intangibleAssets) {

        assertEquals(COST_AT_PERIOD_START, intangibleAssets.getCost().getAtPeriodStart().getOtherIntangibleAssets());
        assertEquals(ADDITIONS, intangibleAssets.getCost().getAdditions().getOtherIntangibleAssets());
        assertEquals(DISPOSALS, intangibleAssets.getCost().getDisposals().getOtherIntangibleAssets());
        assertEquals(REVALUATIONS, intangibleAssets.getCost().getRevaluations().getOtherIntangibleAssets());
        assertEquals(TRANSFERS, intangibleAssets.getCost().getTransfers().getOtherIntangibleAssets());
        assertEquals(COST_AT_PERIOD_END, intangibleAssets.getCost().getAtPeriodEnd().getOtherIntangibleAssets());
    }

    private void assertAmortisationFieldsMappedToWebModel(IntangibleAssets intangibleAssets) {

        assertEquals(AMORTISATION_AT_PERIOD_START, intangibleAssets.getAmortisation().getAtPeriodStart().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_CHARGE_FOR_YEAR, intangibleAssets.getAmortisation().getChargeForYear().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_ON_DISPOSALS, intangibleAssets.getAmortisation().getOnDisposals().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_OTHER_ADJUSTMENTS, intangibleAssets.getAmortisation().getOtherAdjustments().getOtherIntangibleAssets());
        assertEquals(AMORTISATION_AT_PERIOD_END, intangibleAssets.getAmortisation().getAtPeriodEnd().getOtherIntangibleAssets());
    }

    private void assertApiFieldsMapped(IntangibleAssetsResource resource,
                                       boolean expectCostFieldsMapped, boolean expectAmortisationFieldsMapped) {

        if (expectCostFieldsMapped) {
            assertCostFieldsMappedToApiResource(resource.getCost());
        } else {
            assertNull(resource.getCost());
        }

        if (expectAmortisationFieldsMapped) {
            assertAmortisationFieldsMappedToApiResource(resource.getAmortisation());
        }
        else {
            assertNull(resource.getAmortisation());
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

    private void assertAmortisationFieldsMappedToApiResource(Amortisation amortisation) {
        assertNotNull(amortisation);
        assertEquals(AMORTISATION_AT_PERIOD_START, amortisation.getAtPeriodStart());
        assertEquals(AMORTISATION_CHARGE_FOR_YEAR, amortisation.getChargeForYear());
        assertEquals(AMORTISATION_ON_DISPOSALS, amortisation.getOnDisposals());
        assertEquals(AMORTISATION_OTHER_ADJUSTMENTS, amortisation.getOtherAdjustments());
        assertEquals(AMORTISATION_AT_PERIOD_END, amortisation.getAtPeriodEnd());
    }
}
