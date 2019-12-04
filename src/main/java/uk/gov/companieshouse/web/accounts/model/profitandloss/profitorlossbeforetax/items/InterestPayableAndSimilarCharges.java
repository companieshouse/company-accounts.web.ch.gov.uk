package uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.items;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class InterestPayableAndSimilarCharges {

    @ValidationMapping("$.current_period.profit_and_loss.profit_or_loss_before_tax.interest_payable_and_similar_charges")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.profit_and_loss.profit_or_loss_before_tax.interest_payable_and_similar_charges")
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
