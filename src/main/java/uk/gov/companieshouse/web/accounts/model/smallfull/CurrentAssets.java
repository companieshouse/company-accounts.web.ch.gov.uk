package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentAssets {

    private Stocks stocks;
    private Debtors debtors;
    private CashAtBankAndInHand cashAtBankAndInHand;
    private Long currentCurrentAssetsTotal;
    private Long previousCurrentAssetsTotal;


}
