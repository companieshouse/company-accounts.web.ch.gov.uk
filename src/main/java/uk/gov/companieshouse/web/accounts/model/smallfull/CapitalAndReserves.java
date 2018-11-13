package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CapitalAndReserves {

    private CalledUpShareCapital calledUpShareCapital;

    private SharePremiumAccount sharePremiumAccount;

    private OtherReserves otherReserves;

    private ProfitAndLossAccount profitAndLossAccount;

    private TotalShareholdersFunds totalShareholdersFunds;
}
