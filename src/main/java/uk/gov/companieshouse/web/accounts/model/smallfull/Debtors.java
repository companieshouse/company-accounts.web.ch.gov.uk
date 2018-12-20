package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class Debtors {

    @ValidationMapping("$.current_period.balance_sheet.current_assets.debtors")
    private Long currentAmount;

    @ValidationMapping("$.previous_period.balance_sheet.current_assets.debtors")
    private Long previousAmount;
}

