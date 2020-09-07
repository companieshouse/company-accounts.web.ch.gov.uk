package uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors;

import javax.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class LoansToDirectorsAdditionalInfo {

    @NotBlank(message = "{loansToDirectorsAdditionalInfo.additionalInformation.details.missing}")
    private String additionalInfoDetails;

    public String getAdditionalInfoDetails() {
        return additionalInfoDetails;
    }

    public void setAdditionalInfoDetails(String additionalInfoDetails) {
        this.additionalInfoDetails = additionalInfoDetails;
    }
}
