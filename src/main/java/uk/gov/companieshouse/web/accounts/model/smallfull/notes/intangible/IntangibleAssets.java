package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible;


import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation.IntangibleAssetsAmortisation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue.IntangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import java.time.LocalDate;

@ValidationModel
public class IntangibleAssets implements Note {

    private IntangibleAssetsCost cost;

    private IntangibleAssetsAmortisation amortisation;

    private IntangibleAssetsNetBookValue netBookValue;

    @ValidationMapping("$.intangible_assets.additional_information")
    private String additionalInformation;

    private LocalDate nextAccountsPeriodStartOn;

    private LocalDate nextAccountsPeriodEndOn;

    private LocalDate lastAccountsPeriodEndOn;


    public IntangibleAssetsNetBookValue getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(IntangibleAssetsNetBookValue netBookValue) {
        this.netBookValue = netBookValue;
    }

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


    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
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
