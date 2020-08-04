package uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors;

import javax.validation.constraints.NotNull;

public class LoansToDirectorsQuestion {

    @NotNull(message = "{loansToDirectorsQuestion.selectionNotMade}")
    private Boolean hasIncludedLoansToDirectors;

    public Boolean getHasIncludedLoansToDirectors() {
        return hasIncludedLoansToDirectors;
    }

    public void setHasIncludedLoansToDirectors(Boolean hasIncludedLoansToDirectors) {
        this.hasIncludedLoansToDirectors = hasIncludedLoansToDirectors;
    }
}
