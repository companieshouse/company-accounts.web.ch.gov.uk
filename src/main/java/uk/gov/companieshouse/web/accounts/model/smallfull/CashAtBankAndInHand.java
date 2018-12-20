package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class CashAtBankAndInHand {

    @ValidationMapping("$.current_period.balance_sheet.current_assets.cash_at_bank_and_in_hand")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.current_assets.cash_at_bank_and_in_hand")
    private Long previousAmount;

}
