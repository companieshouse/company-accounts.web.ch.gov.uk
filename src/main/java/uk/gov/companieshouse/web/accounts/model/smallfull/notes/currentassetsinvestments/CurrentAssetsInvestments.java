package uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class CurrentAssetsInvestments implements Note {

    @NotBlank(message = "{currentAssetsInvestments.details.missing}")
    @ValidationMapping("$.current_assets_investments.details")
    private String currentAssetsInvestmentsDetails;

    public String getCurrentAssetsInvestmentsDetails() {
        return currentAssetsInvestmentsDetails;
    }

    public void setCurrentAssetsInvestmentsDetails(String currentAssetsInvestmentsDetails) {
        this.currentAssetsInvestmentsDetails = currentAssetsInvestmentsDetails;
    }
}
