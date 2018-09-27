package uk.gov.companieshouse.web.accounts.model.smallfull;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
public class BalanceSheet {

    private BalanceSheetHeadings balanceSheetHeadings;

    @Valid
    private CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

    @Valid
    private FixedAssets fixedAssets;
}
