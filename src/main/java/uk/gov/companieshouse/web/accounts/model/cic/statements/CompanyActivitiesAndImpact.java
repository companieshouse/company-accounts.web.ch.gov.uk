package uk.gov.companieshouse.web.accounts.model.cic.statements;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class CompanyActivitiesAndImpact {

    @NotBlank(message = "{companyActivitiesAndImpact.activitiesAndImpact.missing}")
    @ValidationMapping("$.cic_statements.report_statements.company_activities_and_impact")
    private String activitiesAndImpact;

    public String getActivitiesAndImpact() {
        return activitiesAndImpact;
    }

    public void setActivitiesAndImpact(String activitiesAndImpact) {
        this.activitiesAndImpact = activitiesAndImpact;
    }
}
