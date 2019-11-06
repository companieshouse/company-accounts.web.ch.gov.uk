package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.debtors.Debtors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.stocks.StocksNote;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
public class Review {

    BalanceSheet balanceSheet;

    Statements statements;

    BasisOfPreparation basisOfPreparation;

    TurnoverPolicy turnoverPolicy;

    TangibleDepreciationPolicy tangibleDepreciationPolicy;

    IntangibleAmortisationPolicy intangibleAmortisationPolicy;

    ValuationInformationPolicy valuationInformationPolicy;

    OtherAccountingPolicy otherAccountingPolicy;
    
    CreditorsWithinOneYear creditorsWithinOneYear;

    CreditorsAfterOneYear creditorsAfterOneYear;

    Debtors debtors;

    Employees employees;

    TangibleAssets tangibleAssets;

    IntangibleAssets intangibleAssets;

    StocksNote stocks;

    FixedAssetsInvestments fixedAssetsInvestments;

    CurrentAssetsInvestments currentAssetsInvestments;

    public BalanceSheet getBalanceSheet() {
        return balanceSheet;
    }

    public IntangibleAssets getIntangibleAssets() {
        return intangibleAssets;
    }

    public void setIntangibleAssets(IntangibleAssets intangibleAssets) {
        this.intangibleAssets = intangibleAssets;
    }

    public void setBalanceSheet(BalanceSheet balanceSheet) {
        this.balanceSheet = balanceSheet;
    }

    public Statements getStatements() {
        return statements;
    }

    public void setStatements(Statements statements) {
        this.statements = statements;
    }

    public BasisOfPreparation getBasisOfPreparation() {
        return basisOfPreparation;
    }

    public void setBasisOfPreparation(
        BasisOfPreparation basisOfPreparation) {
        this.basisOfPreparation = basisOfPreparation;
    }

    public TurnoverPolicy getTurnoverPolicy() {
        return turnoverPolicy;
    }

    public void setTurnoverPolicy(
        TurnoverPolicy turnoverPolicy) {
        this.turnoverPolicy = turnoverPolicy;
    }

    public TangibleDepreciationPolicy getTangibleDepreciationPolicy() {
        return tangibleDepreciationPolicy;
    }

    public void setTangibleDepreciationPolicy(
        TangibleDepreciationPolicy tangibleDepreciationPolicy) {
        this.tangibleDepreciationPolicy = tangibleDepreciationPolicy;
    }

    public IntangibleAmortisationPolicy getIntangibleAmortisationPolicy() {
        return intangibleAmortisationPolicy;
    }

    public void setIntangibleAmortisationPolicy(
        IntangibleAmortisationPolicy intangibleAmortisationPolicy) {
        this.intangibleAmortisationPolicy = intangibleAmortisationPolicy;
    }

    public ValuationInformationPolicy getValuationInformationPolicy() {
        return valuationInformationPolicy;
    }

    public void setValuationInformationPolicy(
        ValuationInformationPolicy valuationInformationPolicy) {
        this.valuationInformationPolicy = valuationInformationPolicy;
    }

    public OtherAccountingPolicy getOtherAccountingPolicy() {
        return otherAccountingPolicy;
    }

    public void setOtherAccountingPolicy(
        OtherAccountingPolicy otherAccountingPolicy) {
        this.otherAccountingPolicy = otherAccountingPolicy;
    }

    public CreditorsWithinOneYear getCreditorsWithinOneYear() {
        return creditorsWithinOneYear;
    }

    public void setCreditorsWithinOneYear(
        CreditorsWithinOneYear creditorsWithinOneYear) {
        this.creditorsWithinOneYear = creditorsWithinOneYear;
    }

    public CreditorsAfterOneYear getCreditorsAfterOneYear() {
        return creditorsAfterOneYear;
    }

    public void setCreditorsAfterOneYear(
        CreditorsAfterOneYear creditorsAfterOneYear) {
        this.creditorsAfterOneYear = creditorsAfterOneYear;
    }

    public Debtors getDebtors() {
        return debtors;
    }

    public void setDebtors(Debtors debtors) {
        this.debtors = debtors;
    }

    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(
        Employees employees) {
        this.employees = employees;
    }

    public TangibleAssets getTangibleAssets() {
        return tangibleAssets;
    }

    public void setTangibleAssets(
        TangibleAssets tangibleAssets) {
        this.tangibleAssets = tangibleAssets;
    }

    public StocksNote getStocks() {
        return stocks;
    }

    public void setStocks(StocksNote stocks) {
        this.stocks = stocks;
    }

    public FixedAssetsInvestments getFixedAssetsInvestments() {
        return fixedAssetsInvestments;
    }

    public void setFixedAssetsInvestments(
        FixedAssetsInvestments fixedAssetsInvestments) {
        this.fixedAssetsInvestments = fixedAssetsInvestments;
    }

    public CurrentAssetsInvestments getCurrentAssetsInvestments() {
        return currentAssetsInvestments;
    }

    public void setCurrentAssetsInvestments(
        CurrentAssetsInvestments currentAssetsInvestments) {
        this.currentAssetsInvestments = currentAssetsInvestments;
    }
}
