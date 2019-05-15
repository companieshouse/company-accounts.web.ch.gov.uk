package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Total {

    @ValidationMapping({"$.creditors_within_one_year.current_period.total", "$.creditors_within_one_year.current_period"})
    private Long currentTotal;

    @ValidationMapping({"$.creditors_within_one_year.previous_period.total", "$.creditors_within_one_year.previous_period"})
    private Long previousTotal;
}
