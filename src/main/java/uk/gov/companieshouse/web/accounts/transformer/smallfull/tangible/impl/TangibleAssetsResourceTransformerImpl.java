package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl;

import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsColumns;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssetsNetBookValue;

public abstract class TangibleAssetsResourceTransformerImpl {

    protected TangibleAssetsCost createCost(TangibleAssets tangibleAssets) {

        TangibleAssetsCost tangibleAssetsCost;

        if (tangibleAssets.getCost() == null) {
            tangibleAssetsCost = new TangibleAssetsCost();
            tangibleAssets.setCost(tangibleAssetsCost);
        } else {
            tangibleAssetsCost = tangibleAssets.getCost();
        }

        return tangibleAssetsCost;
    }

    protected TangibleAssetsColumns createCostAtPeriodStart(TangibleAssetsCost tangibleAssetsCost) {

        TangibleAssetsColumns atPeriodStart;

        if (tangibleAssetsCost.getAtPeriodStart() == null) {
            atPeriodStart = new TangibleAssetsColumns();
            tangibleAssetsCost.setAtPeriodStart(atPeriodStart);
        } else {
            atPeriodStart = tangibleAssetsCost.getAtPeriodStart();
        }

        return atPeriodStart;
    }

    protected TangibleAssetsColumns createAdditions(TangibleAssetsCost tangibleAssetsCost) {

        TangibleAssetsColumns additions;

        if (tangibleAssetsCost.getAdditions() == null) {
            additions = new TangibleAssetsColumns();
            tangibleAssetsCost.setAdditions(additions);
        } else {
            additions = tangibleAssetsCost.getAdditions();
        }

        return additions;
    }

    protected TangibleAssetsColumns createDisposals(TangibleAssetsCost tangibleAssetsCost) {

        TangibleAssetsColumns disposals;

        if (tangibleAssetsCost.getDisposals() == null) {
            disposals = new TangibleAssetsColumns();
            tangibleAssetsCost.setDisposals(disposals);
        } else {
            disposals = tangibleAssetsCost.getDisposals();
        }

        return disposals;
    }

    protected TangibleAssetsColumns createRevaluations(TangibleAssetsCost tangibleAssetsCost) {

        TangibleAssetsColumns revaluations;

        if (tangibleAssetsCost.getRevaluations() == null) {
            revaluations = new TangibleAssetsColumns();
            tangibleAssetsCost.setRevaluations(revaluations);
        } else {
            revaluations = tangibleAssetsCost.getRevaluations();
        }

        return revaluations;
    }

    protected TangibleAssetsColumns createTransfers(TangibleAssetsCost tangibleAssetsCost) {

        TangibleAssetsColumns transfers;

        if (tangibleAssetsCost.getTransfers() == null) {
            transfers = new TangibleAssetsColumns();
            tangibleAssetsCost.setTransfers(transfers);
        } else {
            transfers = tangibleAssetsCost.getTransfers();
        }

        return transfers;
    }

    protected TangibleAssetsColumns createCostAtPeriodEnd(TangibleAssetsCost tangibleAssetsCost) {

        TangibleAssetsColumns atPeriodEnd;

        if (tangibleAssetsCost.getAtPeriodEnd() == null) {
            atPeriodEnd = new TangibleAssetsColumns();
            tangibleAssetsCost.setAtPeriodEnd(atPeriodEnd);
        } else {
            atPeriodEnd = tangibleAssetsCost.getAtPeriodEnd();
        }

        return atPeriodEnd;
    }

    protected TangibleAssetsDepreciation createDepreciation(TangibleAssets tangibleAssets) {

        TangibleAssetsDepreciation tangibleAssetsDepreciation;

        if (tangibleAssets.getDepreciation() == null) {
            tangibleAssetsDepreciation = new TangibleAssetsDepreciation();
            tangibleAssets.setDepreciation(tangibleAssetsDepreciation);
        } else {
            tangibleAssetsDepreciation = tangibleAssets.getDepreciation();
        }

        return tangibleAssetsDepreciation;
    }

    protected TangibleAssetsColumns createDepreciationAtPeriodStart(TangibleAssetsDepreciation tangibleAssetsDepreciation) {

        TangibleAssetsColumns atPeriodStart;

        if (tangibleAssetsDepreciation.getAtPeriodStart() == null) {
            atPeriodStart = new TangibleAssetsColumns();
            tangibleAssetsDepreciation.setAtPeriodStart(atPeriodStart);
        } else {
            atPeriodStart = tangibleAssetsDepreciation.getAtPeriodStart();
        }

        return atPeriodStart;
    }

