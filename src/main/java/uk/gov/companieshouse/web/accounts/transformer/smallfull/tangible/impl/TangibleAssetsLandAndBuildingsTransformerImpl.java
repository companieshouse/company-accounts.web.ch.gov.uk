package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl;

import java.util.Objects;
import java.util.stream.Stream;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.Cost;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.Depreciation;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsColumns;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsResourceTransformer;

public class TangibleAssetsLandAndBuildingsTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setLandAndBuildings(tangibleAssetsResource.getCost().getAtPeriodStart());

            TangibleAssetsColumns additions = createAdditions(tangibleAssetsCost);
            additions.setLandAndBuildings(tangibleAssetsResource.getCost().getAdditions());

            TangibleAssetsColumns disposals = createDisposals(tangibleAssetsCost);
            disposals.setLandAndBuildings(tangibleAssetsResource.getCost().getDisposals());

            TangibleAssetsColumns revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setLandAndBuildings(tangibleAssetsResource.getCost().getRevaluations());

            TangibleAssetsColumns transfers = createTransfers(tangibleAssetsCost);
            transfers.setLandAndBuildings(tangibleAssetsResource.getCost().getTransfers());

            TangibleAssetsColumns atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setLandAndBuildings(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            TangibleAssetsColumns chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getChargeForYear());

            TangibleAssetsColumns onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getOnDisposals());

            TangibleAssetsColumns otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            TangibleAssetsColumns atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setLandAndBuildings(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        TangibleAssetsColumns currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setLandAndBuildings(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        TangibleAssetsColumns previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
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

        return Stream.of(tangibleAssets.getCost().getAtPeriodStart().getLandAndBuildings(),
                tangibleAssets.getCost().getAdditions().getLandAndBuildings(),
                tangibleAssets.getCost().getDisposals().getLandAndBuildings(),
                tangibleAssets.getCost().getRevaluations().getLandAndBuildings(),
                tangibleAssets.getCost().getTransfers().getLandAndBuildings(),
                tangibleAssets.getCost().getAtPeriodEnd().getLandAndBuildings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getDepreciation().getAtPeriodStart().getLandAndBuildings(),
                tangibleAssets.getDepreciation().getChargeForYear().getLandAndBuildings(),
                tangibleAssets.getDepreciation().getOnDisposals().getLandAndBuildings(),
                tangibleAssets.getDepreciation().getOtherAdjustments().getLandAndBuildings(),
                tangibleAssets.getDepreciation().getAtPeriodEnd().getLandAndBuildings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getNetBookValue().getCurrentPeriod().getLandAndBuildings(),
                tangibleAssets.getNetBookValue().getPreviousPeriod().getLandAndBuildings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(tangibleAssets.getCost().getAtPeriodStart().getLandAndBuildings());
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
        depreciation.setAtPeriodStart(tangibleAssets.getDepreciation().getAtPeriodStart().getLandAndBuildings());
        depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getLandAndBuildings());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getLandAndBuildings());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getLandAndBuildings());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getLandAndBuildings());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getLandAndBuildings());
        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
                tangibleAssets.getNetBookValue().getPreviousPeriod().getLandAndBuildings());
    }
}
