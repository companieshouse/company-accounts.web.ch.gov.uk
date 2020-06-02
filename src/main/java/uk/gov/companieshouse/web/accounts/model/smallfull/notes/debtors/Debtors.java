package uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors;

import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class Debtors implements Note {

    private BalanceSheetHeadings balanceSheetHeadings;

    @ValidationMapping("$.debtors.current_period.details")
    private String details;

    private GreaterThanOneYear greaterThanOneYear;

    private OtherDebtors otherDebtors;

    private PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

    private Total total;

    private TradeDebtors tradeDebtors;

    public BalanceSheetHeadings getBalanceSheetHeadings() {
        return balanceSheetHeadings;
    }

    public void setBalanceSheetHeadings(
        BalanceSheetHeadings balanceSheetHeadings) {
        this.balanceSheetHeadings = balanceSheetHeadings;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public GreaterThanOneYear getGreaterThanOneYear() {
        return greaterThanOneYear;
    }

    public void setGreaterThanOneYear(
        GreaterThanOneYear greaterThanOneYear) {
        this.greaterThanOneYear = greaterThanOneYear;
    }

    public OtherDebtors getOtherDebtors() {
        return otherDebtors;
    }

    public void setOtherDebtors(
        OtherDebtors otherDebtors) {
        this.otherDebtors = otherDebtors;
    }

    public PrepaymentsAndAccruedIncome getPrepaymentsAndAccruedIncome() {
        return prepaymentsAndAccruedIncome;
    }

    public void setPrepaymentsAndAccruedIncome(
        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome) {
        this.prepaymentsAndAccruedIncome = prepaymentsAndAccruedIncome;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public TradeDebtors getTradeDebtors() {
        return tradeDebtors;
    }

    public void setTradeDebtors(
        TradeDebtors tradeDebtors) {
        this.tradeDebtors = tradeDebtors;
    }
}
