package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

import javax.validation.constraints.NotNull;

public class AdditionalInformationSelection {

    @NotNull(message = "{loansToDirectors.additionalInformation.selectionNotMade}")
    private Boolean hasAdditionalInformation;

    public Boolean getHasAdditionalInformation() {
        return hasAdditionalInformation;
    }

    public void setHasAdditionalInformation(Boolean hasAdditionalInformation) {
        this.hasAdditionalInformation = hasAdditionalInformation;
    }
}
