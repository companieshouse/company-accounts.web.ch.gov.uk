package uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.cost;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TangibleAssetsCost {

    private CostAtPeriodStart atPeriodStart;

    private Additions additions;

    private Disposals disposals;

    private Revaluations revaluations;

    private Transfers transfers;

    private CostAtPeriodEnd atPeriodEnd;

}
