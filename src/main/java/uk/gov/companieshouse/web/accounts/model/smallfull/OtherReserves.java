package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class OtherReserves {

    @ValidationMapping("$.current_period.balance_sheet.capital_and_reserves.other_reserves")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.capital_and_reserves.other_reserves")
    private Long previousAmount;
}
