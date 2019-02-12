package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost.TangibleAssetsCost;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.netbookvalue.TangibleAssetsNetBookValue;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class TangibleAssets {

    private TangibleAssetsCost cost;

    private TangibleAssetsDepreciation depreciation;

    private TangibleAssetsNetBookValue netBookValue;

    @ValidationMapping("$.tangible_assets.additional_information")
    private String additionalInformation;

    private LocalDate lastAccountsPeriodEndOn;

    private LocalDate nextAccountsPeriodStartOn;

    private LocalDate nextAccountsPeriodEndOn;

}
