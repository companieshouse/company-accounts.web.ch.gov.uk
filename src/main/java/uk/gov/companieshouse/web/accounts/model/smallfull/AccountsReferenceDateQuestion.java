package uk.gov.companieshouse.web.accounts.model.smallfull;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AccountsReferenceDateQuestion {

    @NotNull(message = "{accountsReferenceDateQuestion.selectionNotMade}")
    private Boolean hasConfirmedAccountingReferenceDate;

    private LocalDate periodEndOn;

    private LocalDate periodStartOn;

    public LocalDate getPeriodEndOn() {
        return periodEndOn;
    }

    public void setPeriodEndOn(LocalDate periodEndOn) {
        this.periodEndOn = periodEndOn;
    }

    public LocalDate getPeriodStartOn() {
        return periodStartOn;
    }

    public void setPeriodStartOn(LocalDate periodStartOn) {
        this.periodStartOn = periodStartOn;
    }

    public Boolean getHasConfirmedAccountingReferenceDate() {
        return hasConfirmedAccountingReferenceDate;
    }

    public void setHasConfirmedAccountingReferenceDate(Boolean hasConfirmedAccountingReferenceDate) {
        this.hasConfirmedAccountingReferenceDate = hasConfirmedAccountingReferenceDate;
    }
}