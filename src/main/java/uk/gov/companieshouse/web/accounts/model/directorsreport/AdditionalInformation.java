package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class AdditionalInformation {

    @NotBlank(message = "{directorsReport.additionalInformation.details.missing}")
    @ValidationMapping("$.statements.additional_information")
    private String additionalInformationDetails;

    public String getAdditionalInformationDetails() {
        return additionalInformationDetails;
    }

    public void setAdditionalInformationDetails(String additionalInformationDetails) {
        this.additionalInformationDetails = additionalInformationDetails;
    }
}
