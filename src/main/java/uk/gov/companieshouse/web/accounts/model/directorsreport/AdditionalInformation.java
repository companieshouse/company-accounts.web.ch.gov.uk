package uk.gov.companieshouse.web.accounts.model.directorsreport;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class AdditionalInformation {

    @ValidationMapping("$.statements.additional_information")
    private String additionalInformationDetails;

    public String getAdditionalInformationDetails() {
        return additionalInformationDetails;
    }

    public void setAdditionalInformationDetails(String additionalInformationDetails) {
        this.additionalInformationDetails = additionalInformationDetails;
    }
}
