package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.abridged.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Component
public class BalanceSheetTransformerImpl implements BalanceSheetTransformer {

    @Override
    public BalanceSheet getBalanceSheet(CurrentPeriodApi currentPeriod) {

        BalanceSheetApi currentPeriodBalanceSheetApi = currentPeriod.getBalanceSheetApi();

        BalanceSheet balanceSheet = new BalanceSheet();

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(
                currentPeriodBalanceSheetApi.getCalledUpShareCapitalNotPaid().longValue());

        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);

        return balanceSheet;
    }

    @Override
    public CurrentPeriodApi getCurrentPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        balanceSheetApi.setCalledUpShareCapitalNotPaid(
                balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();

        currentPeriod.setBalanceSheetApi(balanceSheetApi);

        return currentPeriod;
    }

    @Override
    public PreviousPeriodApi getPreviousPeriod(BalanceSheet balanceSheet) {
        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        balanceSheetApi.setCalledUpShareCapitalNotPaid(
                balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();

        //previousPeriodApi.setBalanceSheetApi(balanceSheetApi);

        return new PreviousPeriodApi();
    }
}
