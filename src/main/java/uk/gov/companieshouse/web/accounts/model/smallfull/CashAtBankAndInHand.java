package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class CashAtBankAndInHand {
    @ValidationMapping("$.current_period.balance_sheet.current_assets.cash_at_bank_and_in_hand")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.current_assets.cash_at_bank_and_in_hand")
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
