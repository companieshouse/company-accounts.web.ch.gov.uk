package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class TangibleDepreciationPolicy {

    @NotNull(message = "{tangibleDepreciationPolicy.selectionNotMade}")
    private Boolean hasTangibleDepreciationPolicySelected;

    @ValidationMapping("$.accounting_policies.tangible_fixed_assets_depreciation_policy")
    private String tangibleDepreciationPolicyDetails;

}