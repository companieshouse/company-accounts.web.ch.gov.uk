package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class TradeDebtors {
    @ValidationMapping("$.debtors.current_period.trade_debtors")
    private Long currentTradeDebtors;

    @ValidationMapping("$.debtors.previous_period.trade_debtors")
    private Long previousTradeDebtors;

    public Long getCurrentTradeDebtors() {
        return currentTradeDebtors;
    }

    public void setCurrentTradeDebtors(Long currentTradeDebtors) {
        this.currentTradeDebtors = currentTradeDebtors;
    }

    public Long getPreviousTradeDebtors() {
        return previousTradeDebtors;
    }

    public void setPreviousTradeDebtors(Long previousTradeDebtors) {
        this.previousTradeDebtors = previousTradeDebtors;
    }
}
