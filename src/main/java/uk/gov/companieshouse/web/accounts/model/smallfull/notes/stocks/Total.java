package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationPeriodMapping;

@Getter
@Setter
public class Total {

    @ValidationPeriodMapping("$.stocks.current_period")
    private Long currentTotal;

    @ValidationPeriodMapping("$.stocks.previous_period")
    private Long previousTotal;
}
