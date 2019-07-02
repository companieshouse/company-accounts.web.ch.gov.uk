package uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies;

import javax.validation.constraints.NotNull;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class TangibleDepreciationPolicy {

    @NotNull(message = "{tangibleDepreciationPolicy.selectionNotMade}")
    private Boolean hasTangibleDepreciationPolicySelected;

    @ValidationMapping("$.accounting_policies.tangible_fixed_assets_depreciation_policy")
    private String tangibleDepreciationPolicyDetails;

    public Boolean getHasTangibleDepreciationPolicySelected() {
        return hasTangibleDepreciationPolicySelected;
    }

    public void setHasTangibleDepreciationPolicySelected(
        Boolean hasTangibleDepreciationPolicySelected) {
        this.hasTangibleDepreciationPolicySelected = hasTangibleDepreciationPolicySelected;
    }

    public String getTangibleDepreciationPolicyDetails() {
        return tangibleDepreciationPolicyDetails;
    }

    public void setTangibleDepreciationPolicyDetails(String tangibleDepreciationPolicyDetails) {
        this.tangibleDepreciationPolicyDetails = tangibleDepreciationPolicyDetails;
    }
}