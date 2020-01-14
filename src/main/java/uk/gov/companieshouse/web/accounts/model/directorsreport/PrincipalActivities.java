package uk.gov.companieshouse.web.accounts.model.directorsreport;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class PrincipalActivities {

    @ValidationMapping("$.statements.principal_activities")
    private String principalActivitiesDetails;

    public String getPrincipalActivitiesDetails() {
        return principalActivitiesDetails;
    }

    public void setPrincipalActivitiesDetails(String principalActivitiesDetails) {
        this.principalActivitiesDetails = principalActivitiesDetails;
    }
}
