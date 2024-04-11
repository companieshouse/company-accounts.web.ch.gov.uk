package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import jakarta.validation.constraints.NotNull;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class OtherAccountingPolicy {
    @NotNull(message = "{otherAccountingPolicy.selectionNotMade}")
    private Boolean hasOtherAccountingPolicySelected;

    @ValidationMapping("$.accounting_policies.other_accounting_policy")
    private String otherAccountingPolicyDetails;

    public Boolean getHasOtherAccountingPolicySelected() {
        return hasOtherAccountingPolicySelected;
    }

    public void setHasOtherAccountingPolicySelected(Boolean hasOtherAccountingPolicySelected) {
        this.hasOtherAccountingPolicySelected = hasOtherAccountingPolicySelected;
    }

    public String getOtherAccountingPolicyDetails() {
        return otherAccountingPolicyDetails;
    }

    public void setOtherAccountingPolicyDetails(String otherAccountingPolicyDetails) {
        this.otherAccountingPolicyDetails = otherAccountingPolicyDetails;
    }
}