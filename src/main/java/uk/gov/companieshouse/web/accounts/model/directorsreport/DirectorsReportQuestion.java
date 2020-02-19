package uk.gov.companieshouse.web.accounts.model.directorsreport;

import javax.validation.constraints.NotNull;

public class DirectorsReportQuestion {

    @NotNull(message = "{directorsReportQuestion.selectionNotMade}")
    private Boolean hasIncludedDirectorsReport;

    public Boolean getHasIncludedDirectorsReport() {
        return hasIncludedDirectorsReport;
    }

    public void setHasIncludedDirectorsReport(Boolean hasIncludedDirectorsReport) {
        this.hasIncludedDirectorsReport = hasIncludedDirectorsReport;
    }
}
