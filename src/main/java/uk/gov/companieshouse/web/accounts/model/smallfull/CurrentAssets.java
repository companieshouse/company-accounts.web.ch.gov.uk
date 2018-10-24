package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class CurrentAssets {

    private Stocks stocks;
    private Debtors debtors;
    private CashAtBankAndInHand cashAtBankAndInHand;

    @ValidationMapping("$.current_period.balance_sheet.current_assets.total_current_assets")
    private Long currentCurrentAssetsTotal;

    @ValidationMapping("$.previous_period.balance_sheet.current_assets.total_current_assets")
    private Long previousCurrentAssetsTotal;
}
