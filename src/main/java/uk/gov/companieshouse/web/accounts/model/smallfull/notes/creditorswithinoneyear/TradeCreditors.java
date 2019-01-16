package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class TradeCreditors {

    @ValidationMapping("$.creditors_within_one_year.current_period.trade_creditors")
    private Long currentTradeCreditors;

    @ValidationMapping("$.creditors_within_one_year.previous_period.trade_creditors")
    private Long previousTradeCreditors;

}
