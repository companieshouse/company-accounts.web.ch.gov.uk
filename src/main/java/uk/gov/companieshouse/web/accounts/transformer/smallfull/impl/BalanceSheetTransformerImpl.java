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
    public BalanceSheet getBalanceSheet(CurrentPeriodApi currentPeriod) {

        BalanceSheetApi currentPeriodBalanceSheetApi = currentPeriod.getBalanceSheetApi();
        BalanceSheet balanceSheet = new BalanceSheet();

        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid = new CalledUpShareCapitalNotPaid();
        calledUpShareCapitalNotPaid.setCurrentAmount(currentPeriodBalanceSheetApi.getCalledUpShareCapitalNotPaid());

        TangibleAssets tangibleAssets = new TangibleAssets();
        tangibleAssets.setCurrentAmount(currentPeriodBalanceSheetApi.getFixedAssetsApi().getTangibleApi());

        FixedAssets fixedAssets = new FixedAssets();
        fixedAssets.setTotalCurrentFixedAssets(currentPeriodBalanceSheetApi.getFixedAssetsApi().getTotal());
        fixedAssets.setTangibleAssets(tangibleAssets);

        balanceSheet.setCalledUpShareCapitalNotPaid(calledUpShareCapitalNotPaid);
        balanceSheet.setFixedAssets(fixedAssets);

        return balanceSheet;
    }

    @Override
    public CurrentPeriodApi getCurrentPeriod(BalanceSheet balanceSheet) {

        BalanceSheetApi balanceSheetApi = new BalanceSheetApi();

        if (balanceSheet.getFixedAssets()!= null) {

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
        balanceSheetApi.setCalledUpShareCapitalNotPaid(balanceSheet.getCalledUpShareCapitalNotPaid().getPreviousAmount());

        if (balanceSheet.getFixedAssets()!= null) {

            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangibleApi(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getTotalPreviousFixedAssets());

            balanceSheetApi.setFixedAssetsApi(fixedAssetsApi);

        }

        PreviousPeriodApi previousPeriodApi = new PreviousPeriodApi();
        previousPeriodApi.setBalanceSheet(balanceSheetApi);
        return previousPeriodApi;
    }
}
