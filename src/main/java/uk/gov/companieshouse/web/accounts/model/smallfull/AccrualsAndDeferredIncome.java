package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class AccrualsAndDeferredIncome {

    @ValidationMapping("$.current_period.balance_sheet.other_liabilities_or_assets.accruals_and_deferred_income")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.other_liabilities_or_assets.accruals_and_deferred_income")
    private Long previousAmount;
}
