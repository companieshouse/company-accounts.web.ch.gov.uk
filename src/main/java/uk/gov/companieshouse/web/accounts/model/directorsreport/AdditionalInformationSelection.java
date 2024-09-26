package uk.gov.companieshouse.web.accounts.model.directorsreport;

import jakarta.validation.constraints.NotNull;

public class AdditionalInformationSelection {

    @NotNull(message = "{directorsReport.additionalInformation.selectionNotMade}")
    private Boolean hasAdditionalInformation;

    public Boolean getHasAdditionalInformation() {
        return hasAdditionalInformation;
    }

    public void setHasAdditionalInformation(Boolean hasAdditionalInformation) {
        this.hasAdditionalInformation = hasAdditionalInformation;
    }
}
