package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation;

public class TangibleAssetsDepreciation {
    private DepreciationAtPeriodStart atPeriodStart;

    private ChargeForYear chargeForYear;

    private OnDisposals onDisposals;

    private OtherAdjustments otherAdjustments;

    private DepreciationAtPeriodEnd atPeriodEnd;

    public DepreciationAtPeriodStart getAtPeriodStart() {
        return atPeriodStart;
    }

    public void setAtPeriodStart(
        DepreciationAtPeriodStart atPeriodStart) {
        this.atPeriodStart = atPeriodStart;
    }

    public ChargeForYear getChargeForYear() {
        return chargeForYear;
    }

    public void setChargeForYear(
        ChargeForYear chargeForYear) {
        this.chargeForYear = chargeForYear;
    }

    public OnDisposals getOnDisposals() {
        return onDisposals;
    }

    public void setOnDisposals(
        OnDisposals onDisposals) {
        this.onDisposals = onDisposals;
    }

    public OtherAdjustments getOtherAdjustments() {
        return otherAdjustments;
    }

    public void setOtherAdjustments(
        OtherAdjustments otherAdjustments) {
        this.otherAdjustments = otherAdjustments;
    }

    public DepreciationAtPeriodEnd getAtPeriodEnd() {
        return atPeriodEnd;
    }

    public void setAtPeriodEnd(
        DepreciationAtPeriodEnd atPeriodEnd) {
        this.atPeriodEnd = atPeriodEnd;
    }
}
