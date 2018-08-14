package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Component
public class BalanceSheetTransformerImpl implements BalanceSheetTransformer {

    public BalanceSheet getBalanceSheet(CurrentPeriod currentPeriod) {

        uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheet apiCurrentPeriodBalanceSheet = currentPeriod.getBalanceSheet();

        BalanceSheet balanceSheet = new BalanceSheet();

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(apiCurrentPeriodBalanceSheet.getCalledUpShareCapitalNotPaid());

        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        return balanceSheet;
    }
}
