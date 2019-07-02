package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

public class TotalShareholdersFunds {

    @ValidationParentMapping("$.current_period.balance_sheet.capital_and_reserves")
    @ValidationMapping("$.current_period.balance_sheet.capital_and_reserves.total_shareholders_funds")
    private Long currentAmount;

    @ValidationParentMapping("$.previous_period.balance_sheet.capital_and_reserves")
    @ValidationMapping("$.previous_period.balance_sheet.capital_and_reserves.total_shareholders_funds")
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
