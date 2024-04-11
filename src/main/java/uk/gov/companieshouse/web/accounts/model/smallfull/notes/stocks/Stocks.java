package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class Stocks {
    @ValidationMapping("$.stocks.current_period.stocks")
    private Long currentStocks;

    @ValidationMapping("$.stocks.previous_period.stocks")
    private Long previousStocks;

    public Long getCurrentStocks() {
        return currentStocks;
    }

    public void setCurrentStocks(Long currentStocks) {
        this.currentStocks = currentStocks;
    }

    public Long getPreviousStocks() {
        return previousStocks;
    }

    public void setPreviousStocks(Long previousStocks) {
        this.previousStocks = previousStocks;
    }
}
