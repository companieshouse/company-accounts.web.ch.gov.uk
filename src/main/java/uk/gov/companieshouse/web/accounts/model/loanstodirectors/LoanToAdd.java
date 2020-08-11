package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class LoanToAdd {

    @ValidationMapping("$.loan.director_name")
    private String directorName;

    @ValidationMapping("$.loan.description")
    private String description;

    private Breakdown breakdown;

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Breakdown getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(Breakdown breakdown) {
        this.breakdown = breakdown;
    }
}
