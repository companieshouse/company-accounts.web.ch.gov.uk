package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

@Getter
@Setter
public class Total {

    @ValidationParentMapping("$.stocks.current_period")
    @ValidationMapping("$.stocks.current_period.total")
    private Long currentTotal;

    @ValidationParentMapping("$.stocks.previous_period")
    @ValidationMapping("$.stocks.previous_period.total")
    private Long previousTotal;
}
