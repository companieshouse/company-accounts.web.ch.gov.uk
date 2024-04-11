package uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class TotalProfitOrLossBeforeTax {
    @ValidationMapping("$.current_period.profit_and_loss.profit_or_loss_before_tax.total_profit_or_loss_before_tax")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.profit_and_loss.profit_or_loss_before_tax.total_profit_or_loss_before_tax")
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
