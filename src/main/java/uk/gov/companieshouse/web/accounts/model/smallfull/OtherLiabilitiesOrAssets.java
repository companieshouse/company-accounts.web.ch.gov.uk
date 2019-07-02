package uk.gov.companieshouse.web.accounts.model.smallfull;

public class OtherLiabilitiesOrAssets {

    private PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

    private CreditorsDueWithinOneYear creditorsDueWithinOneYear;

    private CreditorsAfterOneYear creditorsAfterOneYear;

    private AccrualsAndDeferredIncome accrualsAndDeferredIncome;

    private NetCurrentAssets netCurrentAssets;

    private ProvisionForLiabilities provisionForLiabilities;

    private TotalNetAssets totalNetAssets;

    private TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities;

    public PrepaymentsAndAccruedIncome getPrepaymentsAndAccruedIncome() {
        return prepaymentsAndAccruedIncome;
    }

    public void setPrepaymentsAndAccruedIncome(
        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome) {
        this.prepaymentsAndAccruedIncome = prepaymentsAndAccruedIncome;
    }

    public CreditorsDueWithinOneYear getCreditorsDueWithinOneYear() {
        return creditorsDueWithinOneYear;
    }

    public void setCreditorsDueWithinOneYear(
        CreditorsDueWithinOneYear creditorsDueWithinOneYear) {
        this.creditorsDueWithinOneYear = creditorsDueWithinOneYear;
    }

    public CreditorsAfterOneYear getCreditorsAfterOneYear() {
        return creditorsAfterOneYear;
    }

    public void setCreditorsAfterOneYear(
        CreditorsAfterOneYear creditorsAfterOneYear) {
        this.creditorsAfterOneYear = creditorsAfterOneYear;
    }

    public AccrualsAndDeferredIncome getAccrualsAndDeferredIncome() {
        return accrualsAndDeferredIncome;
    }

    public void setAccrualsAndDeferredIncome(
        AccrualsAndDeferredIncome accrualsAndDeferredIncome) {
        this.accrualsAndDeferredIncome = accrualsAndDeferredIncome;
    }

    public NetCurrentAssets getNetCurrentAssets() {
        return netCurrentAssets;
    }

    public void setNetCurrentAssets(
        NetCurrentAssets netCurrentAssets) {
        this.netCurrentAssets = netCurrentAssets;
    }

    public ProvisionForLiabilities getProvisionForLiabilities() {
        return provisionForLiabilities;
    }

    public void setProvisionForLiabilities(
        ProvisionForLiabilities provisionForLiabilities) {
        this.provisionForLiabilities = provisionForLiabilities;
    }

    public TotalNetAssets getTotalNetAssets() {
        return totalNetAssets;
    }

    public void setTotalNetAssets(
        TotalNetAssets totalNetAssets) {
        this.totalNetAssets = totalNetAssets;
    }

    public TotalAssetsLessCurrentLiabilities getTotalAssetsLessCurrentLiabilities() {
        return totalAssetsLessCurrentLiabilities;
    }

    public void setTotalAssetsLessCurrentLiabilities(
        TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities) {
        this.totalAssetsLessCurrentLiabilities = totalAssetsLessCurrentLiabilities;
    }
}
