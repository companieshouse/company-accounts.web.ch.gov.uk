package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssets {

    private TangibleAssetsCost cost;

    private TangibleAssetsDepreciation depreciation;

    private TangibleAssetsNetBookValue netBookValue;

    private String additionalInformation;

    private LocalDate lastAccountsPeriodEndOn;

    private LocalDate nextAccountsPeriodStartOn;

    private LocalDate nextAccountsPeriodEndOn;

}
