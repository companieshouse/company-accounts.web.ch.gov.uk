package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import java.time.LocalDate;

import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.TangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.netbookvalue.TangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class TangibleAssets implements Note {

    private TangibleAssetsCost cost;

    private TangibleAssetsDepreciation depreciation;

    private TangibleAssetsNetBookValue netBookValue;

    @ValidationMapping("$.tangible_assets.additional_information")
    private String additionalInformation;

    private LocalDate lastAccountsPeriodEndOn;

    private LocalDate nextAccountsPeriodStartOn;

    private LocalDate nextAccountsPeriodEndOn;

    public TangibleAssetsCost getCost() {
        return cost;
    }

    public void setCost(
        TangibleAssetsCost cost) {
        this.cost = cost;
    }

    public TangibleAssetsDepreciation getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(
        TangibleAssetsDepreciation depreciation) {
        this.depreciation = depreciation;
    }

    public TangibleAssetsNetBookValue getNetBookValue() {
        return netBookValue;
    }

    public void setNetBookValue(
        TangibleAssetsNetBookValue netBookValue) {
        this.netBookValue = netBookValue;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public LocalDate getLastAccountsPeriodEndOn() {
        return lastAccountsPeriodEndOn;
    }

    public void setLastAccountsPeriodEndOn(LocalDate lastAccountsPeriodEndOn) {
        this.lastAccountsPeriodEndOn = lastAccountsPeriodEndOn;
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
}
