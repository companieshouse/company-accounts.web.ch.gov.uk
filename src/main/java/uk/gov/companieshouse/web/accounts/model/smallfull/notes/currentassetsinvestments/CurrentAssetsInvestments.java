package uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class CurrentAssetsInvestments {

    @ValidationMapping("$.current_assets_investments.details")
    private String details;
}
