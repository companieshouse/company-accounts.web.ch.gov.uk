package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class GreaterThanOneYear {
    @ValidationMapping("$.debtors.current_period.greater_than_one_year")
    private Long currentGreaterThanOneYear;

    @ValidationMapping("$.debtors.previous_period.greater_than_one_year")
    private Long previousGreaterThanOneYear;

    public Long getCurrentGreaterThanOneYear() {
        return currentGreaterThanOneYear;
    }

    public void setCurrentGreaterThanOneYear(Long currentGreaterThanOneYear) {
        this.currentGreaterThanOneYear = currentGreaterThanOneYear;
    }

    public Long getPreviousGreaterThanOneYear() {
        return previousGreaterThanOneYear;
    }

    public void setPreviousGreaterThanOneYear(Long previousGreaterThanOneYear) {
        this.previousGreaterThanOneYear = previousGreaterThanOneYear;
    }
}
