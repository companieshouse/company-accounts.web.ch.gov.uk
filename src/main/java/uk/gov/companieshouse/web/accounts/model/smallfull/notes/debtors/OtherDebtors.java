package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class OtherDebtors {

    @ValidationMapping("$.debtors.current_period.other_debtors")
    private Long currentOtherDebtors;

    @ValidationMapping("$.debtors.previous_period.other_debtors")
    private Long previousOtherDebtors;

    public Long getCurrentOtherDebtors() {
        return currentOtherDebtors;
    }

    public void setCurrentOtherDebtors(Long currentOtherDebtors) {
        this.currentOtherDebtors = currentOtherDebtors;
    }

    public Long getPreviousOtherDebtors() {
        return previousOtherDebtors;
    }

    public void setPreviousOtherDebtors(Long previousOtherDebtors) {
        this.previousOtherDebtors = previousOtherDebtors;
    }
}
