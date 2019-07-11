package uk.gov.companieshouse.web.accounts.model.cic;

import javax.validation.constraints.NotNull;

public class CicCriteria {

    @NotNull(message = "{criteria.selectionNotMade}")
    private Boolean isCriteriaMet;

    public Boolean getIsCriteriaMet() {
        return isCriteriaMet;
    }

    public void setIsCriteriaMet(Boolean isCriteriaMet) {
        this.isCriteriaMet = isCriteriaMet;
    }
}
