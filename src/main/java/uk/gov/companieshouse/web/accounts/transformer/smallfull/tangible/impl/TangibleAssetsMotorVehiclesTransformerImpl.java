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

public class TangibleAssetsMotorVehiclesTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setMotorVehicles(tangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(tangibleAssetsCost);
            additions.setMotorVehicles(tangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(tangibleAssetsCost);
            disposals.setMotorVehicles(tangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setMotorVehicles(tangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(tangibleAssetsCost);
            transfers.setMotorVehicles(tangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setMotorVehicles(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            DepreciationAtPeriodStart atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setMotorVehicles(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            ChargeForYear chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setMotorVehicles(tangibleAssetsResource.getDepreciation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setMotorVehicles(tangibleAssetsResource.getDepreciation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setMotorVehicles(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            DepreciationAtPeriodEnd atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setMotorVehicles(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setMotorVehicles(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
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

        TangibleAssetsCost cost = tangibleAssets.getCost();

        return Stream
            .of(Optional.of(cost)
                    .map(TangibleAssetsCost::getAtPeriodStart)
                    .map(CostAtPeriodStart::getMotorVehicles)
                    .orElse(null),
                cost.getAdditions().getMotorVehicles(),
                cost.getDisposals().getMotorVehicles(),
                cost.getRevaluations().getMotorVehicles(),
                cost.getTransfers().getMotorVehicles(),
                cost.getAtPeriodEnd().getMotorVehicles())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        TangibleAssetsDepreciation depreciation = tangibleAssets.getDepreciation();

        return Stream.of(Optional.of(depreciation)
                .map(TangibleAssetsDepreciation::getAtPeriodStart)
                .map(DepreciationAtPeriodStart::getMotorVehicles)
                .orElse(null),
            depreciation.getChargeForYear().getMotorVehicles(),
            depreciation.getOnDisposals().getMotorVehicles(),
            depreciation.getOtherAdjustments().getMotorVehicles(),
            depreciation.getAtPeriodEnd().getMotorVehicles())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        TangibleAssetsNetBookValue netBookValue = tangibleAssets.getNetBookValue();

        return Stream.of(Optional.of(netBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getMotorVehicles)
                .orElse(null),
            netBookValue.getCurrentPeriod().getMotorVehicles())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(tangibleAssets)
            .map(TangibleAssets::getCost)
            .map(TangibleAssetsCost::getAtPeriodStart)
            .map(CostAtPeriodStart::getMotorVehicles)
            .orElse(null));
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
        depreciation.setAtPeriodStart( Optional.of(tangibleAssets)
            .map(TangibleAssets::getDepreciation)
            .map(TangibleAssetsDepreciation::getAtPeriodStart)
            .map(DepreciationAtPeriodStart::getMotorVehicles)
            .orElse(null));
        depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getMotorVehicles());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getMotorVehicles());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getMotorVehicles());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getMotorVehicles());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
            Optional.of(tangibleAssets)
                .map(TangibleAssets::getNetBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getMotorVehicles)
                .orElse(null));
        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getMotorVehicles());
    }
}
