package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class CalledUpShareCapitalNotPaid {

    @ValidationMapping("$.current_period.balance_sheet.called_up_share_capital_not_paid")
    private Integer currentAmount;
}
