package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.amortisation;

public class IntangibleAssetsAmortisation {
    AmortisationAtPeriodStart atPeriodStart;

    ChargeForYear chargeForYear;

    OnDisposals onDisposals;

    OtherAdjustments otherAdjustments;

    AmortisationAtPeriodEnd atPeriodEnd;

    public AmortisationAtPeriodStart getAtPeriodStart() {
        return atPeriodStart;
    }

    public void setAtPeriodStart(
        AmortisationAtPeriodStart atPeriodStart) {
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

    public AmortisationAtPeriodEnd getAtPeriodEnd() {
        return atPeriodEnd;
    }

    public void setAtPeriodEnd(
        AmortisationAtPeriodEnd atPeriodEnd) {
        this.atPeriodEnd = atPeriodEnd;
    }
}
