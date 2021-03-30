package uk.gov.companieshouse.web.accounts.model.relatedpartytransactions;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class RptTransactionBreakdown {

    @ValidationMapping("$.rptTransaction.breakdown.balance_at_period_start")
    private Long balanceAtPeriodStart;

    @ValidationMapping("$.rptTransaction.breakdown.balance_at_period_end")
    private Long balanceAtPeriodEnd;

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
