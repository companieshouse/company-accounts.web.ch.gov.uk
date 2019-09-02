package uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees;

import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class Employees {
    
    private BalanceSheetHeadings balanceSheetHeadings;

    @ValidationMapping("$.employees.current_period.details")
    private String details;

    @ValidationMapping("$.employees.current_period.average_number_of_employees")
    private AverageNumberOfEmployees averageNumberOfEmployees;

    public BalanceSheetHeadings getBalanceSheetHeadings() {
        return balanceSheetHeadings;
    }

    public void setBalanceSheetHeadings(
        BalanceSheetHeadings balanceSheetHeadings) {
        this.balanceSheetHeadings = balanceSheetHeadings;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public AverageNumberOfEmployees getAverageNumberOfEmployees() {
        return averageNumberOfEmployees;
    }

    public void setAverageNumberOfEmployees(
        AverageNumberOfEmployees averageNumberOfEmployees) {
        this.averageNumberOfEmployees = averageNumberOfEmployees;
    }
}
