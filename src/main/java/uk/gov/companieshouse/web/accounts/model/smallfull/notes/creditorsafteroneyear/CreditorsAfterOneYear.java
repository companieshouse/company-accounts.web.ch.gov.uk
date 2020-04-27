package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class CreditorsAfterOneYear {

    private BalanceSheetHeadings balanceSheetHeadings;

    @ValidationMapping("$.creditors_after_more_than_one_year.current_period.details")
    private String details;

    private BankLoansAndOverdrafts bankLoansAndOverdrafts;

    private FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts;

    private OtherCreditors otherCreditors;

    private Total total;

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

    public BankLoansAndOverdrafts getBankLoansAndOverdrafts() {
        return bankLoansAndOverdrafts;
    }

    public void setBankLoansAndOverdrafts(
        BankLoansAndOverdrafts bankLoansAndOverdrafts) {
        this.bankLoansAndOverdrafts = bankLoansAndOverdrafts;
    }

    public FinanceLeasesAndHirePurchaseContracts getFinanceLeasesAndHirePurchaseContracts() {
        return financeLeasesAndHirePurchaseContracts;
    }

    public void setFinanceLeasesAndHirePurchaseContracts(
        FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts) {
        this.financeLeasesAndHirePurchaseContracts = financeLeasesAndHirePurchaseContracts;
    }

    public OtherCreditors getOtherCreditors() {
        return otherCreditors;
    }

    public void setOtherCreditors(
        OtherCreditors otherCreditors) {
        this.otherCreditors = otherCreditors;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(
        Total total) {
        this.total = total;
    }
}
