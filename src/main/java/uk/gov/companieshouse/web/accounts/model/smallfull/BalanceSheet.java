package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@Getter
@Setter
@ValidationModel
public class BalanceSheet {

    private BalanceSheetHeadings balanceSheetHeadings;

    private CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

    private FixedAssets fixedAssets;
    
    private CurrentAssets currentAssets;

    private OtherLiabilitiesOrAssets otherLiabilitiesOrAssets;
}
