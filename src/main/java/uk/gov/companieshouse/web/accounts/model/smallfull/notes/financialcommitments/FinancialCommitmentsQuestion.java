package uk.gov.companieshouse.web.accounts.model.smallfull.notes.financialcommitments;

import jakarta.validation.constraints.NotNull;

public class FinancialCommitmentsQuestion {
    @NotNull(message = "{financialCommitmentsQuestion.selectionNotMade}")
    private Boolean hasIncludedFinancialCommitments;

    public Boolean getHasIncludedFinancialCommitments() {
        return hasIncludedFinancialCommitments;
    }

    public void setHasIncludedFinancialCommitments(Boolean hasIncludedFinancialCommitments) {
        this.hasIncludedFinancialCommitments = hasIncludedFinancialCommitments;
    }
}
