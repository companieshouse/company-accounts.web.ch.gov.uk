package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

@Getter
@Setter
public class StocksNote {

    private BalanceSheetHeadings balanceSheetHeadings;

    private PaymentsOnAccount paymentsOnAccount;

    private Stocks stocks;

    private Total total;
}
