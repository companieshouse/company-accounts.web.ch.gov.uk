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

public class TangibleAssetsOfficeEquipmentTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setOfficeEquipment(tangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(tangibleAssetsCost);
            additions.setOfficeEquipment(tangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(tangibleAssetsCost);
            disposals.setOfficeEquipment(tangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setOfficeEquipment(tangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(tangibleAssetsCost);
            transfers.setOfficeEquipment(tangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setOfficeEquipment(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            DepreciationAtPeriodStart atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            ChargeForYear chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            DepreciationAtPeriodEnd atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setOfficeEquipment(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setOfficeEquipment(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
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

        TangibleAssetsCost cost = tangibleAssets.getCost();

        return Stream
            .of(Optional.of(cost)
                    .map(TangibleAssetsCost::getAtPeriodStart)
                    .map(CostAtPeriodStart::getOfficeEquipment)
                    .orElse(null),
                cost.getAdditions().getOfficeEquipment(),
                cost.getDisposals().getOfficeEquipment(),
                cost.getRevaluations().getOfficeEquipment(),
                cost.getTransfers().getOfficeEquipment(),
                cost.getAtPeriodEnd().getOfficeEquipment())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        TangibleAssetsDepreciation depreciation = tangibleAssets.getDepreciation();

        return Stream.of(Optional.of(depreciation)
                .map(TangibleAssetsDepreciation::getAtPeriodStart)
                .map(DepreciationAtPeriodStart::getOfficeEquipment)
                .orElse(null),
            depreciation.getChargeForYear().getOfficeEquipment(),
            depreciation.getOnDisposals().getOfficeEquipment(),
            depreciation.getOtherAdjustments().getOfficeEquipment(),
            depreciation.getAtPeriodEnd().getOfficeEquipment())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        TangibleAssetsNetBookValue netBookValue = tangibleAssets.getNetBookValue();

        return Stream.of(Optional.of(netBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getOfficeEquipment)
                .orElse(null),
            netBookValue.getCurrentPeriod().getOfficeEquipment())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(tangibleAssets)
            .map(TangibleAssets::getCost)
            .map(TangibleAssetsCost::getAtPeriodStart)
            .map(CostAtPeriodStart::getOfficeEquipment)
            .orElse(0L));
        cost.setAdditions(tangibleAssets.getCost().getAdditions().getOfficeEquipment());
        cost.setDisposals(tangibleAssets.getCost().getDisposals().getOfficeEquipment());
        cost.setRevaluations(tangibleAssets.getCost().getRevaluations().getOfficeEquipment());
        cost.setTransfers(tangibleAssets.getCost().getTransfers().getOfficeEquipment());
        cost.setAtPeriodEnd(tangibleAssets.getCost().getAtPeriodEnd().getOfficeEquipment());
        tangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapDepreciationResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart( Optional.of(tangibleAssets)
            .map(TangibleAssets::getDepreciation)
            .map(TangibleAssetsDepreciation::getAtPeriodStart)
            .map(DepreciationAtPeriodStart::getOfficeEquipment)
            .orElse(0L));
        depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getOfficeEquipment());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getOfficeEquipment());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getOfficeEquipment());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getOfficeEquipment());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
            Optional.of(tangibleAssets)
                .map(TangibleAssets::getNetBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getOfficeEquipment)
                .orElse(0L));
        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getOfficeEquipment());
    }
}
