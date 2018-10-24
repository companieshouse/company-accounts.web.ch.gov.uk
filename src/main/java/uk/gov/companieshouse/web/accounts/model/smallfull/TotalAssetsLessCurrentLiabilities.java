package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class TotalAssetsLessCurrentLiabilities {

    @ValidationMapping("$.current_period.balance_sheet.other_liabilities_or_assets.total_assets_less_current_liabilities")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.other_liabilities_or_assets.total_assets_less_current_liabilities")
    private Long previousAmount;
}
