package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class OtherCreditors {

    @ValidationMapping("$.creditors_after_one_year.current_period.other_creditors")
    private Long currentOtherCreditors;

    @ValidationMapping("$.creditors_after_one_year.previous_period.other_creditors")
    private Long previousOtherCreditors;
}
