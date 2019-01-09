package uk.gov.companieshouse.web.accounts.model.smallfull.notes;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

@Getter
@Setter
public class Debtors {

    private BalanceSheetHeadings balanceSheetHeadings;

    private String details;

    private Long currentGreaterThanOneYear;

    private Long currentOtherDebtors;

    private Long currentPrepaymentsAndAccruedIncome;

    private Long currentTotal;

    private Long currentTradeDebtors;

    private Long previousGreaterThanOneYear;

    private Long previousOtherDebtors;

    private Long previousPrepaymentsAndAccruedIncome;

    private Long previousTotal;

    private Long previousTradeDebtors;
}
