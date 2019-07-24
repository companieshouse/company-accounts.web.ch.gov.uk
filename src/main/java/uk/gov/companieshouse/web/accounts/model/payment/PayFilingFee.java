package uk.gov.companieshouse.web.accounts.model.payment;

import javax.validation.constraints.NotNull;

public class PayFilingFee {

    @NotNull(message = "{payment.payFilingFeeChoice}")
    private Boolean payFilingFeeChoice;

    public Boolean getPayFilingFeeChoice() {
        return payFilingFeeChoice;
    }

    public void setPayFilingFeeChoice(Boolean payFilingFeeChoice) {
        this.payFilingFeeChoice = payFilingFeeChoice;
    }
}
