package uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class Employees {
    
    private BalanceSheetHeadings balanceSheetHeadings;

    @ValidationMapping("$.employees.current_period.details")
    private String details;

    @ValidationMapping("$.employees.current_period.average_number_of_employees")
    private AverageNumberOfEmployees averageNumberOfEmployees;

}
