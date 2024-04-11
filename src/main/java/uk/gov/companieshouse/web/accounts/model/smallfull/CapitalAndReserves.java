package uk.gov.companieshouse.web.accounts.model.smallfull;

public class CapitalAndReserves {
    private CalledUpShareCapital calledUpShareCapital;

    private SharePremiumAccount sharePremiumAccount;

    private OtherReserves otherReserves;

    private ProfitAndLossAccount profitAndLossAccount;

    private TotalShareholdersFunds totalShareholdersFunds;

    public CalledUpShareCapital getCalledUpShareCapital() {
        return calledUpShareCapital;
    }

    public void setCalledUpShareCapital(
        CalledUpShareCapital calledUpShareCapital) {
        this.calledUpShareCapital = calledUpShareCapital;
    }

    public SharePremiumAccount getSharePremiumAccount() {
        return sharePremiumAccount;
    }

    public void setSharePremiumAccount(
        SharePremiumAccount sharePremiumAccount) {
        this.sharePremiumAccount = sharePremiumAccount;
    }

    public OtherReserves getOtherReserves() {
        return otherReserves;
    }

    public void setOtherReserves(
        OtherReserves otherReserves) {
        this.otherReserves = otherReserves;
    }

    public ProfitAndLossAccount getProfitAndLossAccount() {
        return profitAndLossAccount;
    }

    public void setProfitAndLossAccount(
        ProfitAndLossAccount profitAndLossAccount) {
        this.profitAndLossAccount = profitAndLossAccount;
    }

    public TotalShareholdersFunds getTotalShareholdersFunds() {
        return totalShareholdersFunds;
    }

    public void setTotalShareholdersFunds(
        TotalShareholdersFunds totalShareholdersFunds) {
        this.totalShareholdersFunds = totalShareholdersFunds;
    }
}
