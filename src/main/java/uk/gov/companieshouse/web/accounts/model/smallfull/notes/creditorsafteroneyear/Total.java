package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

public class Total {

    @ValidationParentMapping("$.creditors_after_one_year.current_period")
    @ValidationMapping("$.creditors_after_one_year.current_period.total")
    private Long currentTotal;

    @ValidationParentMapping("$.creditors_after_one_year.previous_period")
    @ValidationMapping("$.creditors_after_one_year.previous_period.total")
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
