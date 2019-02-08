package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationPeriodMapping;

@Getter
@Setter
public class Total {

    @ValidationPeriodMapping("$.debtors.current_period")
    @ValidationMapping("$.debtors.current_period.total")
    private Long currentTotal;


    @ValidationPeriodMapping("$.debtors.previous_period")
    @ValidationMapping("$.debtors.previous_period.total")
    private Long previousTotal;
}
