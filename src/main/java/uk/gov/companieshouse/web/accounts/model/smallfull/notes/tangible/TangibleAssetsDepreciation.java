package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsDepreciation {

    private TangibleAssetsColumns atPeriodStart;

    private TangibleAssetsColumns chargeForYear;

    private TangibleAssetsColumns onDisposals;

    private TangibleAssetsColumns otherAdjustments;

    private TangibleAssetsColumns atPeriodEnd;

}
