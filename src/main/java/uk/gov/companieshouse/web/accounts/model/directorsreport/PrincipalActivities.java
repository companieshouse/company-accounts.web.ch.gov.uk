package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class PrincipalActivities {

    @NotBlank(message = "{directorsReport.principalActivities.details.missing}")
    @ValidationMapping("$.statements.principal_activities")
    private String principalActivitiesDetails;

    public String getPrincipalActivitiesDetails() {
        return principalActivitiesDetails;
    }

    public void setPrincipalActivitiesDetails(String principalActivitiesDetails) {
        this.principalActivitiesDetails = principalActivitiesDetails;
    }
}
