package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsCost {

    @JsonProperty("at_period_start")
    private TangibleAssetsColumns atPeriodStart;

    @JsonProperty("additions")
    private TangibleAssetsColumns additions;

    @JsonProperty("disposals")
    private TangibleAssetsColumns disposals;

    @JsonProperty("revaluations")
    private TangibleAssetsColumns revaluations;

    @JsonProperty("transfers")
    private TangibleAssetsColumns transfers;

    @JsonProperty("at_period_end")
    private TangibleAssetsColumns atPeriodEnd;

}
