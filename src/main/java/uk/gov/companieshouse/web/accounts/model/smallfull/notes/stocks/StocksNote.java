package uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks;

import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class StocksNote {

    private BalanceSheetHeadings balanceSheetHeadings;

    private PaymentsOnAccount paymentsOnAccount;

    private Stocks stocks;

    private Total total;

    public BalanceSheetHeadings getBalanceSheetHeadings() {
        return balanceSheetHeadings;
    }

    public void setBalanceSheetHeadings(
        BalanceSheetHeadings balanceSheetHeadings) {
        this.balanceSheetHeadings = balanceSheetHeadings;
    }

    public PaymentsOnAccount getPaymentsOnAccount() {
        return paymentsOnAccount;
    }

    public void setPaymentsOnAccount(
        PaymentsOnAccount paymentsOnAccount) {
        this.paymentsOnAccount = paymentsOnAccount;
    }

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }
}
