package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TurnoverPolicy {
    @NotNull
    private Boolean isIncludeTurnoverSelected;

    private String turnoverPolicyDetails;
}
