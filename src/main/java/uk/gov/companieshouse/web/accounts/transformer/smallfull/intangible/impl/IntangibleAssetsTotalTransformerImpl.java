package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
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

public class IntangibleAssetsTotalTransformerImpl extends
        IntangibleAssetsResourceTransformerImpl implements IntangibleAssetsResourceTransformer {

    @Override
    public void mapIntangibleAssetsResourceToWebModel(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        if (intangibleAssetsResource.getCost() != null) {

            IntangibleAssetsCost intangibleAssetsCost = createCost(intangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(intangibleAssetsCost);
            atPeriodStart
                    .setTotal(intangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(intangibleAssetsCost);
            additions.setTotal(intangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(intangibleAssetsCost);
            disposals.setTotal(intangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(intangibleAssetsCost);
            revaluations.setTotal(intangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(intangibleAssetsCost);
            transfers.setTotal(intangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(intangibleAssetsCost);
            atPeriodEnd.setTotal(intangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (intangibleAssetsResource.getAmortisation() != null) {

            IntangibleAssetsAmortisation intangibleAssetsAmortisation = createAmortisation(intangibleAssets);

            AmortisationAtPeriodStart atPeriodStart = createAmortisationAtPeriodStart(intangibleAssetsAmortisation);
            atPeriodStart.setTotal(intangibleAssetsResource.getAmortisation().getAtPeriodStart());

            ChargeForYear chargeForYear = createAmortisationChargeForYear(intangibleAssetsAmortisation);
            chargeForYear.setTotal(intangibleAssetsResource.getAmortisation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(intangibleAssetsAmortisation);
            onDisposals.setTotal(intangibleAssetsResource.getAmortisation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(intangibleAssetsAmortisation);
            otherAdjustments.setTotal(intangibleAssetsResource.getAmortisation().getOtherAdjustments());

            AmortisationAtPeriodEnd atPeriodEnd = createAmortisationAtPeriodEnd(intangibleAssetsAmortisation);
            atPeriodEnd.setTotal(intangibleAssetsResource.getAmortisation().getAtPeriodEnd());
        }

        IntangibleAssetsNetBookValue intangibleAssetsNetBookValue = createNetBookValue(intangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(intangibleAssetsNetBookValue);
        currentPeriod.setTotal(intangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(intangibleAssetsNetBookValue);
        previousPeriod.setTotal(intangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
    }

    @Override
    public boolean hasIntangibleAssetsToMapToApiResource(IntangibleAssets intangibleAssets) {
        return hasCostResources(intangibleAssets) ||
                hasAmortisationResources(intangibleAssets) ||
                hasNetBookValueResources(intangibleAssets);
    }

    @Override
    public void mapIntangibleAssetsToApiResource(IntangibleAssets intangibleAssets, IntangibleApi intangibleApi) {

        IntangibleAssetsResource total = new IntangibleAssetsResource();

        if (hasCostResources(intangibleAssets)) {
            mapCostResources(intangibleAssets, total);
        }

        if (hasAmortisationResources(intangibleAssets)) {
            mapAmortisationResources(intangibleAssets, total);
        }

        if (hasNetBookValueResources(intangibleAssets)) {
            mapNetBookValueResources(intangibleAssets, total);
        }

        intangibleApi.setTotal(total);
    }

    @Override
    protected boolean hasCostResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsCost cost = intangibleAssets.getCost();

        return Stream
                .of(Optional.of(cost)
                                .map(IntangibleAssetsCost::getAtPeriodStart)
                                .map(CostAtPeriodStart::getTotal)
                                .orElse(null),
                        cost.getAdditions().getTotal(),
                        cost.getDisposals().getTotal(),
                        cost.getRevaluations().getTotal(),
                        cost.getTransfers().getTotal(),
                        cost.getAtPeriodEnd().getTotal())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasAmortisationResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsAmortisation amortisation = intangibleAssets.getAmortisation();

        return Stream
                .of(Optional.of(amortisation)
                                .map(IntangibleAssetsAmortisation::getAtPeriodStart)
                                .map(AmortisationAtPeriodStart::getTotal)
                                .orElse(null),
                        amortisation.getChargeForYear().getTotal(),
                        amortisation.getOnDisposals().getTotal(),
                        amortisation.getOtherAdjustments().getTotal(),
                        amortisation.getAtPeriodEnd().getTotal())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(IntangibleAssets intangibleAssets) {

        IntangibleAssetsNetBookValue netBookValue = intangibleAssets.getNetBookValue();

        return Stream.of(Optional.of(netBookValue)
                        .map(IntangibleAssetsNetBookValue::getPreviousPeriod)
                        .map(PreviousPeriod::getTotal)
                        .orElse(null),
                netBookValue.getCurrentPeriod().getTotal())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getCost)
                .map(IntangibleAssetsCost::getAtPeriodStart)
                .map(CostAtPeriodStart::getTotal)
                .orElse(null));
        cost.setAdditions(intangibleAssets.getCost().getAdditions().getTotal());
        cost.setDisposals(intangibleAssets.getCost().getDisposals().getTotal());
        cost.setRevaluations(intangibleAssets.getCost().getRevaluations().getTotal());
        cost.setTransfers(intangibleAssets.getCost().getTransfers().getTotal());
        cost.setAtPeriodEnd(intangibleAssets.getCost().getAtPeriodEnd().getTotal());
        intangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapAmortisationResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        Amortisation amortisation = new Amortisation();
        amortisation.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getAmortisation)
                .map(IntangibleAssetsAmortisation::getAtPeriodStart)
                .map(AmortisationAtPeriodStart::getTotal)
                .orElse(null));
        amortisation.setChargeForYear(intangibleAssets.getAmortisation().getChargeForYear().getTotal());
        amortisation.setOnDisposals(intangibleAssets.getAmortisation().getOnDisposals().getTotal());
        amortisation.setOtherAdjustments(intangibleAssets.getAmortisation().getOtherAdjustments().getTotal());
        amortisation.setAtPeriodEnd(intangibleAssets.getAmortisation().getAtPeriodEnd().getTotal());
        intangibleAssetsResource.setAmortisation(amortisation);

    }

    @Override
    protected void mapNetBookValueResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        intangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
                Optional.of(intangibleAssets)
                        .map(IntangibleAssets::getNetBookValue)
                        .map(IntangibleAssetsNetBookValue::getPreviousPeriod)
                        .map(PreviousPeriod::getTotal)
                        .orElse(null));
        intangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                intangibleAssets.getNetBookValue().getCurrentPeriod().getTotal());
    }
}
