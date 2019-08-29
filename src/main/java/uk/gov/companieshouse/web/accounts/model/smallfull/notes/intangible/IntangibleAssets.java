package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible;


import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;

import java.time.LocalDate;

public class IntangibleAssets {

    private IntangibleAssetsCost cost;

    private LocalDate nextAccountsPeriodStartOn;

    private LocalDate nextAccountsPeriodEndOn;

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


    public IntangibleAssetsCost getCost() {
        return cost;
    }

    public void setCost(IntangibleAssetsCost cost) {
        this.cost = cost;
    }

}
