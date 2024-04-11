package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import jakarta.validation.constraints.NotNull;

@ValidationModel
public class IntangibleAmortisationPolicy {
    @NotNull(message = "{intangibleAmortisationPolicy.selectionNotMade}")
    private Boolean includeIntangibleAmortisationPolicy;

    @ValidationMapping("$.accounting_policies.intangible_fixed_assets_amortisation_policy")
    private String intangibleAmortisationPolicyDetails;

    public Boolean getIncludeIntangibleAmortisationPolicy() {
        return includeIntangibleAmortisationPolicy;
    }

    public void setIncludeIntangibleAmortisationPolicy(
        Boolean includeIntangibleAmortisationPolicy) {
        this.includeIntangibleAmortisationPolicy = includeIntangibleAmortisationPolicy;
    }

    public String getIntangibleAmortisationPolicyDetails() {
        return intangibleAmortisationPolicyDetails;
    }

    public void setIntangibleAmortisationPolicyDetails(String intangibleAmortisationPolicyDetails) {
        this.intangibleAmortisationPolicyDetails = intangibleAmortisationPolicyDetails;
    }
}
