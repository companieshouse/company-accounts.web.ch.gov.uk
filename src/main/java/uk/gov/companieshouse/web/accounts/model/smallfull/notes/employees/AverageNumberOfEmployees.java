package uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class AverageNumberOfEmployees {
    @ValidationMapping("$.employees.current_period.average_number_of_employees")
    private Long currentAverageNumberOfEmployees;

    @ValidationMapping("$.employees.previous_period.average_number_of_employees")
    private Long previousAverageNumberOfEmployees;

    public Long getCurrentAverageNumberOfEmployees() {
        return currentAverageNumberOfEmployees;
    }

    public void setCurrentAverageNumberOfEmployees(Long currentAverageNumberOfEmployees) {
        this.currentAverageNumberOfEmployees = currentAverageNumberOfEmployees;
    }

    public Long getPreviousAverageNumberOfEmployees() {
        return previousAverageNumberOfEmployees;
    }

    public void setPreviousAverageNumberOfEmployees(Long previousAverageNumberOfEmployees) {
        this.previousAverageNumberOfEmployees = previousAverageNumberOfEmployees;
    }
}
