package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.netbookvalue;

public class TangibleAssetsNetBookValue {

    private CurrentPeriod currentPeriod;

    private PreviousPeriod previousPeriod;

    public CurrentPeriod getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(
        CurrentPeriod currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public PreviousPeriod getPreviousPeriod() {
        return previousPeriod;
    }

    public void setPreviousPeriod(
        PreviousPeriod previousPeriod) {
        this.previousPeriod = previousPeriod;
    }
}