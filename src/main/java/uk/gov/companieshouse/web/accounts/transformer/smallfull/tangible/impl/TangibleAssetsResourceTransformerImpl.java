package uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.impl;

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

    protected CostAtPeriodStart createCostAtPeriodStart(TangibleAssetsCost tangibleAssetsCost) {
        CostAtPeriodStart atPeriodStart;

        if (tangibleAssetsCost.getAtPeriodStart() == null) {
            atPeriodStart = new CostAtPeriodStart();
            tangibleAssetsCost.setAtPeriodStart(atPeriodStart);
        } else {
            atPeriodStart = tangibleAssetsCost.getAtPeriodStart();
        }

        return atPeriodStart;
    }

    protected Additions createAdditions(TangibleAssetsCost tangibleAssetsCost) {
        Additions additions;

        if (tangibleAssetsCost.getAdditions() == null) {
            additions = new Additions();
            tangibleAssetsCost.setAdditions(additions);
        } else {
            additions = tangibleAssetsCost.getAdditions();
        }

        return additions;
    }

    protected Disposals createDisposals(TangibleAssetsCost tangibleAssetsCost) {
        Disposals disposals;

        if (tangibleAssetsCost.getDisposals() == null) {
            disposals = new Disposals();
            tangibleAssetsCost.setDisposals(disposals);
        } else {
            disposals = tangibleAssetsCost.getDisposals();
        }

        return disposals;
    }

    protected Revaluations createRevaluations(TangibleAssetsCost tangibleAssetsCost) {
        Revaluations revaluations;

        if (tangibleAssetsCost.getRevaluations() == null) {
            revaluations = new Revaluations();
            tangibleAssetsCost.setRevaluations(revaluations);
        } else {
            revaluations = tangibleAssetsCost.getRevaluations();
        }

        return revaluations;
    }

    protected Transfers createTransfers(TangibleAssetsCost tangibleAssetsCost) {
        Transfers transfers;

        if (tangibleAssetsCost.getTransfers() == null) {
            transfers = new Transfers();
            tangibleAssetsCost.setTransfers(transfers);
        } else {
            transfers = tangibleAssetsCost.getTransfers();
        }

        return transfers;
    }

    protected CostAtPeriodEnd createCostAtPeriodEnd(TangibleAssetsCost tangibleAssetsCost) {
        CostAtPeriodEnd atPeriodEnd;

        if (tangibleAssetsCost.getAtPeriodEnd() == null) {
            atPeriodEnd = new CostAtPeriodEnd();
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

    protected DepreciationAtPeriodStart createDepreciationAtPeriodStart(TangibleAssetsDepreciation tangibleAssetsDepreciation) {
        DepreciationAtPeriodStart atPeriodStart;

        if (tangibleAssetsDepreciation.getAtPeriodStart() == null) {
            atPeriodStart = new DepreciationAtPeriodStart();
            tangibleAssetsDepreciation.setAtPeriodStart(atPeriodStart);
        } else {
            atPeriodStart = tangibleAssetsDepreciation.getAtPeriodStart();
        }

        return atPeriodStart;
    }

    protected ChargeForYear createChargeForYear(TangibleAssetsDepreciation tangibleAssetsDepreciation) {
        ChargeForYear chargeForYear;

        if (tangibleAssetsDepreciation.getChargeForYear() == null) {
            chargeForYear = new ChargeForYear();
            tangibleAssetsDepreciation.setChargeForYear(chargeForYear);
        } else {
            chargeForYear = tangibleAssetsDepreciation.getChargeForYear();
        }

        return chargeForYear;
    }

    protected OnDisposals createOnDisposals(TangibleAssetsDepreciation tangibleAssetsDepreciation) {
        OnDisposals onDisposals;

        if (tangibleAssetsDepreciation.getOnDisposals() == null) {
            onDisposals = new OnDisposals();
            tangibleAssetsDepreciation.setOnDisposals(onDisposals);
        } else {
            onDisposals = tangibleAssetsDepreciation.getOnDisposals();
        }

        return onDisposals;
    }

    protected OtherAdjustments createOtherAdjustments(TangibleAssetsDepreciation tangibleAssetsDepreciation) {
        OtherAdjustments otherAdjustments;

        if (tangibleAssetsDepreciation.getOtherAdjustments() == null) {
            otherAdjustments = new OtherAdjustments();
            tangibleAssetsDepreciation.setOtherAdjustments(otherAdjustments);
        } else {
            otherAdjustments = tangibleAssetsDepreciation.getOtherAdjustments();
        }

        return otherAdjustments;
    }

    protected DepreciationAtPeriodEnd createDepreciationAtPeriodEnd(TangibleAssetsDepreciation tangibleAssetsDepreciation) {
        DepreciationAtPeriodEnd atPeriodEnd;

        if (tangibleAssetsDepreciation.getAtPeriodEnd() == null) {
            atPeriodEnd = new DepreciationAtPeriodEnd();
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

    protected CurrentPeriod createCurrentPeriod(TangibleAssetsNetBookValue tangibleAssetsNetBookValue) {
        CurrentPeriod currentPeriod;

        if (tangibleAssetsNetBookValue.getCurrentPeriod() == null) {
            currentPeriod = new CurrentPeriod();
            tangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);
        } else {
            currentPeriod = tangibleAssetsNetBookValue.getCurrentPeriod();
        }

        return currentPeriod;
    }

    protected PreviousPeriod createPreviousPeriod(TangibleAssetsNetBookValue tangibleAssetsNetBookValue) {
        PreviousPeriod previousPeriod;

        if (tangibleAssetsNetBookValue.getPreviousPeriod() == null) {
            previousPeriod = new PreviousPeriod();
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
