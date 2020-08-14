package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class Breakdown {

    private Long advancesCreditsMade;

    private Long advancesCreditsRepaid;

    @ValidationMapping("$.loan.breakdown.balance_at_period_start")
    private Long balanceAtPeriodStart;

    @ValidationMapping("$.loan.breakdown.balance_at_period_end")
    private Long balanceAtPeriodEnd;

    public Long getAdvancesCreditsMade() {
        return advancesCreditsMade;
    }

    public void setAdvancesCreditsMade(Long advancesCreditsMade) {
        this.advancesCreditsMade = advancesCreditsMade;
    }

    public Long getAdvancesCreditsRepaid() {
        return advancesCreditsRepaid;
    }

    public void setAdvancesCreditsRepaid(Long advancesCreditsRepaid) {
        this.advancesCreditsRepaid = advancesCreditsRepaid;
    }

    public Long getBalanceAtPeriodStart() {
        return balanceAtPeriodStart;
    }

    public void setBalanceAtPeriodStart(Long balanceAtPeriodStart) {
        this.balanceAtPeriodStart = balanceAtPeriodStart;
    }

    public Long getBalanceAtPeriodEnd() {
        return balanceAtPeriodEnd;
    }

    public void setBalanceAtPeriodEnd(Long balanceAtPeriodEnd) {
        this.balanceAtPeriodEnd = balanceAtPeriodEnd;
    }
}
