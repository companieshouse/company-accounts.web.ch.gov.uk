package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class CurrentAssetsInvestments {

    @ValidationMapping("$.current_period.balance_sheet.current_assets.investments")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.current_assets.investments")
    private Long previousAmount;
}
