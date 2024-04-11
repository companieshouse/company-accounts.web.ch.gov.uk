package uk.gov.companieshouse.web.accounts.model.smallfull.notes.relatedpartytransactions;

import jakarta.validation.constraints.NotNull;

public class RelatedPartyTransactionsQuestion {
    @NotNull(message = "{relatedPartyTransactionsQuestion.selectionNotMade}")
    private Boolean hasIncludedRelatedPartyTransactions;

    public Boolean getHasIncludedRelatedPartyTransactions() {
        return hasIncludedRelatedPartyTransactions;
    }

    public void setHasIncludedRelatedPartyTransactions(Boolean hasIncludedRelatedPartyTransactions) {
        this.hasIncludedRelatedPartyTransactions = hasIncludedRelatedPartyTransactions;
    }
}
