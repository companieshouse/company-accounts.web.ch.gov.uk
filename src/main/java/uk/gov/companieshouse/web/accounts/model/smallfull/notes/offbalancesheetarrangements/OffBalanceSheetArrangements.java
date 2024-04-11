package uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements;

import jakarta.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class OffBalanceSheetArrangements implements Note {
    @NotBlank(message = "{offBalanceSheetArrangements.details.missing}")
    @ValidationMapping("$.off_balance_sheet_arrangements.details")
    private String offBalanceSheetArrangementsDetails;

    public String getOffBalanceSheetArrangementsDetails() {
        return offBalanceSheetArrangementsDetails;
    }

    public void setOffBalanceSheetArrangementsDetails(String offBalanceSheetArrangementsDetails) {
        this.offBalanceSheetArrangementsDetails = offBalanceSheetArrangementsDetails;
    }
}
