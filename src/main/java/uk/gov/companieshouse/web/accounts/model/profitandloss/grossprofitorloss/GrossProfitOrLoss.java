package uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss;

import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.CostOfSales;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.GrossTotal;
import uk.gov.companieshouse.web.accounts.model.profitandloss.grossprofitorloss.items.Turnover;

public class GrossProfitOrLoss {

    private CostOfSales costOfSales;

    private GrossTotal grossTotal;

    private Turnover turnover;

    public CostOfSales getCostOfSales() {
        return costOfSales;
    }

    public void setCostOfSales(CostOfSales costOfSales) {
        this.costOfSales = costOfSales;
    }

    public GrossTotal getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(GrossTotal grossTotal) {
        this.grossTotal = grossTotal;
    }

    public Turnover getTurnover() {
        return turnover;
    }

    public void setTurnover(Turnover turnover) {
        this.turnover = turnover;
    }
}
