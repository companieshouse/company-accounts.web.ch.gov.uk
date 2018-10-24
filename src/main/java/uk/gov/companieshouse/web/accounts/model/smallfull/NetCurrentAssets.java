package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class NetCurrentAssets {

    @ValidationMapping("$.current_period.balance_sheet.other_liabilities_or_assets.net_current_assets")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.other_liabilities_or_assets.net_current_assets")
    private Long previousAmount;
}
