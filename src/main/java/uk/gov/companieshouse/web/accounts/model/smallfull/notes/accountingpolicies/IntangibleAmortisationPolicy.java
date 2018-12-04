package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class IntangibleAmortisationPolicy {

    @NotNull(message = "{intangibleAmortisationPolicy.selectionNotMade}")
    private Boolean includeIntangibleAmortisationPolicy;

    @ValidationMapping("$.accounting_policies.intangible_fixed_assets_amortisation_policy")
    private String intangibleAmortisationPolicyDetails;
}
