package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class Debtors {

    @ValidationMapping("$.debtors.current_period.details")
    private String details;

    private GreaterThanOneYear greaterThanOneYear;

    private OtherDebtors otherDebtors;

    private PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

    private Total total;

    private TradeDebtors tradeDebtors;
}
