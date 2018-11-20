package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class TurnoverPolicy {
    @NotNull(message = "{turnoverPolicy.selectionNotMade}")
    private Boolean isIncludeTurnoverSelected;

    @ValidationMapping("$.accounting_policies.turnover_policy")
    private String turnoverPolicyDetails;
}
