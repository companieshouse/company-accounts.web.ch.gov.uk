package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class AccrualsAndDeferredIncome {

    @ValidationMapping("$.creditors_within_one_year.current_period.accruals_and_deferred_income")
    private Long currentAccrualsAndDeferredIncome;

    @ValidationMapping("$.creditors_within_one_year.previous_period.accruals_and_deferred_income")
    private Long previousAccrualsAndDeferredIncome;

    public Long getCurrentAccrualsAndDeferredIncome() {
        return currentAccrualsAndDeferredIncome;
    }

    public void setCurrentAccrualsAndDeferredIncome(Long currentAccrualsAndDeferredIncome) {
        this.currentAccrualsAndDeferredIncome = currentAccrualsAndDeferredIncome;
    }

    public Long getPreviousAccrualsAndDeferredIncome() {
        return previousAccrualsAndDeferredIncome;
    }

    public void setPreviousAccrualsAndDeferredIncome(Long previousAccrualsAndDeferredIncome) {
        this.previousAccrualsAndDeferredIncome = previousAccrualsAndDeferredIncome;
    }
}
