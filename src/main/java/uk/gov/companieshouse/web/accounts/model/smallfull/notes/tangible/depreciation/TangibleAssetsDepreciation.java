package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsDepreciation {

    private DepreciationAtPeriodStart atPeriodStart;

    private ChargeForYear chargeForYear;

    private OnDisposals onDisposals;

    private OtherAdjustments otherAdjustments;

    private DepreciationAtPeriodEnd atPeriodEnd;

}
