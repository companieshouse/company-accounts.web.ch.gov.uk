package uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.netbookvalue;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class CurrentPeriod {

    @ValidationMapping("$.intangible_assets.goodwill.net_book_value_at_end_of_current_period")
    private Long goodwill;

    @ValidationMapping("$.intangible_assets.other_intangible_assets.net_book_value_at_end_of_current_period")
    private Long otherIntangibleAssets;

    @ValidationMapping("$.intangible_assets.total.net_book_value_at_end_of_current_period")
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
