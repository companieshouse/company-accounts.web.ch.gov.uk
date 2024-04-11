package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl;

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
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsResourceTransformer;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class IntangibleAssetsOtherIntangibleAssetsTransformerImpl extends
        IntangibleAssetsResourceTransformerImpl implements IntangibleAssetsResourceTransformer {
    @Override
    public void mapIntangibleAssetsResourceToWebModel(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {
        if (intangibleAssetsResource.getCost() != null) {
            IntangibleAssetsCost intangibleAssetsCost = createCost(intangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(intangibleAssetsCost);
            atPeriodStart
                    .setOtherIntangibleAssets(intangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(intangibleAssetsCost);
            additions.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(intangibleAssetsCost);
            disposals.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(intangibleAssetsCost);
            revaluations.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(intangibleAssetsCost);
            transfers.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(intangibleAssetsCost);
            atPeriodEnd.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (intangibleAssetsResource.getAmortisation() != null) {
            IntangibleAssetsAmortisation intangibleAssetsAmortisation = createAmortisation(intangibleAssets);

            AmortisationAtPeriodStart atPeriodStart = createAmortisationAtPeriodStart(intangibleAssetsAmortisation);
            atPeriodStart.setOtherIntangibleAssets(intangibleAssetsResource.getAmortisation().getAtPeriodStart());

            ChargeForYear chargeForYear = createAmortisationChargeForYear(intangibleAssetsAmortisation);
            chargeForYear.setOtherIntangibleAssets(intangibleAssetsResource.getAmortisation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(intangibleAssetsAmortisation);
            onDisposals.setOtherIntangibleAssets(intangibleAssetsResource.getAmortisation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(intangibleAssetsAmortisation);
            otherAdjustments.setOtherIntangibleAssets(intangibleAssetsResource.getAmortisation().getOtherAdjustments());

            AmortisationAtPeriodEnd atPeriodEnd = createAmortisationAtPeriodEnd(intangibleAssetsAmortisation);
            atPeriodEnd.setOtherIntangibleAssets(intangibleAssetsResource.getAmortisation().getAtPeriodEnd());
        }

        IntangibleAssetsNetBookValue intangibleAssetsNetBookValue = createNetBookValue(intangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(intangibleAssetsNetBookValue);
        currentPeriod.setOtherIntangibleAssets(intangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(intangibleAssetsNetBookValue);
        previousPeriod.setOtherIntangibleAssets(intangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
    }

    @Override
    public boolean hasIntangibleAssetsToMapToApiResource(IntangibleAssets intangibleAssets) {
        return hasCostResources(intangibleAssets) ||
                hasAmortisationResources(intangibleAssets) ||
                hasNetBookValueResources(intangibleAssets);
    }

    @Override
    public void mapIntangibleAssetsToApiResource(IntangibleAssets intangibleAssets, IntangibleApi intangibleApi) {
        IntangibleAssetsResource otherIntangibleAssets = new IntangibleAssetsResource();

        if (hasCostResources(intangibleAssets)) {
            mapCostResources(intangibleAssets, otherIntangibleAssets);
        }

        if (hasAmortisationResources(intangibleAssets)) {
            mapAmortisationResources(intangibleAssets, otherIntangibleAssets);
        }

        if (hasNetBookValueResources(intangibleAssets)) {
            mapNetBookValueResources(intangibleAssets, otherIntangibleAssets);
        }

        intangibleApi.setOtherIntangibleAssets(otherIntangibleAssets);
    }

    @Override
    protected boolean hasCostResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsCost cost = intangibleAssets.getCost();

        return Stream
                .of(Optional.of(cost)
                        .map(IntangibleAssetsCost::getAtPeriodStart)
                        .map(CostAtPeriodStart::getOtherIntangibleAssets)
                        .orElse(null),
                cost.getAdditions().getOtherIntangibleAssets(),
                cost.getDisposals().getOtherIntangibleAssets(),
                cost.getRevaluations().getOtherIntangibleAssets(),
                cost.getTransfers().getOtherIntangibleAssets(),
                cost.getAtPeriodEnd().getOtherIntangibleAssets())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasAmortisationResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsAmortisation amortisation = intangibleAssets.getAmortisation();

        return Stream
                .of(Optional.of(amortisation)
                        .map(IntangibleAssetsAmortisation::getAtPeriodStart)
                        .map(AmortisationAtPeriodStart::getOtherIntangibleAssets)
                        .orElse(null),
                amortisation.getChargeForYear().getOtherIntangibleAssets(),
                amortisation.getOnDisposals().getOtherIntangibleAssets(),
                amortisation.getOtherAdjustments().getOtherIntangibleAssets(),
                amortisation.getAtPeriodEnd().getOtherIntangibleAssets())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsNetBookValue netBookValue = intangibleAssets.getNetBookValue();

        return Stream.of(Optional.of(netBookValue)
                        .map(IntangibleAssetsNetBookValue::getPreviousPeriod)
                        .map(PreviousPeriod::getOtherIntangibleAssets)
                        .orElse(null),
                netBookValue.getCurrentPeriod().getOtherIntangibleAssets())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {
        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getCost)
                .map(IntangibleAssetsCost::getAtPeriodStart)
                .map(CostAtPeriodStart::getOtherIntangibleAssets)
                .orElse(null));
        cost.setAdditions(intangibleAssets.getCost().getAdditions().getOtherIntangibleAssets());
        cost.setDisposals(intangibleAssets.getCost().getDisposals().getOtherIntangibleAssets());
        cost.setRevaluations(intangibleAssets.getCost().getRevaluations().getOtherIntangibleAssets());
        cost.setTransfers(intangibleAssets.getCost().getTransfers().getOtherIntangibleAssets());
        cost.setAtPeriodEnd(intangibleAssets.getCost().getAtPeriodEnd().getOtherIntangibleAssets());
        intangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapAmortisationResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {
        Amortisation amortisation = new Amortisation();
        amortisation.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getAmortisation)
                .map(IntangibleAssetsAmortisation::getAtPeriodStart)
                .map(AmortisationAtPeriodStart::getOtherIntangibleAssets)
                .orElse(null));
        amortisation.setChargeForYear(intangibleAssets.getAmortisation().getChargeForYear().getOtherIntangibleAssets());
        amortisation.setOnDisposals(intangibleAssets.getAmortisation().getOnDisposals().getOtherIntangibleAssets());
        amortisation.setOtherAdjustments(intangibleAssets.getAmortisation().getOtherAdjustments().getOtherIntangibleAssets());
        amortisation.setAtPeriodEnd(intangibleAssets.getAmortisation().getAtPeriodEnd().getOtherIntangibleAssets());
        intangibleAssetsResource.setAmortisation(amortisation);

    }

    @Override
    protected void mapNetBookValueResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {
        intangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
                Optional.of(intangibleAssets)
                        .map(IntangibleAssets::getNetBookValue)
                        .map(IntangibleAssetsNetBookValue::getPreviousPeriod)
                        .map(PreviousPeriod::getOtherIntangibleAssets)
                        .orElse( null));
        intangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                intangibleAssets.getNetBookValue().getCurrentPeriod().getOtherIntangibleAssets());
    }
}
