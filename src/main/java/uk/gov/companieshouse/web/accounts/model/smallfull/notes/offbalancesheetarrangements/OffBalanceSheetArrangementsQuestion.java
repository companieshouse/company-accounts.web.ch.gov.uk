package uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements;

import jakarta.validation.constraints.NotNull;

public class OffBalanceSheetArrangementsQuestion {

    @NotNull(message = "{offBalanceSheetArrangementsQuestion.selectionNotMade}")
    private Boolean hasIncludedOffBalanceSheetArrangements;

    public Boolean getHasIncludedOffBalanceSheetArrangements() {
        return hasIncludedOffBalanceSheetArrangements;
    }

    public void setHasIncludedOffBalanceSheetArrangements(
            Boolean hasIncludedOffBalanceSheetArrangements) {
        this.hasIncludedOffBalanceSheetArrangements = hasIncludedOffBalanceSheetArrangements;
    }
}
