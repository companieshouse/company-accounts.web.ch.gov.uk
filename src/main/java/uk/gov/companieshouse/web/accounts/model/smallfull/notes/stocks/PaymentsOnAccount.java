package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class PaymentsOnAccount {

    @ValidationMapping("$.stocks.current_period.payments_on_account")
    private Long currentPaymentsOnAccount;

    @ValidationMapping("$.stocks.previous_period.payments_on_account")
    private Long previousPaymentsOnAccount;
}
