package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class TotalMembersFunds {

    @ValidationMapping("$.current_period.balance_sheet.members_funds.total_members_funds")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.members_funds.total_members_funds")
    private Long previousAmount;
}
