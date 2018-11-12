package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OtherLiabilitiesOrAssets {

    private PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

    private CreditorsDueWithinOneYear creditorsDueWithinOneYear;

    private CreditorsAfterOneYear creditorsAfterOneYear;

    private AccrualsAndDeferredIncome accrualsAndDeferredIncome;

    private NetCurrentAssets netCurrentAssets;

    private ProvisionForLiabilities provisionForLiabilities;

    private TotalNetAssets totalNetAssets;

    private TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities;
}
