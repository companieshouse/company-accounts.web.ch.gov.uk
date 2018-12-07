package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class ValuationInformationPolicy {

    @NotNull(message = "{valuationInformationPolicy.selectionNotMade}")
    private Boolean includeValuationInformationPolicy;

    @ValidationMapping("$.accounting_policies.valuation_information_and_policy")
    private String valuationInformationPolicyDetails;
}
