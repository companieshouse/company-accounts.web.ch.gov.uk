package uk.gov.companieshouse.web.accounts.model.payment;

import javax.validation.constraints.NotNull;

public class MakePayment {

    @NotNull(message = "{payment.makePaymentChoice}")
    private Boolean makePaymentChoice;

    public Boolean getMakePaymentChoice() {
        return makePaymentChoice;
    }

    public void setMakePaymentChoice(Boolean makePaymentChoice) {
        this.makePaymentChoice = makePaymentChoice;
    }
}
