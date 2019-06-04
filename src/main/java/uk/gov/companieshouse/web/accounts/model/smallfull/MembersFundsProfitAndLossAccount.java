package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class MembersFundsProfitAndLossAccount {

    @ValidationMapping("$.current_period.balance_sheet.members_funds.profit_and_loss_account")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.members_funds.profit_and_loss_account")
    private Long previousAmount;
}
