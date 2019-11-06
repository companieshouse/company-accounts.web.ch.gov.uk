package uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.impl;

import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleAssetsResource;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.AmortisationAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.AmortisationAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.ChargeForYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.IntangibleAssetsAmortisation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.OnDisposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.OtherAdjustments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Additions;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodEnd;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.CostAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Disposals;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Revaluations;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.Transfers;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue.IntangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue.PreviousPeriod;

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

    protected IntangibleAssetsAmortisation createAmortisation(IntangibleAssets intangibleAssets) {

        IntangibleAssetsAmortisation intangibleAssetsAmortisation;

        if (intangibleAssets.getAmortisation() == null) {
            intangibleAssetsAmortisation = new IntangibleAssetsAmortisation();
            intangibleAssets.setAmortisation(intangibleAssetsAmortisation);
        } else {
            intangibleAssetsAmortisation = intangibleAssets.getAmortisation();
        }

        return intangibleAssetsAmortisation;


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

    protected AmortisationAtPeriodStart createAmortisationAtPeriodStart(IntangibleAssetsAmortisation intangibleAssetsAmortisation) {

        AmortisationAtPeriodStart atPeriodStart;

        if (intangibleAssetsAmortisation.getAtPeriodStart() == null) {
            atPeriodStart = new AmortisationAtPeriodStart();
            intangibleAssetsAmortisation.setAtPeriodStart(atPeriodStart);
        } else {
            atPeriodStart = intangibleAssetsAmortisation.getAtPeriodStart();
        }

        return atPeriodStart;

    }

    protected ChargeForYear createAmortisationChargeForYear(IntangibleAssetsAmortisation intangibleAssetsAmortisation) {

        ChargeForYear chargeForYear;

        if (intangibleAssetsAmortisation.getChargeForYear() == null) {
            chargeForYear = new ChargeForYear();
            intangibleAssetsAmortisation.setChargeForYear(chargeForYear);
        } else {
            chargeForYear = intangibleAssetsAmortisation.getChargeForYear();
        }

        return chargeForYear;
    }

    protected OnDisposals createOnDisposals(IntangibleAssetsAmortisation intangibleAssetsAmortisation) {

        OnDisposals onDisposals;

        if (intangibleAssetsAmortisation.getOnDisposals() == null) {
            onDisposals = new OnDisposals();
            intangibleAssetsAmortisation.setOnDisposals(onDisposals);
        } else {
            onDisposals = intangibleAssetsAmortisation.getOnDisposals();
        }

        return onDisposals;
    }

    protected OtherAdjustments createOtherAdjustments(IntangibleAssetsAmortisation intangibleAssetsAmortisation) {

        OtherAdjustments otherAdjustments;

        if (intangibleAssetsAmortisation.getOtherAdjustments() == null) {
            otherAdjustments = new OtherAdjustments();
            intangibleAssetsAmortisation.setOtherAdjustments(otherAdjustments);
        } else {
            otherAdjustments = intangibleAssetsAmortisation.getOtherAdjustments();
        }

        return otherAdjustments;
    }

    protected AmortisationAtPeriodEnd createAmortisationAtPeriodEnd(IntangibleAssetsAmortisation intangibleAssetsAmortisation) {

        AmortisationAtPeriodEnd atPeriodEnd;

        if (intangibleAssetsAmortisation.getAtPeriodEnd() == null) {
            atPeriodEnd = new AmortisationAtPeriodEnd();
            intangibleAssetsAmortisation.setAtPeriodEnd(atPeriodEnd);
        } else {
            atPeriodEnd = intangibleAssetsAmortisation.getAtPeriodEnd();
        }

        return atPeriodEnd;
    }

    protected IntangibleAssetsNetBookValue createNetBookValue(IntangibleAssets intangibleAssets) {

        IntangibleAssetsNetBookValue intangibleAssetsNetBookValue;

        if (intangibleAssets.getNetBookValue() == null) {
            intangibleAssetsNetBookValue = new IntangibleAssetsNetBookValue();
            intangibleAssets.setNetBookValue(intangibleAssetsNetBookValue);
        } else {
            intangibleAssetsNetBookValue = intangibleAssets.getNetBookValue();
        }

        return intangibleAssetsNetBookValue;
    }

    protected CurrentPeriod createCurrentPeriod(IntangibleAssetsNetBookValue intangibleAssetsNetBookValue) {

        CurrentPeriod currentPeriod;

        if (intangibleAssetsNetBookValue.getCurrentPeriod() == null) {
            currentPeriod = new CurrentPeriod();
            intangibleAssetsNetBookValue.setCurrentPeriod(currentPeriod);
        } else {
            currentPeriod = intangibleAssetsNetBookValue.getCurrentPeriod();
        }

        return currentPeriod;
    }

    protected PreviousPeriod createPreviousPeriod(IntangibleAssetsNetBookValue intangibleAssetsNetBookValue) {

        PreviousPeriod previousPeriod;

        if (intangibleAssetsNetBookValue.getPreviousPeriod() == null) {
            previousPeriod = new PreviousPeriod();
            intangibleAssetsNetBookValue.setPreviousPeriod(previousPeriod);
        } else {
            previousPeriod = intangibleAssetsNetBookValue.getPreviousPeriod();
        }

        return previousPeriod;
    }

    protected abstract boolean hasCostResources(IntangibleAssets intangibleAssets);

    protected abstract boolean hasAmortisationResources(IntangibleAssets intangibleAssets);

    protected abstract boolean hasNetBookValueResources(IntangibleAssets intangibleAssets);

    protected abstract void mapCostResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource);

    protected abstract void mapAmortisationResources(IntangibleAssets intangibleAssets, IntangibleAssetsResource intangibleAssetsResource);

    protected abstract void mapNetBookValueResources(IntangibleAssets intangibleAssets,
                                                     IntangibleAssetsResource intangibleAssetsResource);
}
