package uk.gov.companieshouse.web.accounts.model.corporationtax;

import jakarta.validation.constraints.NotNull;

public class CorporationTax {
    @NotNull(message = "{corporationTax.selectionNotMade}")
    private Boolean fileChoice;

    public Boolean getFileChoice() {
        return fileChoice;
    }

    public void setFileChoice(Boolean fileChoice) {
        this.fileChoice = fileChoice;
    }
}
