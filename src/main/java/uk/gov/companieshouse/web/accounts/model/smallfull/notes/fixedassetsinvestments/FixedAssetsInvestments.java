package uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class FixedAssetsInvestments {

    @ValidationMapping("$.fixed_asset_investments.details")
    private String details;

}
