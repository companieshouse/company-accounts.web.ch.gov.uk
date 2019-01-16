package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class AccrualsAndDeferredIncome {

    @ValidationMapping("$.creditors_within_one_year.current_period.accruals_and_deferred_income")
    private Long currentAccrualsAndDeferredIncome;

    @ValidationMapping("$.creditors_within_one_year.previous_period.accruals_and_deferred_income")
    private Long previousAccrualsAndDeferredIncome;
}
