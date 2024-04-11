package uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class OtherOperatingIncome {
    @ValidationMapping("$.current_period.profit_and_loss.operating_profit_or_loss.other_operating_income")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.profit_and_loss.operating_profit_or_loss.other_operating_income")
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
