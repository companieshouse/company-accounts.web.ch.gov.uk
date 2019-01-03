package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentPeriod {

    private String details;

    private Long greaterThanOneYear;

    private Long otherDebtors;

    private Long prepaymentsAndAccruedIncome;

    private Long total;

    private Long tradeDebtors;

}
