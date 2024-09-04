package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import jakarta.validation.constraints.NotNull;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class ValuationInformationPolicy {

    @NotNull(message = "{valuationInformationPolicy.selectionNotMade}")
    private Boolean includeValuationInformationPolicy;

    @ValidationMapping("$.accounting_policies.valuation_information_and_policy")
    private String valuationInformationPolicyDetails;

    public Boolean getIncludeValuationInformationPolicy() {
        return includeValuationInformationPolicy;
    }

    public void setIncludeValuationInformationPolicy(Boolean includeValuationInformationPolicy) {
        this.includeValuationInformationPolicy = includeValuationInformationPolicy;
    }

    public String getValuationInformationPolicyDetails() {
        return valuationInformationPolicyDetails;
    }

    public void setValuationInformationPolicyDetails(String valuationInformationPolicyDetails) {
        this.valuationInformationPolicyDetails = valuationInformationPolicyDetails;
    }
}
