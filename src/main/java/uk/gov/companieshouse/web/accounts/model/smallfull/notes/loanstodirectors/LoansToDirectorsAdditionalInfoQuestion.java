package uk.gov.companieshouse.web.accounts.model.smallfull.notes.loanstodirectors;

import jakarta.validation.constraints.NotNull;

public class LoansToDirectorsAdditionalInfoQuestion {
    @NotNull(message = "{loansToDirectorsAdditionalInfoQuestion.selectionNotMade}")
    private Boolean hasIncludedLoansToDirectorsAdditionalInfo;

    public Boolean getHasIncludedLoansToDirectorsAdditionalInfo() {
        return hasIncludedLoansToDirectorsAdditionalInfo;
    }

    public void setHasIncludedLoansToDirectorsAdditionalInfo(Boolean hasIncludedLoansToDirectorsAdditionalInfo) {
        this.hasIncludedLoansToDirectorsAdditionalInfo = hasIncludedLoansToDirectorsAdditionalInfo;
    }
}
