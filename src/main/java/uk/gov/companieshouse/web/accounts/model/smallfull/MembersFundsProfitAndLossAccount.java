package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class MembersFundsProfitAndLossAccount {
    @ValidationMapping("$.current_period.balance_sheet.members_funds.profit_and_loss_account")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.members_funds.profit_and_loss_account")
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
