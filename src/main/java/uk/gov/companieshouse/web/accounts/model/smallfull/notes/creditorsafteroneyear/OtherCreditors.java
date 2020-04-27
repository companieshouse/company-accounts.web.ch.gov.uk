package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class OtherCreditors {

    @ValidationMapping("$.creditors_after_more_than_one_year.current_period.other_creditors")
    private Long currentOtherCreditors;

    @ValidationMapping("$.creditors_after_more_than_one_year.previous_period.other_creditors")
    private Long previousOtherCreditors;

    public Long getCurrentOtherCreditors() {
        return currentOtherCreditors;
    }

    public void setCurrentOtherCreditors(Long currentOtherCreditors) {
        this.currentOtherCreditors = currentOtherCreditors;
    }

    public Long getPreviousOtherCreditors() {
        return previousOtherCreditors;
    }

    public void setPreviousOtherCreditors(Long previousOtherCreditors) {
        this.previousOtherCreditors = previousOtherCreditors;
    }
}
