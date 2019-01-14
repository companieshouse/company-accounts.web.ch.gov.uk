package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssets {

    private TangibleAssetsCost cost;

    private TangibleAssetsDepreciation depreciation;

    private TangibleAssetsNetBookValue netBookValue;

    private String additionalInformation;

}
