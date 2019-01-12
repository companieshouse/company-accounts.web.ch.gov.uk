package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsNetBookValue {

    @JsonProperty("current_period")
    private TangibleAssetsColumns currentPeriod;

    @JsonProperty("previous_period")
    private TangibleAssetsColumns previousPeriod;

}