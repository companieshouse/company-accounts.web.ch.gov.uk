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

public class IntangibleAssetsGoodwillTransformerImpl extends
        IntangibleAssetsResourceTransformerImpl implements IntangibleAssetsResourceTransformer {
    @Override
    public void mapIntangibleAssetsResourceToWebModel(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {
        if (intangibleAssetsResource.getCost() != null) {
            IntangibleAssetsCost intangibleAssetsCost = createCost(intangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(intangibleAssetsCost);
            atPeriodStart
                    .setGoodwill(intangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(intangibleAssetsCost);
            additions.setGoodwill(intangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(intangibleAssetsCost);
            disposals.setGoodwill(intangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(intangibleAssetsCost);
            revaluations.setGoodwill(intangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(intangibleAssetsCost);
            transfers.setGoodwill(intangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(intangibleAssetsCost);
            atPeriodEnd.setGoodwill(intangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (intangibleAssetsResource.getAmortisation() != null) {
            IntangibleAssetsAmortisation intangibleAssetsAmortisation = createAmortisation(intangibleAssets);

            AmortisationAtPeriodStart atPeriodStart = createAmortisationAtPeriodStart(intangibleAssetsAmortisation);
            atPeriodStart.setGoodwill(intangibleAssetsResource.getAmortisation().getAtPeriodStart());

            ChargeForYear chargeForYear = createAmortisationChargeForYear(intangibleAssetsAmortisation);
            chargeForYear.setGoodwill(intangibleAssetsResource.getAmortisation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(intangibleAssetsAmortisation);
            onDisposals.setGoodwill(intangibleAssetsResource.getAmortisation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(intangibleAssetsAmortisation);
            otherAdjustments.setGoodwill(intangibleAssetsResource.getAmortisation().getOtherAdjustments());

            AmortisationAtPeriodEnd atPeriodEnd = createAmortisationAtPeriodEnd(intangibleAssetsAmortisation);
            atPeriodEnd.setGoodwill(intangibleAssetsResource.getAmortisation().getAtPeriodEnd());
        }

        IntangibleAssetsNetBookValue intangibleAssetsNetBookValue = createNetBookValue(intangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(intangibleAssetsNetBookValue);
        currentPeriod
                .setGoodwill(intangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(intangibleAssetsNetBookValue);
        previousPeriod
                .setGoodwill(intangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
    }

    @Override
    public boolean hasIntangibleAssetsToMapToApiResource(IntangibleAssets intangibleAssets) {
        return hasCostResources(intangibleAssets) ||
                hasAmortisationResources(intangibleAssets) ||
                hasNetBookValueResources(intangibleAssets);
    }

    @Override
    public void mapIntangibleAssetsToApiResource(IntangibleAssets intangibleAssets, IntangibleApi intangibleApi) {
        IntangibleAssetsResource goodwill = new IntangibleAssetsResource();

        if (hasCostResources(intangibleAssets)) {
            mapCostResources(intangibleAssets, goodwill);
        }

        if (hasAmortisationResources(intangibleAssets)) {
            mapAmortisationResources(intangibleAssets, goodwill);
        }

        if (hasNetBookValueResources(intangibleAssets)) {
            mapNetBookValueResources(intangibleAssets, goodwill);
        }

        intangibleApi.setGoodwill(goodwill);

    }

    @Override
    protected boolean hasCostResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsCost cost = intangibleAssets.getCost();

        return Stream
                .of(Optional.of(cost)
                        .map(IntangibleAssetsCost::getAtPeriodStart)
                        .map(CostAtPeriodStart::getGoodwill)
                        .orElse(null),
                cost.getAdditions().getGoodwill(),
                cost.getDisposals().getGoodwill(),
                cost.getRevaluations().getGoodwill(),
                cost.getTransfers().getGoodwill(),
                cost.getAtPeriodEnd().getGoodwill())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasAmortisationResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsAmortisation amortisation = intangibleAssets.getAmortisation();

        return Stream
                .of(Optional.of(amortisation)
                        .map(IntangibleAssetsAmortisation::getAtPeriodStart)
                        .map(AmortisationAtPeriodStart::getGoodwill)
                        .orElse(null),
                amortisation.getChargeForYear().getGoodwill(),
                amortisation.getOnDisposals().getGoodwill(),
                amortisation.getOtherAdjustments().getGoodwill(),
                amortisation.getAtPeriodEnd().getGoodwill())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsNetBookValue netBookValue = intangibleAssets.getNetBookValue();

        return Stream
                .of(Optional.of(netBookValue)
                                .map(IntangibleAssetsNetBookValue::getPreviousPeriod)
                                .map(PreviousPeriod::getGoodwill)
                                .orElse(null),
                        netBookValue.getCurrentPeriod().getGoodwill())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {
        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getCost)
                .map(IntangibleAssetsCost::getAtPeriodStart)
                .map(CostAtPeriodStart::getGoodwill)
                .orElse(null));
        cost.setAdditions(intangibleAssets.getCost().getAdditions().getGoodwill());
        cost.setDisposals(intangibleAssets.getCost().getDisposals().getGoodwill());
        cost.setRevaluations(intangibleAssets.getCost().getRevaluations().getGoodwill());
        cost.setTransfers(intangibleAssets.getCost().getTransfers().getGoodwill());
        cost.setAtPeriodEnd(intangibleAssets.getCost().getAtPeriodEnd().getGoodwill());
        intangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapAmortisationResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {
        Amortisation amortisation = new Amortisation();
        amortisation.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getAmortisation)
                .map(IntangibleAssetsAmortisation::getAtPeriodStart)
                .map(AmortisationAtPeriodStart::getGoodwill)
                .orElse(null));
        amortisation.setChargeForYear(intangibleAssets.getAmortisation().getChargeForYear().getGoodwill());
        amortisation.setOnDisposals(intangibleAssets.getAmortisation().getOnDisposals().getGoodwill());
        amortisation.setOtherAdjustments(intangibleAssets.getAmortisation().getOtherAdjustments().getGoodwill());
        amortisation.setAtPeriodEnd(intangibleAssets.getAmortisation().getAtPeriodEnd().getGoodwill());
        intangibleAssetsResource.setAmortisation(amortisation);

    }

    @Override
    protected void mapNetBookValueResources(IntangibleAssets intangibleAssets,
                                            IntangibleAssetsResource intangibleAssetsResource) {
        intangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
                Optional.of(intangibleAssets)
                        .map(IntangibleAssets::getNetBookValue)
                        .map(IntangibleAssetsNetBookValue::getPreviousPeriod)
                        .map(PreviousPeriod::getGoodwill)
                        .orElse(null));
        intangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                intangibleAssets.getNetBookValue().getCurrentPeriod().getGoodwill());
    }
}
