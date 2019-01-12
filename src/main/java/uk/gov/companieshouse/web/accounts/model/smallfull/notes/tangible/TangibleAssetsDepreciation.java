package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsDepreciation {

    @JsonProperty("at_period_start")
    private TangibleAssetsColumns atPeriodStart;

    @JsonProperty("charge_for_year")
    private TangibleAssetsColumns chargeForYear;

    @JsonProperty("on_disposals")
    private TangibleAssetsColumns onDisposals;

    @JsonProperty("other_adjustments")
    private TangibleAssetsColumns otherAdjustments;

    @JsonProperty("at_period_end")
    private TangibleAssetsColumns atPeriodEnd;

}
