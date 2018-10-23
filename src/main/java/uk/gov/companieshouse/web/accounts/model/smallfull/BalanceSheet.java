package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import javax.validation.Valid;

@Getter
@Setter
@ValidationModel
public class BalanceSheet {

    private BalanceSheetHeadings balanceSheetHeadings;

    @Valid
    private CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

    @Valid
    private FixedAssets fixedAssets;

    @Valid
    private CurrentAssets currentAssets;

    private OtherLiabilitiesOrAssets otherLiabilitiesOrAssets;
}
