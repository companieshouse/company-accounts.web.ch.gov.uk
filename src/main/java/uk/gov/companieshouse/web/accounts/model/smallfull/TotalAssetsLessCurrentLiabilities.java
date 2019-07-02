package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class TotalAssetsLessCurrentLiabilities {

    @ValidationMapping("$.current_period.balance_sheet.other_liabilities_or_assets.total_assets_less_current_liabilities")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.other_liabilities_or_assets.total_assets_less_current_liabilities")
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
