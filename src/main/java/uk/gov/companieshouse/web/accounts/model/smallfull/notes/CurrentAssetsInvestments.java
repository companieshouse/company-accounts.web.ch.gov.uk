package uk.gov.companieshouse.web.accounts.model.smallfull.notes;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class CurrentAssetsInvestments {

    private String details;
}
