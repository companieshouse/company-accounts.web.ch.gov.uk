package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost;

public class IntangibleAssetsCost {
    private CostAtPeriodStart atPeriodStart;

    private Additions additions;

    private Disposals disposals;

    private Revaluations revaluations;

    private Transfers transfers;

    private CostAtPeriodEnd atPeriodEnd;

    public CostAtPeriodStart getAtPeriodStart() {
        return atPeriodStart;
    }

    public void setAtPeriodStart(
            CostAtPeriodStart atPeriodStart) {
        this.atPeriodStart = atPeriodStart;
    }

    public Additions getAdditions() {
        return additions;
    }

    public void setAdditions(
            Additions additions) {
        this.additions = additions;
    }

    public Disposals getDisposals() {
        return disposals;
    }

    public void setDisposals(
            Disposals disposals) {
        this.disposals = disposals;
    }

    public Revaluations getRevaluations() {
        return revaluations;
    }

    public void setRevaluations(
            Revaluations revaluations) {
        this.revaluations = revaluations;
    }

    public Transfers getTransfers() {
        return transfers;
    }

    public void setTransfers(
            Transfers transfers) {
        this.transfers = transfers;
    }

    public CostAtPeriodEnd getAtPeriodEnd() {
        return atPeriodEnd;
    }

    public void setAtPeriodEnd(
            CostAtPeriodEnd atPeriodEnd) {
        this.atPeriodEnd = atPeriodEnd;
    }
}
