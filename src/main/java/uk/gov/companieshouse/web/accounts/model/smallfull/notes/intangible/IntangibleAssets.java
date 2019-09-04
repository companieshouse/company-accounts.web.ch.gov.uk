package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible;


import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import java.time.LocalDate;

@ValidationModel
public class IntangibleAssets {

    private IntangibleAssetsCost cost;

    private LocalDate nextAccountsPeriodStartOn;

    private LocalDate nextAccountsPeriodEndOn;

    private LocalDate lastAccountsPeriodEndOn;

    public IntangibleAssetsCost getCost() {
        return cost;
    }

    public void setCost(IntangibleAssetsCost cost) {
        this.cost = cost;
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
