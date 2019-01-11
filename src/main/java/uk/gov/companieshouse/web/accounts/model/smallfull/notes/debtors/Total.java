package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Total {

    @ValidationMapping("$.debtors.current_period.total")
    private Long currentTotal;

    @ValidationMapping("$.debtors.previous_period.total")
    private Long previousTotal;
}
