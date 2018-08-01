package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;

public class BalanceSheetTransformer {

    public BalanceSheet getBalanceSheet() {

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(new CalledUpShareCapitalNotPaid());

        return balanceSheet;
    }
}
