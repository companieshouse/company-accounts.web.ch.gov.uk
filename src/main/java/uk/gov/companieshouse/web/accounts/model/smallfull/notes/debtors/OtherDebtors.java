package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class OtherDebtors {

    @ValidationMapping("$.debtors.current_period.other_debtors")
    private Long currentOtherDebtors;

    @ValidationMapping("$.debtors.previous_period.other_debtors")
    private Long previousOtherDebtors;
}
