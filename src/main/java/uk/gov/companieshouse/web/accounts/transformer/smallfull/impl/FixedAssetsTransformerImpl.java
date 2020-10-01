package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.FixedAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.FixedInvestments;
import uk.gov.companieshouse.web.accounts.model.smallfull.TangibleAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component("fixedAssetsTransformer")
public class FixedAssetsTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (Boolean.TRUE.equals(hasCurrentPeriodFixedAssets(balanceSheet))) {
            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
            fixedAssetsApi.setIntangible(balanceSheet.getFixedAssets().getIntangibleAssets().getCurrentAmount());
            fixedAssetsApi.setTangible(balanceSheet.getFixedAssets().getTangibleAssets().getCurrentAmount());
            fixedAssetsApi.setInvestments(balanceSheet.getFixedAssets().getInvestments().getCurrentAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getCurrentTotal());

            balanceSheetApi.setFixedAssets(fixedAssetsApi);
        }
    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (Boolean.TRUE.equals(hasPreviousPeriodFixedAssets(balanceSheet))) {
            FixedAssetsApi fixedAssetsApi = new FixedAssetsApi();
            fixedAssetsApi.setIntangible(balanceSheet.getFixedAssets().getIntangibleAssets().getPreviousAmount());
            fixedAssetsApi.setTangible(balanceSheet.getFixedAssets().getTangibleAssets().getPreviousAmount());
            fixedAssetsApi.setInvestments(balanceSheet.getFixedAssets().getInvestments().getPreviousAmount());
            fixedAssetsApi.setTotal(balanceSheet.getFixedAssets().getPreviousTotal());

            balanceSheetApi.setFixedAssets(fixedAssetsApi);
        }
    }


    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        FixedAssets fixedAssets = createFixedAssets(balanceSheet);
        FixedAssetsApi fixedAssetsApi = balanceSheetApi.getFixedAssets();

        // Intangible assets
        if (fixedAssetsApi.getIntangible() != null) {

            IntangibleAssets intangibleAssets = createIntangibleAssets(balanceSheet);
            intangibleAssets.setCurrentAmount(fixedAssetsApi.getIntangible());
        }

        // Tangible assets
        if (fixedAssetsApi.getTangible() != null) {

            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setCurrentAmount(fixedAssetsApi.getTangible());
        }

        //Fixed assets investments
        if (fixedAssetsApi.getInvestments() !=null) {

            FixedInvestments fixedInvestments = createFixedInvestments(balanceSheet);
            fixedInvestments.setCurrentAmount((fixedAssetsApi.getInvestments()));
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

        // Intangible assets
        if (fixedAssetsApi.getIntangible() != null) {
            IntangibleAssets intangibleAssets = createIntangibleAssets(balanceSheet);
            intangibleAssets.setPreviousAmount(fixedAssetsApi.getIntangible());
        }

        // Tangible assets
        if (fixedAssetsApi.getTangible() != null) {
            TangibleAssets tangibleAssets = createTangibleAssets(balanceSheet);
            tangibleAssets.setPreviousAmount(fixedAssetsApi.getTangible());
        }

        // Fixed Investments
        if (fixedAssetsApi.getInvestments() != null) {
            FixedInvestments fixedInvestments = createFixedInvestments(balanceSheet);
            fixedInvestments.setPreviousAmount(fixedAssetsApi.getInvestments());
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

    private IntangibleAssets createIntangibleAssets(BalanceSheet balanceSheet) {

        IntangibleAssets intangibleAssets;

        if (balanceSheet.getFixedAssets().getIntangibleAssets() == null) {
            intangibleAssets = new IntangibleAssets();
            balanceSheet.getFixedAssets().setIntangibleAssets(intangibleAssets);
        } else {
            intangibleAssets = balanceSheet.getFixedAssets().getIntangibleAssets();
        }

        return intangibleAssets;
    }

    private FixedInvestments createFixedInvestments(BalanceSheet balanceSheet){

        FixedInvestments fixedInvestments;

        if(balanceSheet.getFixedAssets().getInvestments() == null) {
            fixedInvestments = new FixedInvestments();
            balanceSheet.getFixedAssets().setInvestments(fixedInvestments);
        } else {
            fixedInvestments = balanceSheet.getFixedAssets().getInvestments();
        }

        return fixedInvestments;
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
