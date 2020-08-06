package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

public class Breakdown {

    private Long advancesCreditsMade;

    private Long advancesCreditsRepaid;

    private Long balanceAtPeriodStart;

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
