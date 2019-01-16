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

public class TangibleAssetsMotorVehiclesTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setMotorVehicles(tangibleAssetsResource.getCost().getAtPeriodStart());

            TangibleAssetsColumns additions = createAdditions(tangibleAssetsCost);
            additions.setMotorVehicles(tangibleAssetsResource.getCost().getAdditions());

            TangibleAssetsColumns disposals = createDisposals(tangibleAssetsCost);
            disposals.setMotorVehicles(tangibleAssetsResource.getCost().getDisposals());

            TangibleAssetsColumns revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setMotorVehicles(tangibleAssetsResource.getCost().getRevaluations());

            TangibleAssetsColumns transfers = createTransfers(tangibleAssetsCost);
            transfers.setMotorVehicles(tangibleAssetsResource.getCost().getTransfers());

            TangibleAssetsColumns atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setMotorVehicles(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setMotorVehicles(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            TangibleAssetsColumns chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setMotorVehicles(tangibleAssetsResource.getDepreciation().getChargeForYear());

            TangibleAssetsColumns onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setMotorVehicles(tangibleAssetsResource.getDepreciation().getOnDisposals());

            TangibleAssetsColumns otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setMotorVehicles(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            TangibleAssetsColumns atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setMotorVehicles(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        TangibleAssetsColumns currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setMotorVehicles(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        TangibleAssetsColumns previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
        previousPeriod.setMotorVehicles(tangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
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

        TangibleAssetsResource motorVehicles = new TangibleAssetsResource();

        if (hasCostResources(tangibleAssets)) {
            mapCostResources(tangibleAssets, motorVehicles);
        }

        if (hasDepreciationResources(tangibleAssets)) {
            mapDepreciationResources(tangibleAssets, motorVehicles);
        }

        if (hasNetBookValueResources(tangibleAssets)) {
            mapNetBookValueResources(tangibleAssets, motorVehicles);
        }

        tangibleApi.setMotorVehicles(motorVehicles);
    }

    @Override
    protected boolean hasCostResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getCost().getAtPeriodStart().getMotorVehicles(),
                tangibleAssets.getCost().getAdditions().getMotorVehicles(),
                tangibleAssets.getCost().getDisposals().getMotorVehicles(),
                tangibleAssets.getCost().getRevaluations().getMotorVehicles(),
                tangibleAssets.getCost().getTransfers().getMotorVehicles(),
                tangibleAssets.getCost().getAtPeriodEnd().getMotorVehicles())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getDepreciation().getAtPeriodStart().getMotorVehicles(),
                tangibleAssets.getDepreciation().getChargeForYear().getMotorVehicles(),
                tangibleAssets.getDepreciation().getOnDisposals().getMotorVehicles(),
                tangibleAssets.getDepreciation().getOtherAdjustments().getMotorVehicles(),
                tangibleAssets.getDepreciation().getAtPeriodEnd().getMotorVehicles())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getNetBookValue().getCurrentPeriod().getMotorVehicles(),
                tangibleAssets.getNetBookValue().getPreviousPeriod().getMotorVehicles())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(tangibleAssets.getCost().getAtPeriodStart().getMotorVehicles());
        cost.setAdditions(tangibleAssets.getCost().getAdditions().getMotorVehicles());
        cost.setDisposals(tangibleAssets.getCost().getDisposals().getMotorVehicles());
        cost.setRevaluations(tangibleAssets.getCost().getRevaluations().getMotorVehicles());
        cost.setTransfers(tangibleAssets.getCost().getTransfers().getMotorVehicles());
        cost.setAtPeriodEnd(tangibleAssets.getCost().getAtPeriodEnd().getMotorVehicles());
        tangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapDepreciationResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart(tangibleAssets.getDepreciation().getAtPeriodStart().getMotorVehicles());
        depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getMotorVehicles());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getMotorVehicles());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getMotorVehicles());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getMotorVehicles());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getMotorVehicles());
        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
                tangibleAssets.getNetBookValue().getPreviousPeriod().getMotorVehicles());
    }
}
