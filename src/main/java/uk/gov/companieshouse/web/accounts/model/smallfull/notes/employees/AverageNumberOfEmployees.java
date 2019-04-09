package uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class AverageNumberOfEmployees {

    @ValidationMapping("$.employees.current_period.average_number_of_employees")
    private Long currentAverageNumberOfEmployees;

    @ValidationMapping("$.employees.previous_period.average_number_of_employees")
    private Long previousAverageNumberOfEmployees;
}
