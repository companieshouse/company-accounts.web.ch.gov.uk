package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl;

import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.Cost;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Additions;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Disposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Revaluations;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Transfers;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsResourceTransformer;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class IntangibleAssetsGoodwillTransformerImpl extends
        IntangibleAssetsResourceTransformerImpl implements IntangibleAssetsResourceTransformer {


    @Override
    public void mapIntangibleAssetsResourceToWebModel(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        if (intangibleAssetsResource.getCost() != null) {

            IntangibleAssetsCost intangibleAssetsCost = createCost(intangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(intangibleAssetsCost);
            atPeriodStart
                    .setGoodwill(intangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(intangibleAssetsCost);
            additions.setGoodwill(intangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(intangibleAssetsCost);
            disposals.setGoodwill(intangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(intangibleAssetsCost);
            revaluations.setGoodwill(intangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(intangibleAssetsCost);
            transfers.setGoodwill(intangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(intangibleAssetsCost);
            atPeriodEnd.setGoodwill(intangibleAssetsResource.getCost().getAtPeriodEnd());
        }
    }

    @Override
    public boolean hasIntangibleAssetsToMapToApiresource(IntangibleAssets intangibleAssets) {
        return hasCostResources(intangibleAssets);
    }

    @Override
    public void mapIntangibleAssetsToApiResource(IntangibleAssets intangibleAssets, IntangibleApi intangibleApi) {

        IntangibleAssetsResource goodwill = new IntangibleAssetsResource();

        if (hasCostResources(intangibleAssets)) {
            mapCostResources(intangibleAssets, goodwill);
        }

        intangibleApi.setGoodwill(goodwill);
    }

    @Override
    protected boolean hasCostResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsCost cost = intangibleAssets.getCost();

        return Stream
                .of(Optional.of(cost)
                                .map(IntangibleAssetsCost::getAtPeriodStart)
                                .map(CostAtPeriodStart::getGoodwill)
                                .orElse(null),
                        cost.getAdditions().getGoodwill(),
                        cost.getDisposals().getGoodwill(),
                        cost.getRevaluations().getGoodwill(),
                        cost.getTransfers().getGoodwill(),
                        cost.getAtPeriodEnd().getGoodwill())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getCost)
                .map(IntangibleAssetsCost::getAtPeriodStart)
                .map(CostAtPeriodStart::getGoodwill)
                .orElse(null));
        cost.setAdditions(intangibleAssets.getCost().getAdditions().getGoodwill());
        cost.setDisposals(intangibleAssets.getCost().getDisposals().getGoodwill());
        cost.setRevaluations(intangibleAssets.getCost().getRevaluations().getGoodwill());
        cost.setTransfers(intangibleAssets.getCost().getTransfers().getGoodwill());
        cost.setAtPeriodEnd(intangibleAssets.getCost().getAtPeriodEnd().getGoodwill());
        intangibleAssetsResource.setCost(cost);
    }
}
