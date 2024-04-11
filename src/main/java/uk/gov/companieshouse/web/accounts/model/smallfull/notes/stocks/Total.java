package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

public class Total {
    @ValidationParentMapping("$.stocks.current_period")
    @ValidationMapping("$.stocks.current_period.total")
    private Long currentTotal;

    @ValidationParentMapping("$.stocks.previous_period")
    @ValidationMapping("$.stocks.previous_period.total")
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
