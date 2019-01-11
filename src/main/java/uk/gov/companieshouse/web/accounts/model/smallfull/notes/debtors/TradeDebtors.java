package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class TradeDebtors {

    @ValidationMapping("$.debtors.current_period.trade_debtors")
    private Long currentTradeDebtors;

    @ValidationMapping("$.debtors.previous_period.trade_debtors")
    private Long previousTradeDebtors;

}
