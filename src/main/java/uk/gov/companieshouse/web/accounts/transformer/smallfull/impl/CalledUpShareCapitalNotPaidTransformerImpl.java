package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

@Component("calledUpShareCapitalNotPaidTransformer")
public class CalledUpShareCapitalNotPaidTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi,
        BalanceSheet balanceSheet) {

        if (Boolean.TRUE.equals(hasCurrentPeriodCalledUpShareCapitalNotPaid(balanceSheet))) {
            balanceSheetApi.setCalledUpShareCapitalNotPaid(
                balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        }
    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi,
        BalanceSheet balanceSheet) {

        if (Boolean.TRUE.equals(hasPreviousPeriodCalledUpShareCapitalNotPaid(balanceSheet))) {
            balanceSheetApi.setCalledUpShareCapitalNotPaid(
                balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());
        }
    }

    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet,
        BalanceSheetApi balanceSheetApi) {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

        if (balanceSheet.getCalledUpShareCapitalNotPaid() == null) {
            calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
            balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);
        } else {
            calledUpShareCapitalNotPaid = balanceSheet.getCalledUpShareCapitalNotPaid();
        }

        calledUpShareCapitalNotPaid
            .setCurrentAmount(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    @Override
    public void addPreviousPeriodToWebModel(BalanceSheet balanceSheet,
        BalanceSheetApi balanceSheetApi) {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

        if (balanceSheet.getCalledUpShareCapitalNotPaid() == null) {
            calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
            balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);
        } else {
            calledUpShareCapitalNotPaid = balanceSheet.getCalledUpShareCapitalNotPaid();
        }

        calledUpShareCapitalNotPaid
            .setPreviousAmount(balanceSheetApi.getCalledUpShareCapitalNotPaid());
    }

    private Boolean hasCurrentPeriodCalledUpShareCapitalNotPaid(BalanceSheet balanceSheet) {

        return (balanceSheet.getCalledUpShareCapitalNotPaid() != null
            && balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount() != null);
    }

    private Boolean hasPreviousPeriodCalledUpShareCapitalNotPaid(BalanceSheet balanceSheet) {

        return (balanceSheet.getCalledUpShareCapitalNotPaid() != null
            && balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount() != null);
    }
}
