package uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements;

import javax.validation.constraints.NotBlank;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class OffBalanceSheetArrangements {

    @NotBlank(message = "{offBalanceSheetArrangements.details.missing}")
    @ValidationMapping("$.off_balance_sheet_arrangements.details")
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
