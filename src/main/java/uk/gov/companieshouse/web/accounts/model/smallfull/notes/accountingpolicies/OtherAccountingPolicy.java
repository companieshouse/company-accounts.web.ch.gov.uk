package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class OtherAccountingPolicy {

    @NotNull(message = "{otherAccountingPolicy.selectionNotMade}")
    private Boolean hasOtherAccountingPolicySelected;

    @ValidationMapping("$.accounting_policies.other_accounting_policy")
    private String otherAccountingPolicyDetails;
}