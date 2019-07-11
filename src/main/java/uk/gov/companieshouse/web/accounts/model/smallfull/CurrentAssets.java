package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class CurrentAssets {

    private Stocks stocks;
    private Debtors debtors;
    private CashAtBankAndInHand cashAtBankAndInHand;
    private CurrentAssetsInvestments investments;

    @ValidationMapping("$.current_period.balance_sheet.current_assets.total")
    private Long currentTotal;

    @ValidationMapping("$.previous_period.balance_sheet.current_assets.total")
    private Long previousTotal;

    public Stocks getStocks() {
        return stocks;
    }

    public void setStocks(Stocks stocks) {
        this.stocks = stocks;
    }

    public Debtors getDebtors() {
        return debtors;
    }

    public void setDebtors(Debtors debtors) {
        this.debtors = debtors;
    }

    public CashAtBankAndInHand getCashAtBankAndInHand() {
        return cashAtBankAndInHand;
    }

    public void setCashAtBankAndInHand(
        CashAtBankAndInHand cashAtBankAndInHand) {
        this.cashAtBankAndInHand = cashAtBankAndInHand;
    }

    public CurrentAssetsInvestments getInvestments() {
        return investments;
    }

    public void setInvestments(
        CurrentAssetsInvestments investments) {
        this.investments = investments;
    }

    public Long getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(Long currentTotal) {
        this.currentTotal = currentTotal;
    }

    public Long getPreviousTotal() {
        return previousTotal;
    }

    public void setPreviousTotal(Long previousTotal) {
        this.previousTotal = previousTotal;
    }
}
