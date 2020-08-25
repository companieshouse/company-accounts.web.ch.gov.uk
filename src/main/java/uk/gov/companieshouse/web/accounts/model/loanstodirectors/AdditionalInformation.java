package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdditionalInformation {

    @JsonProperty("has_provided_additional_information")
    private Boolean hasProvidedAdditionalInformation;

    public Boolean getHasProvidedAdditionalInformation() {
        return hasProvidedAdditionalInformation;
    }

    public void setHasProvidedAdditionalInformation(Boolean hasProvidedAdditionalInformation) {
        this.hasProvidedAdditionalInformation = hasProvidedAdditionalInformation;
    }

    private String additionalInformationDetails;

    public String getAdditionalInformationDetails() {
        return additionalInformationDetails;
    }

    public void setAdditionalInformationDetails(String additionalInformationDetails) {
        this.additionalInformationDetails = additionalInformationDetails;
    }
}
