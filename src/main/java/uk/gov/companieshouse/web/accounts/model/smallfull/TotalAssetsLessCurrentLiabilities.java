package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalAssetsLessCurrentLiabilities {

    private Long currentAmount;

    private Long previousAmount;
}
