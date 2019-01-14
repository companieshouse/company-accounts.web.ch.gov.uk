package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsCost {

    private TangibleAssetsColumns atPeriodStart;

    private TangibleAssetsColumns additions;

    private TangibleAssetsColumns disposals;

    private TangibleAssetsColumns revaluations;

    private TangibleAssetsColumns transfers;

    private TangibleAssetsColumns atPeriodEnd;

}
