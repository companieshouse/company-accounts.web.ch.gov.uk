package uk.gov.companieshouse.web.accounts.model.state;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DirectorsReportStatements implements Serializable {

    @JsonProperty("has_provided_principal_activities")
    private Boolean hasProvidedPrincipalActivities;

    @JsonProperty("has_provided_political_and_charitable_donations")
    private Boolean hasProvidedPoliticalAndCharitableDonations;

    @JsonProperty("has_provided_company_policy_on_disabled_employees")
    private Boolean hasProvidedCompanyPolicyOnDisabledEmployees;

    @JsonProperty("has_provided_additional_information")
    private Boolean hasProvidedAdditionalInformation;

    public Boolean getHasProvidedPrincipalActivities() {
        return hasProvidedPrincipalActivities;
    }

    public void setHasProvidedPrincipalActivities(Boolean hasProvidedPrincipalActivities) {
        this.hasProvidedPrincipalActivities = hasProvidedPrincipalActivities;
    }

    public Boolean getHasProvidedPoliticalAndCharitableDonations() {
        return hasProvidedPoliticalAndCharitableDonations;
    }

    public void setHasProvidedPoliticalAndCharitableDonations(
            Boolean hasProvidedPoliticalAndCharitableDonations) {
        this.hasProvidedPoliticalAndCharitableDonations = hasProvidedPoliticalAndCharitableDonations;
    }

    public Boolean getHasProvidedCompanyPolicyOnDisabledEmployees() {
        return hasProvidedCompanyPolicyOnDisabledEmployees;
    }

    public void setHasProvidedCompanyPolicyOnDisabledEmployees(
            Boolean hasProvidedCompanyPolicyOnDisabledEmployees) {
        this.hasProvidedCompanyPolicyOnDisabledEmployees = hasProvidedCompanyPolicyOnDisabledEmployees;
    }

    public Boolean getHasProvidedAdditionalInformation() {
        return hasProvidedAdditionalInformation;
    }

    public void setHasProvidedAdditionalInformation(Boolean hasProvidedAdditionalInformation) {
        this.hasProvidedAdditionalInformation = hasProvidedAdditionalInformation;
    }
}
