package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class CompanyPolicyOnDisabledEmployees {
    @NotBlank(message = "{directorsReport.companyPolicyOnDisabledEmployees.details.missing}")
    @ValidationMapping("$.statements.company_policy_on_disabled_employees")
    private String companyPolicyOnDisabledEmployeesDetails;

    public String getCompanyPolicyOnDisabledEmployeesDetails() {
        return companyPolicyOnDisabledEmployeesDetails;
    }

    public void setCompanyPolicyOnDisabledEmployeesDetails(
            String companyPolicyOnDisabledEmployeesDetails) {
        this.companyPolicyOnDisabledEmployeesDetails = companyPolicyOnDisabledEmployeesDetails;
    }
}
