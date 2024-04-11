package uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear;

import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.items.Tax;
import uk.gov.companieshouse.web.accounts.model.profitandloss.profitorlossforfinancialyear.items.TotalProfitOrLossForFinancialYear;

public class ProfitOrLossForFinancialYear {
    private Tax tax;

    private TotalProfitOrLossForFinancialYear totalProfitOrLossForFinancialYear;

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public TotalProfitOrLossForFinancialYear getTotalProfitOrLossForFinancialYear() {
        return totalProfitOrLossForFinancialYear;
    }

    public void setTotalProfitOrLossForFinancialYear(TotalProfitOrLossForFinancialYear totalProfitOrLossForFinancialYear) {
        this.totalProfitOrLossForFinancialYear = totalProfitOrLossForFinancialYear;
    }
}
