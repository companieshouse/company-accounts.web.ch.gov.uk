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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue.IntangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl.IntangibleAssetsTotalTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntangibleAssetsTotalTransformerImplTests {

    private static final Long COST_AT_PERIOD_START = 1L;
    private static final Long ADDITIONS = 2L;
    private static final Long DISPOSALS = 3L;
    private static final Long REVALUATIONS = 4L;
    private static final Long TRANSFERS = 5L;
    private static final Long COST_AT_PERIOD_END = 6L;
    private static final Long CURRENT_PERIOD = 12L;
    private static final Long PREVIOUS_PERIOD = 13L;

    private static final Long OTHER_COST_AT_PERIOD_START = 100L;
    private static final Long OTHER_ADDITIONS = 200L;
    private static final Long OTHER_DISPOSALS = 300L;
    private static final Long OTHER_REVALUATIONS = 400L;
    private static final Long OTHER_TRANSFERS = 500L;
    private static final Long OTHER_COST_AT_PERIOD_END = 600L;

    private static final Long AMORTISATION_AT_PERIOD_START = 7L;
    private static final Long AMORTISATION_CHARGE_FOR_YEAR = 8L;
    private static final Long AMORTISATION_ON_DISPOSALS = 9L;
    private static final Long AMORTISATION_OTHER_ADJUSTMENTS = 10L;
    private static final Long AMORTISATION_AT_PERIOD_END = 11L;

    private static final Long OTHER_AMORTISATION_AT_PERIOD_START = 700L;
    private static final Long OTHER_AMORTISATION_CHARGE_FOR_YEAR = 800L;
    private static final Long OTHER_AMORTISATION_ON_DISPOSALS = 900L;
    private static final Long OTHER_AMORTISATION_OTHER_ADJUSTMENTS = 1000L;
    private static final Long OTHER_AMORTISATION_AT_PERIOD_END = 1100L;
    private static final Long OTHER_CURRENT_PERIOD = 1200L;
    private static final Long OTHER_PREVIOUS_PERIOD = 1300L;

    private IntangibleAssetsResourceTransformer transformer = new IntangibleAssetsTotalTransformerImpl();

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an empty web model")
    void mapFullApiResourceToEmptyWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        IntangibleAssetsResource total = createTotalApiResource(true, true);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, total);

        assertWebModelsMapped(intangibleAssets, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from an api resource which doesn't have a cost or amortisation object to an empty web model")
    void mapApiResourceWithoutCostOrAmortisationToEmptyWebModel() {

        IntangibleAssets intangibleAssets = new IntangibleAssets();
        IntangibleAssetsResource total = createTotalApiResource(false, false);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, total);

        assertWebModelsMapped(intangibleAssets, false, false);

        assertNull(intangibleAssets.getCost());
        assertNull(intangibleAssets.getAmortisation());
    }

    @Test
    @DisplayName("Tests all resources are mapped from an api resource to an web model with other existing resources")
    void mapFullApiResourceToPopulatedWebModel() {

        IntangibleAssets intangibleAssets = createIntangibleAssetsWithPreExistingResources();

        IntangibleAssetsResource total = createTotalApiResource(true, true);
        transformer.mapIntangibleAssetsResourceToWebModel(intangibleAssets, total);

        assertWebModelsMapped(intangibleAssets, true, true);
        assertPreExistingFieldsUnaffected(intangibleAssets);
    }

    @Test
    @DisplayName("Tests all resources are mapped from a web model to an api resource")
    void mapFullWebModelToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(true, true, true);

        IntangibleApi intangibleApi = new IntangibleApi();
        transformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);

        assertApiFieldsMapped(intangibleApi.getTotal(), true, true, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without cost or amortisation to an api resource")
    void mapWebModelWithoutCostToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(false, false, true);

        IntangibleApi intangibleApi = new IntangibleApi();
        transformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);

        assertApiFieldsMapped(intangibleApi.getTotal(), false, false, true);
    }

    @Test
    @DisplayName("Tests resources are mapped from a web model without net book values to an api resource")
    void mapWebModelWithoutNetBookValuesToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(true, true, false);

        IntangibleApi intangibleApi = new IntangibleApi();
        transformer.mapIntangibleAssetsToApiResource(intangibleAssets, intangibleApi);

        assertApiFieldsMapped(intangibleApi.getTotal(), true, true, false);
    }

    @Test
    @DisplayName("Tests intangible assets will be mapped for a fully populated web model")
    void hasIntangibleAssetsToMapToApiResource() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(true, true, true);

        assertTrue(transformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets));
    }

    @Test
    @DisplayName("Tests intangible assets will be mapped for a populated web model without any cost and amortisation values")
    void hasIntangibleAssetsToMapToApiResourceNoCostNoAmortisation() {

        IntangibleAssets intangibleAssets =
                createIntangibleAssetsWithTotalResources(false, false, false);

        assertFalse(transformer.hasIntangibleAssetsToMapToApiResource(intangibleAssets));
    }

    private IntangibleAssetsResource createTotalApiResource(boolean includeCost, boolean includeAmortisation) {

        IntangibleAssetsResource total = new IntangibleAssetsResource();

        if (includeCost) {
            total.setCost(createCostApiResource());
        }

        if(includeAmortisation) {
            total.setAmortisation(createAmortisationApiResource());
        }

        total.setNetBookValueAtEndOfPreviousPeriod(PREVIOUS_PERIOD);
        total.setNetBookValueAtEndOfCurrentPeriod(CURRENT_PERIOD);

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

        IntangibleAssetsNetBookValue intangibleAssetsNetBookValue = new IntangibleAssetsNetBookValue();

        CurrentPeriod currentPeriod = new CurrentPeriod();
        currentPeriod.setOtherIntangibleAssets(OTHER_CURRENT_PERIOD);
        intangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);

        PreviousPeriod previousPeriod = new PreviousPeriod();
        previousPeriod.setOther(OTHER_PREVIOUS_PERIOD);
        intangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);

        intangibleAssets.setNetBookValue(intangibleAssetsNetBookValue);


        return intangibleAssets;
    }

    private void assertPreExistingFieldsUnaffected(IntangibleAssets intangibleAssets) {

        assertEquals(OTHER_COST_AT_PERIOD_START, intangibleAssets.getCost().getAtPeriodStart().getOtherIntangibleAssets());
        assertEquals(OTHER_ADDITIONS, intangibleAssets.getCost().getAdditions().getOtherIntangibleAssets());
        assertEquals(OTHER_DISPOSALS, intangibleAssets.getCost().getDisposals().getOtherIntangibleAssets());
        assertEquals(OTHER_REVALUATIONS, intangibleAssets.getCost().getRevaluations().getOtherIntangibleAssets());
        assertEquals(OTHER_TRANSFERS, intangibleAssets.getCost().getTransfers().getOtherIntangibleAssets());
        assertEquals(OTHER_COST_AT_PERIOD_END, intangibleAssets.getCost().getAtPeriodEnd().getOtherIntangibleAssets());

        assertEquals(OTHER_AMORTISATION_AT_PERIOD_START, intangibleAssets.getAmortisation().getAtPeriodStart().getOtherIntangibleAssets());
        assertEquals(OTHER_AMORTISATION_CHARGE_FOR_YEAR, intangibleAssets.getAmortisation().getChargeForYear().getOtherIntangibleAssets());
        assertEquals(OTHER_AMORTISATION_ON_DISPOSALS, intangibleAssets.getAmortisation().getOnDisposals().getOtherIntangibleAssets());
        assertEquals(OTHER_AMORTISATION_OTHER_ADJUSTMENTS, intangibleAssets.getAmortisation().getOtherAdjustments().getOtherIntangibleAssets());
        assertEquals(OTHER_AMORTISATION_AT_PERIOD_END, intangibleAssets.getAmortisation().getAtPeriodEnd().getOtherIntangibleAssets());
    }

    private IntangibleAssets createIntangibleAssetsWithTotalResources(boolean includeCost, boolean includeAmortisation, boolean includeNetBookValue) {

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

        IntangibleAssetsNetBookValue intangibleAssetsNetBookValue = new IntangibleAssetsNetBookValue();
        intangibleAssets.setNetBookValue(intangibleAssetsNetBookValue);

        CurrentPeriod currentPeriod = new CurrentPeriod();
        intangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);

        PreviousPeriod previousPeriod = new PreviousPeriod();
        intangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);

        if (includeCost) {

            costAtPeriodStart.setTotal(COST_AT_PERIOD_START);
            additions.setTotal(ADDITIONS);
            disposals.setTotal(DISPOSALS);
            revaluations.setTotal(REVALUATIONS);
            transfers.setTotal(TRANSFERS);
            costAtPeriodEnd.setTotal(COST_AT_PERIOD_END);
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

            amortisationAtPeriodStart.setTotal(AMORTISATION_AT_PERIOD_START);
            chargeForYear.setTotal(AMORTISATION_CHARGE_FOR_YEAR);
            onDisposals.setTotal(AMORTISATION_ON_DISPOSALS);
            otherAdjustments.setTotal(AMORTISATION_OTHER_ADJUSTMENTS);
            amortisationAtPeriodEnd.setTotal(AMORTISATION_AT_PERIOD_END);
        }

        if (includeNetBookValue) {

            currentPeriod.setTotal(CURRENT_PERIOD);
            previousPeriod.setTotal(PREVIOUS_PERIOD);
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
        assertEquals(CURRENT_PERIOD, intangibleAssets.getNetBookValue().getCurrentPeriod().getTotal());
        assertEquals(PREVIOUS_PERIOD, intangibleAssets.getNetBookValue().getPreviousPeriod().getTotal());
    }

    private void assertCostFieldsMappedToWebModel(IntangibleAssets intangibleAssets) {

        assertEquals(COST_AT_PERIOD_START, intangibleAssets.getCost().getAtPeriodStart().getTotal());
        assertEquals(ADDITIONS, intangibleAssets.getCost().getAdditions().getTotal());
        assertEquals(DISPOSALS, intangibleAssets.getCost().getDisposals().getTotal());
        assertEquals(REVALUATIONS, intangibleAssets.getCost().getRevaluations().getTotal());
        assertEquals(TRANSFERS, intangibleAssets.getCost().getTransfers().getTotal());
        assertEquals(COST_AT_PERIOD_END, intangibleAssets.getCost().getAtPeriodEnd().getTotal());
    }

    private void assertAmortisationFieldsMappedToWebModel(IntangibleAssets intangibleAssets) {

        assertEquals(AMORTISATION_AT_PERIOD_START, intangibleAssets.getAmortisation().getAtPeriodStart().getTotal());
        assertEquals(AMORTISATION_CHARGE_FOR_YEAR, intangibleAssets.getAmortisation().getChargeForYear().getTotal());
        assertEquals(AMORTISATION_ON_DISPOSALS, intangibleAssets.getAmortisation().getOnDisposals().getTotal());
        assertEquals(AMORTISATION_OTHER_ADJUSTMENTS, intangibleAssets.getAmortisation().getOtherAdjustments().getTotal());
        assertEquals(AMORTISATION_AT_PERIOD_END, intangibleAssets.getAmortisation().getAtPeriodEnd().getTotal());
    }

    private void assertApiFieldsMapped(IntangibleAssetsResource resource,
                                       boolean expectCostFieldsMapped, boolean expectAmortisationFieldsMapped,
                                        boolean expectNetBookValueFieldsMapped) {

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

    private void assertAmortisationFieldsMappedToApiResource(Amortisation amortisation) {
        assertNotNull(amortisation);
        assertEquals(AMORTISATION_AT_PERIOD_START, amortisation.getAtPeriodStart());
        assertEquals(AMORTISATION_CHARGE_FOR_YEAR, amortisation.getChargeForYear());
        assertEquals(AMORTISATION_ON_DISPOSALS, amortisation.getOnDisposals());
        assertEquals(AMORTISATION_OTHER_ADJUSTMENTS, amortisation.getOtherAdjustments());
        assertEquals(AMORTISATION_AT_PERIOD_END, amortisation.getAtPeriodEnd());
    }

    private void assertNetBookValueFieldsMappedToApiResource(IntangibleAssetsResource resource) {
        assertEquals(CURRENT_PERIOD, resource.getNetBookValueAtEndOfCurrentPeriod());
        assertEquals(PREVIOUS_PERIOD, resource.getNetBookValueAtEndOfPreviousPeriod());
    }
}
