package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible;


import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost.IntangibleAssetsCost;

public class IntangibleAssets {

    private IntangibleAssetsCost cost;

    public IntangibleAssetsCost getCost() {
        return cost;
    }

    public void setCost(IntangibleAssetsCost cost) {
        this.cost = cost;
    }

}
