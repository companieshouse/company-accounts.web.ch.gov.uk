package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotNull;

public class PrincipalActivitiesSelection {
    @NotNull(message = "{directorsReport.principalActivities.selectionNotMade}")
    private Boolean hasPrincipalActivities;

    public Boolean getHasPrincipalActivities() {
        return hasPrincipalActivities;
    }

    public void setHasPrincipalActivities(Boolean hasPrincipalActivities) {
        this.hasPrincipalActivities = hasPrincipalActivities;
    }
}
