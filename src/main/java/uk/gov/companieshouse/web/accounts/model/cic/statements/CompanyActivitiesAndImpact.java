package uk.gov.companieshouse.web.accounts.model.cic.statements;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class CompanyActivitiesAndImpact {

    @NotBlank(message = "{companyActivitiesAndImpact.activitiesAndImpact.missing}")
    @ValidationMapping("$.cic_statements.report_statements.company_activities_and_impact")
    private String activitiesAndImpact;
}
