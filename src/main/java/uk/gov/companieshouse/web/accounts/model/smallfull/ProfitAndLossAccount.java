package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class ProfitAndLossAccount {

    @ValidationMapping("$.current_period.balance_sheet.capital_and_reserves.profit_and_loss")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.capital_and_reserves.profit_and_loss")
    private Long previousAmount;
}
