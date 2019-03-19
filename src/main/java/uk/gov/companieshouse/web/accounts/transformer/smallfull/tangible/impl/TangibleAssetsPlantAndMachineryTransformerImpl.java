package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
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
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsResourceTransformer;

public class TangibleAssetsPlantAndMachineryTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setPlantAndMachinery(tangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(tangibleAssetsCost);
            additions.setPlantAndMachinery(tangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(tangibleAssetsCost);
            disposals.setPlantAndMachinery(tangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setPlantAndMachinery(tangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(tangibleAssetsCost);
            transfers.setPlantAndMachinery(tangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setPlantAndMachinery(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            DepreciationAtPeriodStart atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setPlantAndMachinery(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            ChargeForYear chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setPlantAndMachinery(tangibleAssetsResource.getDepreciation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setPlantAndMachinery(tangibleAssetsResource.getDepreciation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setPlantAndMachinery(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            DepreciationAtPeriodEnd atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setPlantAndMachinery(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setPlantAndMachinery(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
        previousPeriod.setPlantAndMachinery(tangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
    }

    @Override
    public boolean hasTangibleAssetsToMapToApiResource(TangibleAssets tangibleAssets) {
        return hasCostResources(tangibleAssets) ||
                hasDepreciationResources(tangibleAssets) ||
                hasNetBookValueResources(tangibleAssets);
    }

    @Override
    public void mapTangibleAssetsToApiResource(TangibleAssets tangibleAssets,
            TangibleApi tangibleApi) {

        TangibleAssetsResource plantAndMachinery = new TangibleAssetsResource();

        if (hasCostResources(tangibleAssets)) {
            mapCostResources(tangibleAssets, plantAndMachinery);
        }

        if (hasDepreciationResources(tangibleAssets)) {
            mapDepreciationResources(tangibleAssets, plantAndMachinery);
        }

        if (hasNetBookValueResources(tangibleAssets)) {
            mapNetBookValueResources(tangibleAssets, plantAndMachinery);
        }

        tangibleApi.setPlantAndMachinery(plantAndMachinery);
    }

    @Override
    protected boolean hasCostResources(TangibleAssets tangibleAssets) {

        TangibleAssetsCost cost = tangibleAssets.getCost();

        return Stream
            .of(Optional.of(cost)
                    .map(TangibleAssetsCost::getAtPeriodStart)
                    .map(CostAtPeriodStart::getPlantAndMachinery)
                    .orElse(null),
                cost.getAdditions().getPlantAndMachinery(),
                cost.getDisposals().getPlantAndMachinery(),
                cost.getRevaluations().getPlantAndMachinery(),
                cost.getTransfers().getPlantAndMachinery(),
                cost.getAtPeriodEnd().getPlantAndMachinery())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        TangibleAssetsDepreciation depreciation = tangibleAssets.getDepreciation();

        return Stream.of(Optional.of(depreciation)
                .map(TangibleAssetsDepreciation::getAtPeriodStart)
                .map(DepreciationAtPeriodStart::getPlantAndMachinery)
                .orElse(null),
                depreciation.getChargeForYear().getPlantAndMachinery(),
            depreciation.getOnDisposals().getPlantAndMachinery(),
            depreciation.getOtherAdjustments().getPlantAndMachinery(),
            depreciation.getAtPeriodEnd().getPlantAndMachinery())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        TangibleAssetsNetBookValue netBookValue = tangibleAssets.getNetBookValue();

        return Stream.of(Optional.of(netBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getPlantAndMachinery)
                .orElse(null),
            netBookValue.getCurrentPeriod().getPlantAndMachinery())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(tangibleAssets)
            .map(TangibleAssets::getCost)
            .map(TangibleAssetsCost::getAtPeriodStart)
            .map(CostAtPeriodStart::getPlantAndMachinery)
            .orElse(null));
        cost.setAdditions(tangibleAssets.getCost().getAdditions().getPlantAndMachinery());
        cost.setDisposals(tangibleAssets.getCost().getDisposals().getPlantAndMachinery());
        cost.setRevaluations(tangibleAssets.getCost().getRevaluations().getPlantAndMachinery());
        cost.setTransfers(tangibleAssets.getCost().getTransfers().getPlantAndMachinery());
        cost.setAtPeriodEnd(tangibleAssets.getCost().getAtPeriodEnd().getPlantAndMachinery());
        tangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapDepreciationResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart( Optional.of(tangibleAssets)
            .map(TangibleAssets::getDepreciation)
            .map(TangibleAssetsDepreciation::getAtPeriodStart)
            .map(DepreciationAtPeriodStart::getPlantAndMachinery)
            .orElse(null));
        depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getPlantAndMachinery());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getPlantAndMachinery());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getPlantAndMachinery());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getPlantAndMachinery());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
            Optional.of(tangibleAssets)
                .map(TangibleAssets::getNetBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getPlantAndMachinery)
                .orElse(null));
        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getPlantAndMachinery());
    }
}
