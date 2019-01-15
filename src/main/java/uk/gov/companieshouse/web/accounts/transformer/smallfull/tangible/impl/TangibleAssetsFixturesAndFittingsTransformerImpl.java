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

public class TangibleAssetsFixturesAndFittingsTransformerImpl extends TangibleAssetsResourceTransformerImpl implements TangibleAssetsResourceTransformer {

    @Override
    public void mapTangibleAssetsResourceToWebModel(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource) {

        if (tangibleAssetsResource.getCost() != null) {

            TangibleAssetsCost tangibleAssetsCost = createCost(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createCostAtPeriodStart(tangibleAssetsCost);
            atPeriodStart.setFixturesAndFittings(tangibleAssetsResource.getCost().getAtPeriodStart());

            TangibleAssetsColumns additions = createAdditions(tangibleAssetsCost);
            additions.setFixturesAndFittings(tangibleAssetsResource.getCost().getAdditions());

            TangibleAssetsColumns disposals = createDisposals(tangibleAssetsCost);
            disposals.setFixturesAndFittings(tangibleAssetsResource.getCost().getDisposals());

            TangibleAssetsColumns revaluations = createRevaluations(tangibleAssetsCost);
            revaluations.setFixturesAndFittings(tangibleAssetsResource.getCost().getRevaluations());

            TangibleAssetsColumns transfers = createTransfers(tangibleAssetsCost);
            transfers.setFixturesAndFittings(tangibleAssetsResource.getCost().getTransfers());

            TangibleAssetsColumns atPeriodEnd = createCostAtPeriodEnd(tangibleAssetsCost);
            atPeriodEnd.setFixturesAndFittings(tangibleAssetsResource.getCost().getAtPeriodEnd());
        }

        if (tangibleAssetsResource.getDepreciation() != null) {

            TangibleAssetsDepreciation tangibleAssetsDepreciation = createDepreciation(tangibleAssets);

            TangibleAssetsColumns atPeriodStart = createDepreciationAtPeriodStart(tangibleAssetsDepreciation);
            atPeriodStart.setFixturesAndFittings(tangibleAssetsResource.getDepreciation().getAtPeriodStart());

            TangibleAssetsColumns chargeForYear = createChargeForYear(tangibleAssetsDepreciation);
            chargeForYear.setFixturesAndFittings(tangibleAssetsResource.getDepreciation().getChargeForYear());

            TangibleAssetsColumns onDisposals = createOnDisposals(tangibleAssetsDepreciation);
            onDisposals.setFixturesAndFittings(tangibleAssetsResource.getDepreciation().getOnDisposals());

            TangibleAssetsColumns otherAdjustments = createOtherAdjustments(tangibleAssetsDepreciation);
            otherAdjustments.setFixturesAndFittings(tangibleAssetsResource.getDepreciation().getOtherAdjustments());

            TangibleAssetsColumns atPeriodEnd = createDepreciationAtPeriodEnd(tangibleAssetsDepreciation);
            atPeriodEnd.setFixturesAndFittings(tangibleAssetsResource.getDepreciation().getAtPeriodEnd());
        }

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue = createNetBookValue(tangibleAssets);

        TangibleAssetsColumns currentPeriod = createCurrentPeriod(tangibleAssetsNetBookValue);
        currentPeriod.setFixturesAndFittings(tangibleAssetsResource.getNetBookValueAtEndOfCurrentPeriod());

        TangibleAssetsColumns previousPeriod = createPreviousPeriod(tangibleAssetsNetBookValue);
        previousPeriod.setFixturesAndFittings(tangibleAssetsResource.getNetBookValueAtEndOfPreviousPeriod());
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

        return Stream.of(tangibleAssets.getCost().getAtPeriodStart().getFixturesAndFittings(),
                            tangibleAssets.getCost().getAdditions().getFixturesAndFittings(),
                            tangibleAssets.getCost().getDisposals().getFixturesAndFittings(),
                            tangibleAssets.getCost().getRevaluations().getFixturesAndFittings(),
                            tangibleAssets.getCost().getTransfers().getFixturesAndFittings(),
                            tangibleAssets.getCost().getAtPeriodEnd().getFixturesAndFittings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasDepreciationResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getDepreciation().getAtPeriodStart().getFixturesAndFittings(),
                            tangibleAssets.getDepreciation().getChargeForYear().getFixturesAndFittings(),
                            tangibleAssets.getDepreciation().getOnDisposals().getFixturesAndFittings(),
                            tangibleAssets.getDepreciation().getOtherAdjustments().getFixturesAndFittings(),
                            tangibleAssets.getDepreciation().getAtPeriodEnd().getFixturesAndFittings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected boolean hasNetBookValueResources(TangibleAssets tangibleAssets) {

        return Stream.of(tangibleAssets.getNetBookValue().getCurrentPeriod().getFixturesAndFittings(),
                            tangibleAssets.getNetBookValue().getPreviousPeriod().getFixturesAndFittings())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(tangibleAssets.getCost().getAtPeriodStart().getFixturesAndFittings());
        cost.setAdditions(tangibleAssets.getCost().getAdditions().getFixturesAndFittings());
        cost.setDisposals(tangibleAssets.getCost().getDisposals().getFixturesAndFittings());
        cost.setRevaluations(tangibleAssets.getCost().getRevaluations().getFixturesAndFittings());
        cost.setTransfers(tangibleAssets.getCost().getTransfers().getFixturesAndFittings());
        cost.setAtPeriodEnd(tangibleAssets.getCost().getAtPeriodEnd().getFixturesAndFittings());
        tangibleAssetsResource.setCost(cost);
    }

    @Override
    protected void mapDepreciationResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        Depreciation depreciation = new Depreciation();
        depreciation.setAtPeriodStart(tangibleAssets.getDepreciation().getAtPeriodStart().getFixturesAndFittings());
        depreciation.setChargeForYear(tangibleAssets.getDepreciation().getChargeForYear().getFixturesAndFittings());
        depreciation.setOnDisposals(tangibleAssets.getDepreciation().getOnDisposals().getFixturesAndFittings());
        depreciation.setOtherAdjustments(tangibleAssets.getDepreciation().getOtherAdjustments().getFixturesAndFittings());
        depreciation.setAtPeriodEnd(tangibleAssets.getDepreciation().getAtPeriodEnd().getFixturesAndFittings());
        tangibleAssetsResource.setDepreciation(depreciation);
    }

    @Override
    protected void mapNetBookValueResources(TangibleAssets tangibleAssets, TangibleAssetsResource tangibleAssetsResource) {

        tangibleAssetsResource.setNetBookValueAtEndOfCurrentPeriod(
                tangibleAssets.getNetBookValue().getCurrentPeriod().getFixturesAndFittings());
        tangibleAssetsResource.setNetBookValueAtEndOfPreviousPeriod(
                tangibleAssets.getNetBookValue().getPreviousPeriod().getFixturesAndFittings());
    }
}
