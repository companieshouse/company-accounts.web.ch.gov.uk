package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

public class Total {
    @ValidationParentMapping("$.debtors.current_period")
    @ValidationMapping("$.debtors.current_period.total")
    private Long currentTotal;
    
    @ValidationParentMapping("$.debtors.previous_period")
    @ValidationMapping("$.debtors.previous_period.total")
    private Long previousTotal;

    public Long getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(Long currentTotal) {
        this.currentTotal = currentTotal;
    }

    public Long getPreviousTotal() {
        return previousTotal;
    }

    public void setPreviousTotal(Long previousTotal) {
        this.previousTotal = previousTotal;
    }
}
