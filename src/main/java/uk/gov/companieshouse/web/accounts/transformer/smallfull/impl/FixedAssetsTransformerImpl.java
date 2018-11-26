package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component("fixedAssetsTransformer")
public class FixedAssetsTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (hasCurrentPeriodFixedAssets(balanceSheet)) {
            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangible(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getCurrentTotal());

            balanceSheetApi.setFixedAssets(fixedAssetsApi);
        }
    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (hasPreviousPeriodFixedAssets(balanceSheet)) {
            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();

            fixedAssetsApi.setTangible(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getPreviousTotal());

            balanceSheetApi.setFixedAssets(fixedAssetsApi);
        }
    }


    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        FixedAssets fixedAssets = createFixedAssets(balanceSheet);
        FixedAssetsApi fixedAssetsApi = balanceSheetApi.getFixedAssets();

        // Tangible assets
        if (fixedAssetsApi.getTangible() != null) {

            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setCurrentAmount(fixedAssetsApi.getTangible());
        }

        // Total fixed assets
        if (fixedAssetsApi.getTotal() != null) {
            fixedAssets.setCurrentTotal(fixedAssetsApi.getTotal());
        }
    }

    @Override
    public void addPreviousPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        FixedAssets fixedAssets = createFixedAssets(balanceSheet);
        FixedAssetsApi fixedAssetsApi = balanceSheetApi.getFixedAssets();

        // Tangible assets
        if (fixedAssetsApi.getTangible() != null) {
            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setPreviousAmount(fixedAssetsApi.getTangible());
        }

        // Total fixed assets
        if (fixedAssetsApi.getTotal() != null) {
            fixedAssets.setPreviousTotal(fixedAssetsApi.getTotal());
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

    private Boolean hasCurrentPeriodFixedAssets(BalanceSheet balanceSheet) {

        FixedAssets fixedAssets = balanceSheet.getFixedAssets();

        return Stream.of(fixedAssets.getTangibleAssets().getCurrentAmount(),
                    fixedAssets.getCurrentTotal()).
                anyMatch(Objects::nonNull);
    }

    private Boolean hasPreviousPeriodFixedAssets(BalanceSheet balanceSheet) {

        FixedAssets fixedAssets = balanceSheet.getFixedAssets();

        return Stream.of(fixedAssets.getTangibleAssets().getPreviousAmount(),
                    fixedAssets.getPreviousTotal()).
                anyMatch(Objects::nonNull);
    }
}
