package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

@Getter
@Setter
public class Debtors {

    private BalanceSheetHeadings balanceSheetHeadings;

    private String details;

    private GreaterThanOneYear greaterThanOneYear;

    private OtherDebtors otherDebtors;

    private PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

    private Total total;

    private TradeDebtors tradeDebtors;
}
