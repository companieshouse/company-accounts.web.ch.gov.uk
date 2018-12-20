package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class CreditorsDueWithinOneYear {

    @ValidationMapping("$.current_period.balance_sheet.other_liabilities_or_assets.creditors_due_within_one_year")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.other_liabilities_or_assets.creditors_due_within_one_year")
    private Long previousAmount;
}
