package uk.gov.companieshouse.web.accounts.model.smallfull;

public class BalanceSheetHeadings {
    private String currentPeriodHeading;

    private String previousPeriodHeading;

    public String getCurrentPeriodHeading() {
        return currentPeriodHeading;
    }

    public void setCurrentPeriodHeading(String currentPeriodHeading) {
        this.currentPeriodHeading = currentPeriodHeading;
    }

    public String getPreviousPeriodHeading() {
        return previousPeriodHeading;
    }

    public void setPreviousPeriodHeading(String previousPeriodHeading) {
        this.previousPeriodHeading = previousPeriodHeading;
    }
}
