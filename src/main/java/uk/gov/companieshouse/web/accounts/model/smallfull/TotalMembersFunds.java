package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

@Getter
@Setter
public class TotalMembersFunds {

    @ValidationParentMapping("$.current_period.balance_sheet.members_funds")
    @ValidationMapping("$.current_period.balance_sheet.members_funds.total_members_funds")
    private Long currentAmount;

    @ValidationParentMapping("$.previous_period.balance_sheet.members_funds")
    @ValidationMapping("$.previous_period.balance_sheet.members_funds.total_members_funds")
    private Long previousAmount;
}
