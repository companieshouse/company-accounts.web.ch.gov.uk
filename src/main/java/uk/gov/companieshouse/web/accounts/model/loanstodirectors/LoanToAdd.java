package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

public class LoanToAdd {

    private String directorName;

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
