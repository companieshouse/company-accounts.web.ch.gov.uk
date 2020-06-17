package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import javax.validation.constraints.NotNull;

@ValidationModel
public class TurnoverPolicy {
    @NotNull(message = "{turnoverPolicy.selectionNotMade}")
    private Boolean isIncludeTurnoverSelected;

    @ValidationMapping("$.accounting_policies.turnover_policy")
    private String turnoverPolicyDetails;

    public Boolean getIsIncludeTurnoverSelected() {
        return isIncludeTurnoverSelected;
    }

    public void setIsIncludeTurnoverSelected(Boolean includeTurnoverSelected) {
        isIncludeTurnoverSelected = includeTurnoverSelected;
    }

    public String getTurnoverPolicyDetails() {
        return turnoverPolicyDetails;
    }

    public void setTurnoverPolicyDetails(String turnoverPolicyDetails) {
        this.turnoverPolicyDetails = turnoverPolicyDetails;
    }
}
