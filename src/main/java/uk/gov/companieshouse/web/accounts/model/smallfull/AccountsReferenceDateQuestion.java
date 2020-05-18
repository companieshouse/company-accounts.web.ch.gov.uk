package uk.gov.companieshouse.web.accounts.model.smallfull;

import javax.validation.constraints.NotNull;

public class AccountsReferenceDateQuestion {

    @NotNull(message = "{accountsReferenceDateQuestion.selectionNotMade}")
    private Boolean hasConfirmedAccountingReferenceDate;

    public Boolean getHasConfirmedAccountingReferenceDate() {
        return hasConfirmedAccountingReferenceDate;
    }

    public void setHasConfirmedAccountingReferenceDate(Boolean hasConfirmedAccountingReferenceDate) {
        this.hasConfirmedAccountingReferenceDate = hasConfirmedAccountingReferenceDate;
    }
}