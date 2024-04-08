package uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors;

import jakarta.validation.constraints.NotBlank;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class LoansToDirectorsAdditionalInfo {

    @NotBlank(message = "{loansToDirectorsAdditionalInfo.additionalInformation.details.missing}")
    @ValidationMapping("$.additional_information.details")
    private String additionalInfoDetails;

    public String getAdditionalInfoDetails() {
        return additionalInfoDetails;
    }

    public void setAdditionalInfoDetails(String additionalInfoDetails) {
        this.additionalInfoDetails = additionalInfoDetails;
    }
}
