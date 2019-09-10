package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible;


import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.IntangibleAssetsAmortisation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;

import java.time.LocalDate;

public class IntangibleAssets {

    private IntangibleAssetsCost cost;

    private IntangibleAssetsAmortisation amortisation;

    private LocalDate nextAccountsPeriodStartOn;

    private LocalDate nextAccountsPeriodEndOn;

    private LocalDate lastAccountsPeriodEndOn;

    public IntangibleAssetsCost getCost() {
        return cost;
    }

    public void setCost(IntangibleAssetsCost cost) {
        this.cost = cost;
    }

    public IntangibleAssetsAmortisation getAmortisation() {
        return amortisation;
    }

    public void setAmortisation(IntangibleAssetsAmortisation amortisation) {
        this.amortisation = amortisation;
    }

    public LocalDate getNextAccountsPeriodStartOn() {
        return nextAccountsPeriodStartOn;
    }

    public void setNextAccountsPeriodStartOn(LocalDate nextAccountsPeriodStartOn) {
        this.nextAccountsPeriodStartOn = nextAccountsPeriodStartOn;
    }

    public LocalDate getNextAccountsPeriodEndOn() {
        return nextAccountsPeriodEndOn;
    }

    public void setNextAccountsPeriodEndOn(LocalDate nextAccountsPeriodEndOn) {
        this.nextAccountsPeriodEndOn = nextAccountsPeriodEndOn;
    }

    public LocalDate getLastAccountsPeriodEndOn() {
        return lastAccountsPeriodEndOn;
    }

    public void setLastAccountsPeriodEndOn(LocalDate lastAccountsPeriodEndOn) {
        this.lastAccountsPeriodEndOn = lastAccountsPeriodEndOn;
    }
}
