package uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss;

import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.AdministrativeExpenses;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.DistributionCosts;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OperatingTotal;
import uk.gov.companieshouse.web.accounts.model.profitandloss.operatingprofitorloss.items.OtherOperatingIncome;

public class OperatingProfitOrLoss {
    private AdministrativeExpenses administrativeExpenses;

    private DistributionCosts distributionCosts;

    private OperatingTotal operatingTotal;

    private OtherOperatingIncome otherOperatingIncome;

    public AdministrativeExpenses getAdministrativeExpenses() {
        return administrativeExpenses;
    }

    public void setAdministrativeExpenses(AdministrativeExpenses administrativeExpenses) {
        this.administrativeExpenses = administrativeExpenses;
    }

    public DistributionCosts getDistributionCosts() {
        return distributionCosts;
    }

    public void setDistributionCosts(DistributionCosts distributionCosts) {
        this.distributionCosts = distributionCosts;
    }

    public OperatingTotal getOperatingTotal() {
        return operatingTotal;
    }

    public void setOperatingTotal(OperatingTotal operatingTotal) {
        this.operatingTotal = operatingTotal;
    }

    public OtherOperatingIncome getOtherOperatingIncome() {
        return otherOperatingIncome;
    }

    public void setOtherOperatingIncome(OtherOperatingIncome otherOperatingIncome) {
        this.otherOperatingIncome = otherOperatingIncome;
    }
}
