package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

@Getter
@Setter
public class TotalShareholdersFunds {

    @ValidationParentMapping("$.current_period.balance_sheet.capital_and_reserves")
    @ValidationMapping("$.current_period.balance_sheet.capital_and_reserves.total_shareholders_funds")
    private Long currentAmount;

    @ValidationParentMapping("$.previous_period.balance_sheet.capital_and_reserves")
    @ValidationMapping("$.previous_period.balance_sheet.capital_and_reserves.total_shareholders_funds")
    private Long previousAmount;
}
