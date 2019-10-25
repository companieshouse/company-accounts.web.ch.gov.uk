package uk.gov.companieshouse.web.accounts.model.profitandloss;

import javax.validation.constraints.NotNull;

public class ProfitAndLossQuestion {

    @NotNull(message = "{profitAndLossQuestion.selectionNotMade}")
    private Boolean hasIncludedProfitAndLoss;

    public Boolean getHasIncludedProfitAndLoss() {
        return hasIncludedProfitAndLoss;
    }

    public void setHasIncludedProfitAndLoss(Boolean hasIncludedProfitAndLoss) {
        this.hasIncludedProfitAndLoss = hasIncludedProfitAndLoss;
    }
}
