package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class TradeCreditors {
    @ValidationMapping("$.creditors_within_one_year.current_period.trade_creditors")
    private Long currentTradeCreditors;

    @ValidationMapping("$.creditors_within_one_year.previous_period.trade_creditors")
    private Long previousTradeCreditors;

    public Long getCurrentTradeCreditors() {
        return currentTradeCreditors;
    }

    public void setCurrentTradeCreditors(Long currentTradeCreditors) {
        this.currentTradeCreditors = currentTradeCreditors;
    }

    public Long getPreviousTradeCreditors() {
        return previousTradeCreditors;
    }

    public void setPreviousTradeCreditors(Long previousTradeCreditors) {
        this.previousTradeCreditors = previousTradeCreditors;
    }
}
