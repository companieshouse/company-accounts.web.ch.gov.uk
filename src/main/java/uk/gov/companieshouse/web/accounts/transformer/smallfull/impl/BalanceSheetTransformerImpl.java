package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

@Component
public class BalanceSheetTransformerImpl implements BalanceSheetTransformer {

    @Autowired
    @Qualifier("calledUpShareCapitalNotPaidTransformer")
    private Transformer calledUpShareCapitalNotPaidTransformer;

    @Autowired
    @Qualifier("fixedAssetsTransformer")
    private Transformer fixedAssetsTransformer;

    @Autowired
    @Qualifier("currentAssetsTransformer")
    private Transformer currentAssetsTransformer;

    @Autowired
    @Qualifier("otherLiabilitiesOrAssetsTransformer")
    private Transformer otherLiabilitiesOrAssetsTransformer;

    @Autowired
    @Qualifier("capitalAndReservesTransformer")
    private Transformer capitalAndReservesTransformer;

    @Override
    public BalanceSheet getBalanceSheet(CurrentPeriodApi currentPeriodApi, PreviousPeriodApi previousPeriodApi) {

        BalanceSheet balanceSheet = new BalanceSheet();

        if (currentPeriodApi != null && currentPeriodApi.getBalanceSheetApi() != null) {
            populateCurrentPeriodValues(balanceSheet, currentPeriodApi.getBalanceSheetApi());
        }

        if (previousPeriodApi != null && previousPeriodApi.getBalanceSheet() != null) {
            populatePreviousPeriodValues(balanceSheet, previousPeriodApi.getBalanceSheet());
        }

        return balanceSheet;
    }

    @Override
    public CurrentPeriodApi getCurrentPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        fixedAssetsTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        calledUpShareCapitalNotPaidTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        currentAssetsTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        otherLiabilitiesOrAssetsTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        capitalAndReservesTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();
        currentPeriod.setBalanceSheetApi(balanceSheetApi);

        return currentPeriod;
    }

    @Override
    public PreviousPeriodApi getPreviousPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        fixedAssetsTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        calledUpShareCapitalNotPaidTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        currentAssetsTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        otherLiabilitiesOrAssetsTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        capitalAndReservesTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        previousPeriodApi.setBalanceSheet(balanceSheetApi);
        return previousPeriodApi;
    }

    private void populateCurrentPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssetsApi() != null) {
            fixedAssetsTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            calledUpShareCapitalNotPaidTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCurrentAssetsApi() != null) {
            currentAssetsTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getOtherLiabilitiesOrAssetsApi() != null) {
            otherLiabilitiesOrAssetsTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCapitalAndReservesApi() != null) {
            capitalAndReservesTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }
    }

    private void populatePreviousPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssetsApi() != null) {
            fixedAssetsTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            calledUpShareCapitalNotPaidTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCurrentAssetsApi() != null) {
            currentAssetsTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getOtherLiabilitiesOrAssetsApi() != null) {
            otherLiabilitiesOrAssetsTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCapitalAndReservesApi() != null) {
            capitalAndReservesTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }
    }

}
