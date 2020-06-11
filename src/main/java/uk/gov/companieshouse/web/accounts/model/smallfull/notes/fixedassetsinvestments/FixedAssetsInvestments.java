package uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments;

import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import javax.validation.constraints.NotBlank;

@ValidationModel
public class FixedAssetsInvestments implements Note {

    @NotBlank(message = "{fixedAssetsInvestments.details.missing}")
    @ValidationMapping("$.fixed_assets_investments.details")
    private String fixedAssetsDetails;

    public String getFixedAssetsDetails() {
        return fixedAssetsDetails;
    }

    public void setFixedAssetsDetails(String fixedAssetsDetails) {
        this.fixedAssetsDetails = fixedAssetsDetails;
    }
}
