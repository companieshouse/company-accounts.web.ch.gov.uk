package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Total {

    @ValidationMapping({"$.stocks.current_period.total", "$.stocks.current_period"})
    private Long currentTotal;

    @ValidationMapping({"$.stocks.previous_period.total", "$.stocks.previous_period"})
    private Long previousTotal;
}
