package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapitalNotPaid;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

@Component
public class BalanceSheetTransformerImpl implements BalanceSheetTransformer {

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

    private void populateCurrentPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssetsApi() != null) {
            populateCurrentFixedAssets(balanceSheet, balanceSheetApi.getFixedAssetsApi());
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            populateCurrentCalledUpShareCapitalNotPaid(balanceSheet, balanceSheetApi.getCalledUpShareCapitalNotPaid());
        }
    }

    private void populatePreviousPeriodValues(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        if (balanceSheetApi.getFixedAssetsApi() != null) {
            populatePreviousFixedAssets(balanceSheet, balanceSheetApi.getFixedAssetsApi());
        }

        if (balanceSheetApi.getCalledUpShareCapitalNotPaid() != null) {
            populatePreviousCalledUpShareCapitalNotPaid(balanceSheet, balanceSheetApi.getCalledUpShareCapitalNotPaid());
        }
    }

    private void populateCurrentCalledUpShareCapitalNotPaid(BalanceSheet balanceSheet, Long amount) {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

        if (balanceSheet.getCalledUpShareCapitalNotPaid() == null) {
            calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
            balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);
        } else {
            calledUpShareCapitalNotPaid = balanceSheet.getCalledUpShareCapitalNotPaid();
        }

        calledUpShareCapitalNotPaid.setCurrentAmount(amount);
    }

    private void populatePreviousCalledUpShareCapitalNotPaid(BalanceSheet balanceSheet, Long amount) {

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

        if (balanceSheet.getCalledUpShareCapitalNotPaid() == null) {
            calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
            balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);
        } else {
            calledUpShareCapitalNotPaid = balanceSheet.getCalledUpShareCapitalNotPaid();
        }

        calledUpShareCapitalNotPaid.setPreviousAmount(amount);
    }

    private void populateCurrentFixedAssets(BalanceSheet balanceSheet, FixedAssetsApi fixedAssetsApi) {

        FixedAssets fixedAssets = createFixedAssets(balanceSheet);

        // Tangible assets
        if (fixedAssetsApi.getTangibleApi() != null) {

            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setCurrentAmount(fixedAssetsApi.getTangibleApi());
        }

        // Total fixed assets
        if (fixedAssetsApi.getTotal() != null) {
            fixedAssets.setTotalCurrentFixedAssets(fixedAssetsApi.getTotal());
        }
    }

    private FixedAssets createFixedAssets(BalanceSheet balanceSheet) {

        FixedAssets fixedAssets;

        if (balanceSheet.getFixedAssets() == null) {
            fixedAssets = new FixedAssets();
            balanceSheet.setFixedAssets(fixedAssets);
        } else {
            fixedAssets = balanceSheet.getFixedAssets();
        }

        return fixedAssets;
    }

    private TangibleAssets createTangibleAssets(BalanceSheet balanceSheet) {

        TangibleAssets tangibleAssets;

        if (balanceSheet.getFixedAssets().getTangibleAssets() == null) {
            tangibleAssets = new TangibleAssets();
            balanceSheet.getFixedAssets().setTangibleAssets(tangibleAssets);
        } else {
            tangibleAssets = balanceSheet.getFixedAssets().getTangibleAssets();
        }

        return tangibleAssets;
    }

    private void populatePreviousFixedAssets(BalanceSheet balanceSheet, FixedAssetsApi fixedAssetsApi) {

        FixedAssets fixedAssets = createFixedAssets(balanceSheet);

        // Tangible assets
        if (fixedAssetsApi.getTangibleApi() != null) {

            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setPreviousAmount(fixedAssetsApi.getTangibleApi());
        }

        // Total fixed assets
        if (fixedAssetsApi.getTotal() != null) {
            fixedAssets.setTotalPreviousFixedAssets(fixedAssetsApi.getTotal());
        }
    }

    @Override
    public CurrentPeriodApi getCurrentPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        if (balanceSheet.getFixedAssets() != null) {

            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangibleApi(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getTotalCurrentFixedAssets());

            balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);
        }

        if (balanceSheet.getCalledUpShareCapitalNotPaid() != null) {

            balanceSheetApi.setCalledUpShareCapitalNotPaid(balanceSheet.getCalledUpShareCapitalNotPaid().getCurrentAmount());
        }

        CurrentPeriodApi currentPeriod = new CurrentPeriodApi();
        currentPeriod.setBalanceSheetApi(balanceSheetApi);
        return currentPeriod;

    }

    @Override
    public PreviousPeriodApi getPreviousPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        if (balanceSheet.getFixedAssets() != null) {

            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangibleApi(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getTotalPreviousFixedAssets());

            balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        }

        if (balanceSheet.getCalledUpShareCapitalNotPaid() != null) {
            balanceSheetApi.setCalledUpShareCapitalNotPaid(balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());

        }

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        previousPeriodApi.setBalanceSheet(balanceSheetApi);

        return previousPeriodApi;
    }
}
