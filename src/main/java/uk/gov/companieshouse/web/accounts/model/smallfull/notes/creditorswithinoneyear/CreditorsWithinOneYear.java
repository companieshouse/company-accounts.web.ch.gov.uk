package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear;

import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class CreditorsWithinOneYear {
	
    private BalanceSheetHeadings balanceSheetHeadings;

    @ValidationMapping("$.creditors_within_one_year.current_period.details")
    private String details;

    private AccrualsAndDeferredIncome accrualsAndDeferredIncome;

    private BankLoansAndOverdrafts bankLoansAndOverdrafts;

    private FinanceLeasesAndHirePurchaseContracts financeLeasesAndHirePurchaseContracts;
    
    private OtherCreditors otherCreditors;
    
    private TaxationAndSocialSecurity taxationAndSocialSecurity;
    
    private Total total;

    private TradeCreditors tradeCreditors;

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

    public AccrualsAndDeferredIncome getAccrualsAndDeferredIncome() {
        return accrualsAndDeferredIncome;
    }

    public void setAccrualsAndDeferredIncome(
        AccrualsAndDeferredIncome accrualsAndDeferredIncome) {
        this.accrualsAndDeferredIncome = accrualsAndDeferredIncome;
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

    public TaxationAndSocialSecurity getTaxationAndSocialSecurity() {
        return taxationAndSocialSecurity;
    }

    public void setTaxationAndSocialSecurity(
        TaxationAndSocialSecurity taxationAndSocialSecurity) {
        this.taxationAndSocialSecurity = taxationAndSocialSecurity;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(
        Total total) {
        this.total = total;
    }

    public TradeCreditors getTradeCreditors() {
        return tradeCreditors;
    }

    public void setTradeCreditors(
        TradeCreditors tradeCreditors) {
        this.tradeCreditors = tradeCreditors;
    }
}