    protected TangibleAssetsColumns createChargeForYear(TangibleAssetsDepreciation tangibleAssetsDepreciation) {

        TangibleAssetsColumns chargeForYear;

        if (tangibleAssetsDepreciation.getChargeForYear() == null) {
            chargeForYear = new TangibleAssetsColumns();
            tangibleAssetsDepreciation.setChargeForYear(chargeForYear);
        } else {
            chargeForYear = tangibleAssetsDepreciation.getChargeForYear();
        }

        return chargeForYear;
    }

    protected TangibleAssetsColumns createOnDisposals(TangibleAssetsDepreciation tangibleAssetsDepreciation) {

        TangibleAssetsColumns onDisposals;

        if (tangibleAssetsDepreciation.getOnDisposals() == null) {
            onDisposals = new TangibleAssetsColumns();
            tangibleAssetsDepreciation.setOnDisposals(onDisposals);
        } else {
            onDisposals = tangibleAssetsDepreciation.getOnDisposals();
        }

        return onDisposals;
    }

    protected TangibleAssetsColumns createOtherAdjustments(TangibleAssetsDepreciation tangibleAssetsDepreciation) {

        TangibleAssetsColumns otherAdjustments;

        if (tangibleAssetsDepreciation.getOtherAdjustments() == null) {
            otherAdjustments = new TangibleAssetsColumns();
            tangibleAssetsDepreciation.setOtherAdjustments(otherAdjustments);
        } else {
            otherAdjustments = tangibleAssetsDepreciation.getOtherAdjustments();
        }

        return otherAdjustments;
    }

    protected TangibleAssetsColumns createDepreciationAtPeriodEnd(TangibleAssetsDepreciation tangibleAssetsDepreciation) {

        TangibleAssetsColumns atPeriodEnd;

        if (tangibleAssetsDepreciation.getAtPeriodEnd() == null) {
            atPeriodEnd = new TangibleAssetsColumns();
            tangibleAssetsDepreciation.setAtPeriodEnd(atPeriodEnd);
        } else {
            atPeriodEnd = tangibleAssetsDepreciation.getAtPeriodEnd();
        }

        return atPeriodEnd;
    }

    protected TangibleAssetsNetBookValue createNetBookValue(TangibleAssets tangibleAssets) {

        TangibleAssetsNetBookValue tangibleAssetsNetBookValue;

        if (tangibleAssets.getNetBookValue() == null) {
            tangibleAssetsNetBookValue = new TangibleAssetsNetBookValue();
            tangibleAssets.setNetBookValue(tangibleAssetsNetBookValue);
        } else {
            tangibleAssetsNetBookValue = tangibleAssets.getNetBookValue();
        }

        return tangibleAssetsNetBookValue;
    }

    protected TangibleAssetsColumns createCurrentPeriod(TangibleAssetsNetBookValue tangibleAssetsNetBookValue) {

        TangibleAssetsColumns currentPeriod;

        if (tangibleAssetsNetBookValue.getCurrentPeriod() == null) {
            currentPeriod = new TangibleAssetsColumns();
            tangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);
        } else {
            currentPeriod = tangibleAssetsNetBookValue.getCurrentPeriod();
        }

        return currentPeriod;
    }

    protected TangibleAssetsColumns createPreviousPeriod(TangibleAssetsNetBookValue tangibleAssetsNetBookValue) {

        TangibleAssetsColumns previousPeriod;

        if (tangibleAssetsNetBookValue.getPreviousPeriod() == null) {
            previousPeriod = new TangibleAssetsColumns();
            tangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);
        } else {
            previousPeriod = tangibleAssetsNetBookValue.getPreviousPeriod();
        }

        return previousPeriod;
    }

    protected abstract boolean hasCostResources(TangibleAssets tangibleAssets);

    protected abstract boolean hasDepreciationResources(TangibleAssets tangibleAssets);

    protected abstract boolean hasNetBookValueResources(TangibleAssets tangibleAssets);

    protected abstract void mapCostResources(TangibleAssets tangibleAssets,
                                            TangibleAssetsResource tangibleAssetsResource);

    protected abstract void mapDepreciationResources(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource);

    protected abstract void mapNetBookValueResources(TangibleAssets tangibleAssets,
                                                    TangibleAssetsResource tangibleAssetsResource);
}
