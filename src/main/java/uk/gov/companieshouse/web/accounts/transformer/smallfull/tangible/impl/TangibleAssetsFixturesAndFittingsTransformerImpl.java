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

public class TangibleAssetsFixturesAndFittingsTransformerImpl extends
    TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
        TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart
                .setFixturesAndFittings(tangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(tangibleAssetsCost);
            additions.setFixturesAndFittings(tangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(tangibleAssetsCost);
            disposals.setFixturesAndFittings(tangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setFixturesAndFittings(tangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(tangibleAssetsCost);
            transfers.setFixturesAndFittings(tangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setFixturesAndFittings(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(
                tangibleAssets);

            DepreciationAtPeriodStart atPeriodStart = createDepreciationAtPeriodStart(
                tangibleAssetsDepreciation);
            atPeriodStart.setFixturesAndFittings(
                tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            ChargeForYear chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setFixturesAndFittings(
                tangibleAssetsResource.getDepreciation().getChargeForYear());

            OnDisposals onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals
                .setFixturesAndFittings(tangibleAssetsResource.getDepreciation().getOnDisposals());

            OtherAdjustments otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setFixturesAndFittings(
                tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            DepreciationAtPeriodEnd atPeriodEnd = createDepreciationAtPeriodEnd(
                tangibleAssetsDepreciation);
            atPeriodEnd
                .setFixturesAndFittings(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        CurrentPeriod currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod
            .setFixturesAndFittings(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        PreviousPeriod previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
        previousPeriod
            .setFixturesAndFittings(tangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
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

        TangibleAssetsResource fixturesAndFittings = new TangibleAssetsResource();

        if (hasCostResources(tangibleAssets)) {
            mapCostResources(tangibleAssets, fixturesAndFittings);
        }

        if (hasDepreciationResources(tangibleAssets)) {
            mapDepreciationResources(tangibleAssets, fixturesAndFittings);
        }

        if (hasNetBookValueResources(tangibleAssets)) {
            mapNetBookValueResources(tangibleAssets, fixturesAndFittings);
        }

        tangibleApi.setFixturesAndFittings(fixturesAndFittings);
    }

    @Override
    protected boolean hasCostResources(TangibleAssets tangibleAssets) {

        TangibleAssetsCost cost = tangibleAssets.getCost();

        return Stream
            .of(Optional.of(cost)
                    .map(TangibleAssetsCost::getAtPeriodStart)
                    .map(CostAtPeriodStart::getFixturesAndFittings)
                    .orElse(null),
                cost.getAdditions().getFixturesAndFittings(),
                cost.getDisposals().getFixturesAndFittings(),
                cost.getRevaluations().getFixturesAndFittings(),
                cost.getTransfers().getFixturesAndFittings(),
                cost.getAtPeriodEnd().getFixturesAndFittings())
            .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        TangibleAssetsDepreciation depreciation = tangibleAssets.getDepreciation();

        return Stream
            .of(Optional.of(depreciation)
                    .map(TangibleAssetsDepreciation::getAtPeriodStart)
                    .map(DepreciationAtPeriodStart::getFixturesAndFittings)
                    .orElse(null),
                depreciation.getChargeForYear().getFixturesAndFittings(),
                depreciation.getOnDisposals().getFixturesAndFittings(),
                depreciation.getOtherAdjustments().getFixturesAndFittings(),
                depreciation.getAtPeriodEnd().getFixturesAndFittings())
            .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        TangibleAssetsNetBookValue netBookValue = tangibleAssets.getNetBookValue();

        return Stream
            .of(Optional.of(netBookValue)
                    .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                    .map(PreviousPeriod::getFixturesAndFittings)
                    .orElse(null),
                netBookValue.getCurrentPeriod().getFixturesAndFittings())
            .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets,
        TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(tangibleAssets)
            .map(TangibleAssets::getCost)
            .map(TangibleAssetsCost::getAtPeriodStart)
            .map(CostAtPeriodStart::getFixturesAndFittings)
            .orElse(null));
        cost.setAdditions(tangibleAssets.getCost().getAdditions().getFixturesAndFittings());
        cost.setDisposals(tangibleAssets.getCost().getDisposals().getFixturesAndFittings());
        cost.setRevaluations(tangibleAssets.getCost().getRevaluations().getFixturesAndFittings());
        cost.setTransfers(tangibleAssets.getCost().getTransfers().getFixturesAndFittings());
        cost.setAtPeriodEnd(tangibleAssets.getCost().getAtPeriodEnd().getFixturesAndFittings());
        tangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapDepreciationResources(TangibleAssets tangibleAssets,
        TangibleAssetsResource tangibleAssetsResource) {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart(Optional.of(tangibleAssets)
            .map(TangibleAssets::getDepreciation)
            .map(TangibleAssetsDepreciation::getAtPeriodStart)
            .map(DepreciationAtPeriodStart::getFixturesAndFittings)
            .orElse(null));
        depreciation.setChargeForYear(
            tangibleAssets.getDepreciation().getChargeForYear().getFixturesAndFittings());
        depreciation.setOnDisposals(
            tangibleAssets.getDepreciation().getOnDisposals().getFixturesAndFittings());
        depreciation.setOtherAdjustments(
            tangibleAssets.getDepreciation().getOtherAdjustments().getFixturesAndFittings());
        depreciation.setAtPeriodEnd(
            tangibleAssets.getDepreciation().getAtPeriodEnd().getFixturesAndFittings());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets,
        TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
            Optional.of(tangibleAssets)
                .map(TangibleAssets::getNetBookValue)
                .map(TangibleAssetsNetBookValue::getPreviousPeriod)
                .map(PreviousPeriod::getFixturesAndFittings)
                .orElse(null));
        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
            tangibleAssets.getNetBookValue().getCurrentPeriod().getFixturesAndFittings());
    }
}
