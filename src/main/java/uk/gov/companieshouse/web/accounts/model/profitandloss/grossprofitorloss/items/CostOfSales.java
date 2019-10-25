package uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class CostOfSales {

    @ValidationMapping("$.current_period.profit_and_loss.gross_profit_or_loss.cost_of_sales")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.profit_and_loss.gross_profit_or_loss.cost_of_sales")
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
