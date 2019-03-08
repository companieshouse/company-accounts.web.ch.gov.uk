package uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class FixedAssetsInvestments {

    @NotBlank(message = "{fixedAssetsInvestments.details.missing}")
    @ValidationMapping("$.fixed_assets_investments.details")
    private String details;

}
