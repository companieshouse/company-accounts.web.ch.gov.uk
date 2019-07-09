package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class PaymentsOnAccount {

    @ValidationMapping("$.stocks.current_period.payments_on_account")
    private Long currentPaymentsOnAccount;

    @ValidationMapping("$.stocks.previous_period.payments_on_account")
    private Long previousPaymentsOnAccount;

    public Long getCurrentPaymentsOnAccount() {
        return currentPaymentsOnAccount;
    }

    public void setCurrentPaymentsOnAccount(Long currentPaymentsOnAccount) {
        this.currentPaymentsOnAccount = currentPaymentsOnAccount;
    }

    public Long getPreviousPaymentsOnAccount() {
        return previousPaymentsOnAccount;
    }

    public void setPreviousPaymentsOnAccount(Long previousPaymentsOnAccount) {
        this.previousPaymentsOnAccount = previousPaymentsOnAccount;
    }
}
