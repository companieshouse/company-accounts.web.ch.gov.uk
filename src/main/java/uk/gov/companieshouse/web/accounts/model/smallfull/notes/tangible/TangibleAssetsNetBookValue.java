package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsNetBookValue {

    private TangibleAssetsColumns currentPeriod;

    private TangibleAssetsColumns previousPeriod;

}