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

public class TangibleAssetsLandAndBuildingsTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setLandAndBuildings(tangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(tangibleAssetsCost);
            additions.setLandAndBuildings(tangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(tangibleAssetsCost);
            disposals.setLandAndBuildings(tangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setLandAndBuildings(tangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(tangibleAssetsCost);
            transfers.setLandAndBuildings(tangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setLandAndBuildings(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            DepreciationAtPeriodStart atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            ChargeForYear chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            DepreciationAtPeriodEnd atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setLandAndBuildings(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
        previousPeriod.setLandAndBuildings(tangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
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

        TangibleAssetsResource landAndBuildings = new TangibleAssetsResource();

        if (hasCostResources(tangibleAssets)) {
            mapCostResources(tangibleAssets, landAndBuildings);
        }

        if (hasDepreciationResources(tangibleAssets)) {
            mapDepreciationResources(tangibleAssets, landAndBuildings);
        }

        if (hasNetBookValueResources(tangibleAssets)) {
            mapNetBookValueResources(tangibleAssets, landAndBuildings);
        }

        tangibleApi.setLandAndBuildings(landAndBuildings);
    }

    @Override
    protected boolean hasCostResources(TangibleAssets tangibleAssets) {

        TangibleAssetsCost cost = tangibleAssets.getCost();

        return Stream
            .of(Optional.of(cost)
                    .map(TangibleAssetsCost::getAtPeriodStart)
                    .map(CostAtPeriodStart::getLandAndBuildings)
                    .orElse(null),
                cost.getAdditions().getLandAndBuildings(),
                cost.getDisposals().getLandAndBuildings(),
                cost.getRevaluations().getLandAndBuildings(),
                cost.getTransfers().getLandAndBuildings(),
                cost.getAtPeriodEnd().getLandAndBuildings())
            .anyMatch(Objects::nonNull);

    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        TangibleAssetsDepreciation depreciation = tangibleAssets.getDepreciation();

        return Stream.of(Optional.of(depreciation)
                .map(TangibleAssetsDepreciation::getAtPeriodStart)
                .map(DepreciationAtPeriodStart::getLandAndBuildings)
                .orElse(null),
            depreciation.getChargeForYear().getLandAndBuildings(),
            depreciation.getOnDisposals().getLandAndBuildings(),
            depreciation.getOtherAdjustments().getLandAndBuildings(),
            depreciation.getAtPeriodEnd().getLandAndBuildings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        TangibleAssetsNetBookValue netBookValue = tangibleAssets.getNetBookValue();

        return Stream.of(Optional.of(netBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getLandAndBuildings)
                .orElse(null),
            netBookValue.getCurrentPeriod().getLandAndBuildings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(tangibleAssets)
            .map(TangibleAssets::getCost)
            .map(TangibleAssetsCost::getAtPeriodStart)
            .map(CostAtPeriodStart::getLandAndBuildings)
            .orElse(0L));
        cost.setAdditions(tangibleAssets.getCost().getAdditions().getLandAndBuildings());
        cost.setDisposals(tangibleAssets.getCost().getDisposals().getLandAndBuildings());
        cost.setRevaluations(tangibleAssets.getCost().getRevaluations().getLandAndBuildings());
        cost.setTransfers(tangibleAssets.getCost().getTransfers().getLandAndBuildings());
        cost.setAtPeriodEnd(tangibleAssets.getCost().getAtPeriodEnd().getLandAndBuildings());
        tangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapDepreciationResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart( Optional.of(tangibleAssets)
            .map(TangibleAssets::getDepreciation)
            .map(TangibleAssetsDepreciation::getAtPeriodStart)
            .map(DepreciationAtPeriodStart::getLandAndBuildings)
            .orElse(0L));depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getLandAndBuildings());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getLandAndBuildings());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getLandAndBuildings());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getLandAndBuildings());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
            Optional.of(tangibleAssets)
                .map(TangibleAssets::getNetBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getLandAndBuildings)
                .orElse(0L));
        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getLandAndBuildings());
    }
}
