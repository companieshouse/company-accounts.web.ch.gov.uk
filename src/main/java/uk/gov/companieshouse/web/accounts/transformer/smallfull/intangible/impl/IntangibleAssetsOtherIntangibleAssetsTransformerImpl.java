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

public class IntangibleAssetsOtherIntangibleAssetsTransformerImpl extends
        IntangibleAssetsResourceTransformerImpl implements IntangibleAssetsResourceTransformer {

    @Override
    public void mapIntangibleAssetsResourceToWebModel(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        if (intangibleAssetsResource.getCost() != null) {

            IntangibleAssetsCost intangibleAssetsCost = createCost(intangibleAssets);

            CostAtPeriodStart atPeriodStart = createCostAtPeriodStart(intangibleAssetsCost);
            atPeriodStart
                    .setOtherIntangibleAssets(intangibleAssetsResource.getCost().getAtPeriodStart());

            Additions additions = createAdditions(intangibleAssetsCost);
            additions.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getAdditions());

            Disposals disposals = createDisposals(intangibleAssetsCost);
            disposals.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getDisposals());

            Revaluations revaluations = createRevaluations(intangibleAssetsCost);
            revaluations.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getRevaluations());

            Transfers transfers = createTransfers(intangibleAssetsCost);
            transfers.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getTransfers());

            CostAtPeriodEnd atPeriodEnd = createCostAtPeriodEnd(intangibleAssetsCost);
            atPeriodEnd.setOtherIntangibleAssets(intangibleAssetsResource.getCost().getAtPeriodEnd());
        }
    }

    @Override
    public boolean hasIntangibleAssetsToMapToApiResource(IntangibleAssets intangibleAssets) {
        return hasCostResources(intangibleAssets);
    }

    @Override
    public void mapIntangibleAssetsToApiResource(IntangibleAssets intangibleAssets, IntangibleApi intangibleApi) {

        IntangibleAssetsResource otherIntangibleAssets = new IntangibleAssetsResource();

        if (hasCostResources(intangibleAssets)) {
            mapCostResources(intangibleAssets, otherIntangibleAssets);
        }

        intangibleApi.setOtherIntangibleAssets(otherIntangibleAssets);
    }

    @Override
    protected boolean hasCostResources(IntangibleAssets intangibleAssets) {
        IntangibleAssetsCost cost = intangibleAssets.getCost();

        return Stream
                .of(Optional.of(cost)
                                .map(IntangibleAssetsCost::getAtPeriodStart)
                                .map(CostAtPeriodStart::getOtherIntangibleAssets)
                                .orElse(null),
                        cost.getAdditions().getOtherIntangibleAssets(),
                        cost.getDisposals().getOtherIntangibleAssets(),
                        cost.getRevaluations().getOtherIntangibleAssets(),
                        cost.getTransfers().getOtherIntangibleAssets(),
                        cost.getAtPeriodEnd().getOtherIntangibleAssets())
                .anyMatch(Objects::nonNull);
    }

    @Override
    protected void mapCostResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource) {

        Cost cost = new Cost();
        cost.setAtPeriodStart(Optional.of(intangibleAssets)
                .map(IntangibleAssets::getCost)
                .map(IntangibleAssetsCost::getAtPeriodStart)
                .map(CostAtPeriodStart::getOtherIntangibleAssets)
                .orElse(null));
        cost.setAdditions(intangibleAssets.getCost().getAdditions().getOtherIntangibleAssets());
        cost.setDisposals(intangibleAssets.getCost().getDisposals().getOtherIntangibleAssets());
        cost.setRevaluations(intangibleAssets.getCost().getRevaluations().getOtherIntangibleAssets());
        cost.setTransfers(intangibleAssets.getCost().getTransfers().getOtherIntangibleAssets());
        cost.setAtPeriodEnd(intangibleAssets.getCost().getAtPeriodEnd().getOtherIntangibleAssets());
        intangibleAssetsResource.setCost(cost);
    }
}
