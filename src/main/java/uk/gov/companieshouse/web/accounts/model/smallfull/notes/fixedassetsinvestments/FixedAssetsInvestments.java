package uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ValidationModel
public class FixedAssetsInvestments {

    @NotBlank(message = "{fixedAssetsInvestments.details.missing}")
    @ValidationMapping("$.fixed_assets_investments.details")
    private String fixedAssetsDetails;

}
