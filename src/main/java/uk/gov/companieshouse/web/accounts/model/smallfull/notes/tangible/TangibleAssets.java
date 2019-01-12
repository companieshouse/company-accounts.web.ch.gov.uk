package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssets {

    @JsonProperty("cost")
    private TangibleAssetsCost cost;

    @JsonProperty("depreciation")
    private TangibleAssetsDepreciation depreciation;

    @JsonProperty("net_book_value")
    private TangibleAssetsNetBookValue netBookValue;

    @JsonProperty("additional_information")
    private String additionalInformation;

}
