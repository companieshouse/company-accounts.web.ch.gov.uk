package uk.gov.companieshouse.web.accounts.model.profitandloss;

import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.GrossProfitOrLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.OperatingProfitOrLoss;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossbeforetax.ProfitOrLossBeforeTax;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.ProfitOrLossForFinancialYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class ProfitAndLoss {

    private BalanceSheetHeadings balanceSheetHeadings;

    private GrossProfitOrLoss grossProfitOrLoss;

    private OperatingProfitOrLoss operatingProfitOrLoss;

    private ProfitOrLossBeforeTax profitOrLossBeforeTax;

    private ProfitOrLossForFinancialYear profitOrLossForFinancialYear;

    public BalanceSheetHeadings getBalanceSheetHeadings() {
        return balanceSheetHeadings;
    }

    public void setBalanceSheetHeadings(BalanceSheetHeadings balanceSheetHeadings) {
        this.balanceSheetHeadings = balanceSheetHeadings;
    }

    public GrossProfitOrLoss getGrossProfitOrLoss() {
        return grossProfitOrLoss;
    }

    public void setGrossProfitOrLoss(GrossProfitOrLoss grossProfitOrLoss) {
        this.grossProfitOrLoss = grossProfitOrLoss;
    }

    public OperatingProfitOrLoss getOperatingProfitOrLoss() {
        return operatingProfitOrLoss;
    }

    public void setOperatingProfitOrLoss(OperatingProfitOrLoss operatingProfitOrLoss) {
        this.operatingProfitOrLoss = operatingProfitOrLoss;
    }

    public ProfitOrLossBeforeTax getProfitOrLossBeforeTax() {
        return profitOrLossBeforeTax;
    }

    public void setProfitOrLossBeforeTax(ProfitOrLossBeforeTax profitOrLossBeforeTax) {
        this.profitOrLossBeforeTax = profitOrLossBeforeTax;
    }

    public ProfitOrLossForFinancialYear getProfitOrLossForFinancialYear() {
        return profitOrLossForFinancialYear;
    }

    public void setProfitOrLossForFinancialYear(ProfitOrLossForFinancialYear profitOrLossForFinancialYear) {
        this.profitOrLossForFinancialYear = profitOrLossForFinancialYear;
    }
}
