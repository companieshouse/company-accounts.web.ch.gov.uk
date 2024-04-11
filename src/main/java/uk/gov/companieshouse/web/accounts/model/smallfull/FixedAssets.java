package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class FixedAssets {
    private TangibleAssets tangibleAssets;
    private IntangibleAssets intangibleAssets;
    private FixedInvestments investments;

    @ValidationMapping("$.previous_period.balance_sheet.fixed_assets.total")
    private Long previousTotal;

    @ValidationMapping("$.current_period.balance_sheet.fixed_assets.total")
    private Long currentTotal;

    public TangibleAssets getTangibleAssets() {
        return tangibleAssets;
    }

    public void setTangibleAssets(
        TangibleAssets tangibleAssets) {
        this.tangibleAssets = tangibleAssets;
    }

    public IntangibleAssets getIntangibleAssets() {
        return intangibleAssets;
    }

    public void setIntangibleAssets(IntangibleAssets intangibleAssets) {
        this.intangibleAssets = intangibleAssets;
    }

    public FixedInvestments getInvestments() {
        return investments;
    }

    public void setInvestments(
        FixedInvestments investments) {
        this.investments = investments;
    }

    public Long getPreviousTotal() {
        return previousTotal;
    }

    public void setPreviousTotal(Long previousTotal) {
        this.previousTotal = previousTotal;
    }

    public Long getCurrentTotal() {
        return currentTotal;
    }

    public void setCurrentTotal(Long currentTotal) {
        this.currentTotal = currentTotal;
    }
}
