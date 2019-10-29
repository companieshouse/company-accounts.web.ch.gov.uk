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

    @Autowired
    @Qualifier("membersFundsTransformer")
    private Transformer membersFundsTransformer;

    @Override
    public BalanceSheet getBalanceSheet(CurrentPeriodApi currentPeriodApi, PreviousPeriodApi previousPeriodApi) {

        BalanceSheet balanceSheet = new BalanceSheet();

        if (currentPeriodApi != null && currentPeriodApi.getBalanceSheet() != null) {
            populateCurrentPeriodValues(balanceSheet, currentPeriodApi.getBalanceSheet());
        }

        if (previousPeriodApi != null && previousPeriodApi.getBalanceSheet() != null) {
            populatePreviousPeriodValues(balanceSheet, previousPeriodApi.getBalanceSheet());
        }

        return balanceSheet;
    }

    @Override
    public BalanceSheetApi getCurrentPeriodBalanceSheet(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        fixedAssetsTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        calledUpShareCapitalNotPaidTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        currentAssetsTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        otherLiabilitiesOrAssetsTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        if (balanceSheet.getLbg()){
            membersFundsTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        } else {
            capitalAndReservesTransformer.addCurrentPeriodToApiModel(balanceSheetApi, balanceSheet);
        }

        return balanceSheetApi;
    }

    @Override
    public BalanceSheetApi getPreviousPeriodBalanceSheet(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        fixedAssetsTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        calledUpShareCapitalNotPaidTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        currentAssetsTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        otherLiabilitiesOrAssetsTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        if (balanceSheet.getLbg()){
            membersFundsTransformer.addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        } else {
            capitalAndReservesTransformer
                .addPreviousPeriodToApiModel(balanceSheetApi, balanceSheet);
        }

        return balanceSheetApi;
    }

    private void populateCurrentPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssets() != null) {
            fixedAssetsTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            calledUpShareCapitalNotPaidTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCurrentAssets() != null) {
            currentAssetsTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getOtherLiabilitiesOrAssets() != null) {
            otherLiabilitiesOrAssetsTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCapitalAndReserves() != null) {
            capitalAndReservesTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getMembersFunds() != null) {
            membersFundsTransformer.addCurrentPeriodToWebModel(balanceSheet, balanceSheetApi);
        }
    }

    private void populatePreviousPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssets() != null) {
            fixedAssetsTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            calledUpShareCapitalNotPaidTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCurrentAssets() != null) {
            currentAssetsTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getOtherLiabilitiesOrAssets() != null) {
            otherLiabilitiesOrAssetsTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getCapitalAndReserves() != null) {
            capitalAndReservesTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }

        if (balanceSheetApi.getMembersFunds() != null) {
            membersFundsTransformer.addPreviousPeriodToWebModel(balanceSheet, balanceSheetApi);
        }
    }
}
