package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class CalledUpShareCapitalNotPaid {

    @ValidationMapping("$.current_period.balance_sheet.called_up_share_capital_not_paid")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.called_up_share_capital_not_paid")
    private Long previousAmount;

    public Long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Long getPreviousAmount() {
        return previousAmount;
    }

    public void setPreviousAmount(Long previousAmount) {
        this.previousAmount = previousAmount;
    }
}
