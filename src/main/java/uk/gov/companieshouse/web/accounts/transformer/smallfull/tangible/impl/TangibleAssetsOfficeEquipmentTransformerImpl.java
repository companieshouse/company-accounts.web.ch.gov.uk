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

public class TangibleAssetsOfficeEquipmentTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setOfficeEquipment(tangibleAssetsResource.getCost().getAtPeriodStart());

            TangibleAssetsColumns additions = createAdditions(tangibleAssetsCost);
            additions.setOfficeEquipment(tangibleAssetsResource.getCost().getAdditions());

            TangibleAssetsColumns disposals = createDisposals(tangibleAssetsCost);
            disposals.setOfficeEquipment(tangibleAssetsResource.getCost().getDisposals());

            TangibleAssetsColumns revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setOfficeEquipment(tangibleAssetsResource.getCost().getRevaluations());

            TangibleAssetsColumns transfers = createTransfers(tangibleAssetsCost);
            transfers.setOfficeEquipment(tangibleAssetsResource.getCost().getTransfers());

            TangibleAssetsColumns atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setOfficeEquipment(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            TangibleAssetsColumns chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getChargeForYear());

            TangibleAssetsColumns onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getOnDisposals());

            TangibleAssetsColumns otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            TangibleAssetsColumns atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        TangibleAssetsColumns currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setOfficeEquipment(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        TangibleAssetsColumns previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
        previousPeriod.setOfficeEquipment(tangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
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

        TangibleAssetsResource officeEquipment = new TangibleAssetsResource();

        if (hasCostResources(tangibleAssets)) {
            mapCostResources(tangibleAssets, officeEquipment);
        }

        if (hasDepreciationResources(tangibleAssets)) {
            mapDepreciationResources(tangibleAssets, officeEquipment);
        }

        if (hasNetBookValueResources(tangibleAssets)) {
            mapNetBookValueResources(tangibleAssets, officeEquipment);
        }

        tangibleApi.setOfficeEquipment(officeEquipment);
    }

    @Override
    protected boolean hasCostResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getCost().getAtPeriodStart().getOfficeEquipment(),
                tangibleAssets.getCost().getAdditions().getOfficeEquipment(),
                tangibleAssets.getCost().getDisposals().getOfficeEquipment(),
                tangibleAssets.getCost().getRevaluations().getOfficeEquipment(),
                tangibleAssets.getCost().getTransfers().getOfficeEquipment(),
                tangibleAssets.getCost().getAtPeriodEnd().getOfficeEquipment())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getDepreciation().getAtPeriodStart().getOfficeEquipment(),
                tangibleAssets.getDepreciation().getChargeForYear().getOfficeEquipment(),
                tangibleAssets.getDepreciation().getOnDisposals().getOfficeEquipment(),
                tangibleAssets.getDepreciation().getOtherAdjustments().getOfficeEquipment(),
                tangibleAssets.getDepreciation().getAtPeriodEnd().getOfficeEquipment())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getNetBookValue().getCurrentPeriod().getOfficeEquipment(),
                tangibleAssets.getNetBookValue().getPreviousPeriod().getOfficeEquipment())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(tangibleAssets.getCost().getAtPeriodStart().getOfficeEquipment());
        cost.setAdditions(tangibleAssets.getCost().getAtPeriodStart().getOfficeEquipment());
        cost.setDisposals(tangibleAssets.getCost().getDisposals().getOfficeEquipment());
        cost.setRevaluations(tangibleAssets.getCost().getRevaluations().getOfficeEquipment());
        cost.setTransfers(tangibleAssets.getCost().getTransfers().getOfficeEquipment());
        cost.setAtPeriodEnd(tangibleAssets.getCost().getAtPeriodEnd().getOfficeEquipment());
        tangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapDepreciationResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart(tangibleAssets.getDepreciation().getAtPeriodStart().getOfficeEquipment());
        depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getOfficeEquipment());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getOfficeEquipment());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getOfficeEquipment());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getOfficeEquipment());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getOfficeEquipment());
        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
                tangibleAssets.getNetBookValue().getPreviousPeriod().getOfficeEquipment());
    }
}
