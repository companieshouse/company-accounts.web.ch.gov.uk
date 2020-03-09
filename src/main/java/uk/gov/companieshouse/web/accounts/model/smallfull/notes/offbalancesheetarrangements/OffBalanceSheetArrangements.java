package uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements;

import javax.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class OffBalanceSheetArrangements {

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
