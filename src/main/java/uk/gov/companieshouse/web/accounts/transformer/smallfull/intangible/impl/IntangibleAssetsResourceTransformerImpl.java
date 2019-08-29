package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl;

import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Additions;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Disposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Revaluations;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Transfers;

public abstract class IntangibleAssetsResourceTransformerImpl {

    protected IntangibleAssetsCost createCost(IntangibleAssets intangibleAssets) {

        IntangibleAssetsCost intangibleAssetsCost;

        if (intangibleAssets.getCost() == null) {
            intangibleAssetsCost = new IntangibleAssetsCost();
            intangibleAssets.setCost(intangibleAssetsCost);
        } else {
            intangibleAssetsCost = intangibleAssets.getCost();
        }

        return intangibleAssetsCost;
    }

    protected CostAtPeriodStart createCostAtPeriodStart(IntangibleAssetsCost intangibleAssetsCost) {

        CostAtPeriodStart atPeriodStart;

        if (intangibleAssetsCost.getAtPeriodStart() == null) {
            atPeriodStart = new CostAtPeriodStart();
            intangibleAssetsCost.setAtPeriodStart(atPeriodStart);
        } else {
            atPeriodStart = intangibleAssetsCost.getAtPeriodStart();
        }

        return atPeriodStart;
    }

    protected Additions createAdditions(IntangibleAssetsCost intangibleAssetsCost) {

        Additions additions;

        if (intangibleAssetsCost.getAdditions() == null) {
            additions = new Additions();
            intangibleAssetsCost.setAdditions(additions);
        } else {
            additions = intangibleAssetsCost.getAdditions();
        }

        return additions;
    }

    protected Disposals createDisposals(IntangibleAssetsCost intangibleAssetsCost) {

        Disposals disposals;

        if (intangibleAssetsCost.getDisposals() == null) {
            disposals = new Disposals();
            intangibleAssetsCost.setDisposals(disposals);
        } else {
            disposals = intangibleAssetsCost.getDisposals();
        }

        return disposals;
    }

    protected Revaluations createRevaluations(IntangibleAssetsCost intangibleAssetsCost) {

        Revaluations revaluations;

        if (intangibleAssetsCost.getRevaluations() == null) {
            revaluations = new Revaluations();
            intangibleAssetsCost.setRevaluations(revaluations);
        } else {
            revaluations = intangibleAssetsCost.getRevaluations();
        }

        return revaluations;
    }

    protected Transfers createTransfers(IntangibleAssetsCost intangibleAssetsCost) {

        Transfers transfers;

        if (intangibleAssetsCost.getTransfers() == null) {
            transfers = new Transfers();
            intangibleAssetsCost.setTransfers(transfers);
        } else {
            transfers = intangibleAssetsCost.getTransfers();
        }

        return transfers;
    }

    protected CostAtPeriodEnd createCostAtPeriodEnd(IntangibleAssetsCost intangibleAssetsCost) {

        CostAtPeriodEnd atPeriodEnd;

        if (intangibleAssetsCost.getAtPeriodEnd() == null) {
            atPeriodEnd = new CostAtPeriodEnd();
            intangibleAssetsCost.setAtPeriodEnd(atPeriodEnd);
        } else {
            atPeriodEnd = intangibleAssetsCost.getAtPeriodEnd();
        }

        return atPeriodEnd;
    }

    protected abstract boolean hasCostResources(IntangibleAssets intangibleAssets);

    protected abstract void mapCostResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource);
}
