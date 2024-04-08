package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotNull;

public class CompanyPolicyOnDisabledEmployeesSelection {

    @NotNull(message = "{directorsReport.companyPolicyOnDisabledEmployees.selectionNotMade}")
    private Boolean hasCompanyPolicyOnDisabledEmployees;

    public Boolean getHasCompanyPolicyOnDisabledEmployees() {
        return hasCompanyPolicyOnDisabledEmployees;
    }

    public void setHasCompanyPolicyOnDisabledEmployees(
            Boolean hasCompanyPolicyOnDisabledEmployees) {
        this.hasCompanyPolicyOnDisabledEmployees = hasCompanyPolicyOnDisabledEmployees;
    }
}
