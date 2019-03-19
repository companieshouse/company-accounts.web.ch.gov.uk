package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Stocks {

    @ValidationMapping("$.stocks.current_period.stocks")
    private Long currentStocks;

    @ValidationMapping("$.stocks.previous_period.stocks")
    private Long previousStocks;
}
