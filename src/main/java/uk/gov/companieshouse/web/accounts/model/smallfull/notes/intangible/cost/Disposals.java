package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.cost;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class Disposals {

    @ValidationMapping("$.intangible_assets.goodwill.cost.disposals")
    private Long goodwill;

    @ValidationMapping("$.intangible_assets.other_intangible_assets.cost.disposals")
    private Long otherIntangibleAssets;

    @ValidationMapping("$.intangible_assets.total.cost.disposals")
    private Long total;


    public Long getGoodwill() {
        return goodwill;
    }

    public void setGoodwill(Long goodwill) {
        this.goodwill = goodwill;
    }

    public Long getOtherIntangibleAssets() {
        return otherIntangibleAssets;
    }

    public void setOtherIntangibleAssets(Long otherIntangibleAssets) {
        this.otherIntangibleAssets = otherIntangibleAssets;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }




}
