package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationPeriodMapping;

@Getter
@Setter
public class Total {

    @ValidationPeriodMapping("$.creditors_within_one_year.current_period")
    @ValidationMapping("$.creditors_within_one_year.current_period.total")
    private Long currentTotal;

    @ValidationPeriodMapping("$.creditors_within_one_year.previous_period")
    @ValidationMapping("$.creditors_within_one_year.previous_period.total")
    private Long previousTotal;
}
