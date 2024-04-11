package uk.gov.companieshouse.web.accounts.model.smallfull;

import jakarta.validation.constraints.NotNull;

public class Criteria {
    @NotNull(message = "{criteria.selectionNotMade}")
    private String isCriteriaMet;

    public String getIsCriteriaMet() {
        return isCriteriaMet;
    }

    public void setIsCriteriaMet(String isCriteriaMet) {
        this.isCriteriaMet = isCriteriaMet;
    }
}
