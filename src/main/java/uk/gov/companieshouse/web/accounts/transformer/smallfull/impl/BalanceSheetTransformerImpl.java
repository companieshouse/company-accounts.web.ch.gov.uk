package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Component
public class BalanceSheetTransformerImpl implements BalanceSheetTransformer {

    public BalanceSheet getBalanceSheet() {

        BalanceSheet balanceSheet = new BalanceSheet();
        balanceSheet.setCalledUpShareCapitalNotPaid(new CalledUpShareCapitalNotPaid());

        return balanceSheet;
    }
}
