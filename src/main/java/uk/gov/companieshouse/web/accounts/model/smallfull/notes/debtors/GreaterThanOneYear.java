package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class GreaterThanOneYear {

    @ValidationMapping("$.debtors.current_period.greater_than_one_year")
    private Long currentGreaterThanOneYear;

    @ValidationMapping("$.debtors.previous_period.greater_than_one_year")
    private Long previousGreaterThanOneYear;
}
