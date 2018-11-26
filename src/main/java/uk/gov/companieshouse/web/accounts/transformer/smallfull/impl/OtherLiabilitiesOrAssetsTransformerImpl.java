package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.OtherLiabilitiesOrAssetsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.AccrualsAndDeferredIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsAfterOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.CreditorsDueWithinOneYear;
import uk.gov.companieshouse.web.accounts.model.smallfull.NetCurrentAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherLiabilitiesOrAssets;
import uk.gov.companieshouse.web.accounts.model.smallfull.PrepaymentsAndAccruedIncome;
import uk.gov.companieshouse.web.accounts.model.smallfull.ProvisionForLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalAssetsLessCurrentLiabilities;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalNetAssets;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component("otherLiabilitiesOrAssetsTransformer")
public class OtherLiabilitiesOrAssetsTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (hasCurrentPeriodOtherLiabilitiesOrAssets(balanceSheet)) {
            OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
            OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();

            otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome().getCurrentAmount());
            otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear().getCurrentAmount());
            otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(otherLiabilitiesOrAssets.getCreditorsAfterOneYear().getCurrentAmount());
            otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome().getCurrentAmount());
            otherLiabilitiesOrAssetsApi.setNetCurrentAssets(otherLiabilitiesOrAssets.getNetCurrentAssets().getCurrentAmount());
            otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(otherLiabilitiesOrAssets.getProvisionForLiabilities().getCurrentAmount());
            otherLiabilitiesOrAssetsApi.setTotalNetAssets(otherLiabilitiesOrAssets.getTotalNetAssets().getCurrentAmount());
            otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities().getCurrentAmount());

            balanceSheetApi.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssetsApi);
        }
    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (hasPreviousPeriodOtherLiabilitiesOrAssets(balanceSheet)) {
            OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = new OtherLiabilitiesOrAssetsApi();
            OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();

            otherLiabilitiesOrAssetsApi.setPrepaymentsAndAccruedIncome(otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome().getPreviousAmount());
            otherLiabilitiesOrAssetsApi.setCreditorsDueWithinOneYear(otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear().getPreviousAmount());
            otherLiabilitiesOrAssetsApi.setCreditorsAfterOneYear(otherLiabilitiesOrAssets.getCreditorsAfterOneYear().getPreviousAmount());
            otherLiabilitiesOrAssetsApi.setAccrualsAndDeferredIncome(otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome().getPreviousAmount());
            otherLiabilitiesOrAssetsApi.setNetCurrentAssets(otherLiabilitiesOrAssets.getNetCurrentAssets().getPreviousAmount());
            otherLiabilitiesOrAssetsApi.setProvisionForLiabilities(otherLiabilitiesOrAssets.getProvisionForLiabilities().getPreviousAmount());
            otherLiabilitiesOrAssetsApi.setTotalNetAssets(otherLiabilitiesOrAssets.getTotalNetAssets().getPreviousAmount());
            otherLiabilitiesOrAssetsApi.setTotalAssetsLessCurrentLiabilities(otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities().getPreviousAmount());

            balanceSheetApi.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssetsApi);
        }
    }

    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        createOtherLiabilitiesOrAssets(balanceSheet);
        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = balanceSheetApi.getOtherLiabilitiesOrAssets();

        if (otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome() != null) {
            AccrualsAndDeferredIncome accrualsAndDeferredIncome = createAccrualsAndDeferredIncome(balanceSheet);
            accrualsAndDeferredIncome.setCurrentAmount(otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear() != null) {
            CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear(balanceSheet);
            creditorsAfterOneYear.setCurrentAmount(otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear() != null) {
            CreditorsDueWithinOneYear creditorsDueWithinOneYear = createCreditorsDueWithinOneYear(balanceSheet);
            creditorsDueWithinOneYear.setCurrentAmount(otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getNetCurrentAssets() != null) {
            NetCurrentAssets netCurrentAssets = createNetCurrentAssets(balanceSheet);
            netCurrentAssets.setCurrentAmount(otherLiabilitiesOrAssetsApi.getNetCurrentAssets());
        }

        if (otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome() != null) {
            PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = createPrepaymentsAndAccruedIncome(balanceSheet);
            prepaymentsAndAccruedIncome.setCurrentAmount(otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getProvisionForLiabilities() != null) {
            ProvisionForLiabilities provisionForLiabilities = createProvisionForLiabilities(balanceSheet);
            provisionForLiabilities.setCurrentAmount(otherLiabilitiesOrAssetsApi.getProvisionForLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities() != null) {
            TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities = createTotalAssetsLessCurrentLiabilities(balanceSheet);
            totalAssetsLessCurrentLiabilities.setCurrentAmount(otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalNetAssets() != null) {
            TotalNetAssets totalNetAssets = createTotalNetAssets(balanceSheet);
            totalNetAssets.setCurrentAmount(otherLiabilitiesOrAssetsApi.getTotalNetAssets());
        }
    }

    @Override
    public void addPreviousPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        createOtherLiabilitiesOrAssets(balanceSheet);
        OtherLiabilitiesOrAssetsApi otherLiabilitiesOrAssetsApi = balanceSheetApi.getOtherLiabilitiesOrAssets();

        if (otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome() != null) {
            AccrualsAndDeferredIncome accrualsAndDeferredIncome = createAccrualsAndDeferredIncome(balanceSheet);
            accrualsAndDeferredIncome.setPreviousAmount(otherLiabilitiesOrAssetsApi.getAccrualsAndDeferredIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear() != null) {
            CreditorsAfterOneYear creditorsAfterOneYear = createCreditorsAfterOneYear(balanceSheet);
            creditorsAfterOneYear.setPreviousAmount(otherLiabilitiesOrAssetsApi.getCreditorsAfterOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear() != null) {
            CreditorsDueWithinOneYear creditorsDueWithinOneYear = createCreditorsDueWithinOneYear(balanceSheet);
            creditorsDueWithinOneYear.setPreviousAmount(otherLiabilitiesOrAssetsApi.getCreditorsDueWithinOneYear());
        }

        if (otherLiabilitiesOrAssetsApi.getNetCurrentAssets() != null) {
            NetCurrentAssets netCurrentAssets = createNetCurrentAssets(balanceSheet);
            netCurrentAssets.setPreviousAmount(otherLiabilitiesOrAssetsApi.getNetCurrentAssets());
        }

        if (otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome() != null) {
            PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome = createPrepaymentsAndAccruedIncome(balanceSheet);
            prepaymentsAndAccruedIncome.setPreviousAmount(otherLiabilitiesOrAssetsApi.getPrepaymentsAndAccruedIncome());
        }

        if (otherLiabilitiesOrAssetsApi.getProvisionForLiabilities() != null) {
            ProvisionForLiabilities provisionForLiabilities = createProvisionForLiabilities(balanceSheet);
            provisionForLiabilities.setPreviousAmount(otherLiabilitiesOrAssetsApi.getProvisionForLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities() != null) {
            TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities = createTotalAssetsLessCurrentLiabilities(balanceSheet);
            totalAssetsLessCurrentLiabilities.setPreviousAmount(otherLiabilitiesOrAssetsApi.getTotalAssetsLessCurrentLiabilities());
        }

        if (otherLiabilitiesOrAssetsApi.getTotalNetAssets() != null) {
            TotalNetAssets totalNetAssets = createTotalNetAssets(balanceSheet);
            totalNetAssets.setPreviousAmount(otherLiabilitiesOrAssetsApi.getTotalNetAssets());
        }
    }

    private OtherLiabilitiesOrAssets createOtherLiabilitiesOrAssets(BalanceSheet balanceSheet) {

        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets;

        if (balanceSheet.getOtherLiabilitiesOrAssets() == null) {
            otherLiabilitiesOrAssets = new OtherLiabilitiesOrAssets();
            balanceSheet.setOtherLiabilitiesOrAssets(otherLiabilitiesOrAssets);
        } else {
            otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();
        }

        return otherLiabilitiesOrAssets;
    }

    private AccrualsAndDeferredIncome createAccrualsAndDeferredIncome(BalanceSheet balanceSheet) {

        AccrualsAndDeferredIncome accrualsAndDeferredIncome;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome() == null) {
            accrualsAndDeferredIncome = new AccrualsAndDeferredIncome();
            balanceSheet.getOtherLiabilitiesOrAssets().setAccrualsAndDeferredIncome(accrualsAndDeferredIncome);
        } else {
            accrualsAndDeferredIncome = balanceSheet.getOtherLiabilitiesOrAssets().getAccrualsAndDeferredIncome();
        }

        return accrualsAndDeferredIncome;
    }

    private CreditorsAfterOneYear createCreditorsAfterOneYear(BalanceSheet balanceSheet) {

        CreditorsAfterOneYear creditorsAfterOneYear;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear() == null) {
            creditorsAfterOneYear = new CreditorsAfterOneYear();
            balanceSheet.getOtherLiabilitiesOrAssets().setCreditorsAfterOneYear(creditorsAfterOneYear);
        } else {
            creditorsAfterOneYear = balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsAfterOneYear();
        }

        return creditorsAfterOneYear;
    }

    private CreditorsDueWithinOneYear createCreditorsDueWithinOneYear(BalanceSheet balanceSheet) {

        CreditorsDueWithinOneYear creditorsDueWithinOneYear;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear() == null) {
            creditorsDueWithinOneYear = new CreditorsDueWithinOneYear();
            balanceSheet.getOtherLiabilitiesOrAssets().setCreditorsDueWithinOneYear(creditorsDueWithinOneYear);
        } else {
            creditorsDueWithinOneYear = balanceSheet.getOtherLiabilitiesOrAssets().getCreditorsDueWithinOneYear();
        }

        return creditorsDueWithinOneYear;
    }

    private NetCurrentAssets createNetCurrentAssets(BalanceSheet balanceSheet) {

        NetCurrentAssets netCurrentAssets;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets() == null) {
            netCurrentAssets = new NetCurrentAssets();
            balanceSheet.getOtherLiabilitiesOrAssets().setNetCurrentAssets(netCurrentAssets);
        } else {
            netCurrentAssets = balanceSheet.getOtherLiabilitiesOrAssets().getNetCurrentAssets();
        }

        return netCurrentAssets;
    }

    private PrepaymentsAndAccruedIncome createPrepaymentsAndAccruedIncome(BalanceSheet balanceSheet) {

        PrepaymentsAndAccruedIncome prepaymentsAndAccruedIncome;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome() == null) {
            prepaymentsAndAccruedIncome = new PrepaymentsAndAccruedIncome();
            balanceSheet.getOtherLiabilitiesOrAssets().setPrepaymentsAndAccruedIncome(prepaymentsAndAccruedIncome);
        } else {
            prepaymentsAndAccruedIncome = balanceSheet.getOtherLiabilitiesOrAssets().getPrepaymentsAndAccruedIncome();
        }

        return prepaymentsAndAccruedIncome;
    }

    private ProvisionForLiabilities createProvisionForLiabilities(BalanceSheet balanceSheet) {

        ProvisionForLiabilities provisionForLiabilities;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities() == null) {
            provisionForLiabilities = new ProvisionForLiabilities();
            balanceSheet.getOtherLiabilitiesOrAssets().setProvisionForLiabilities(provisionForLiabilities);
        } else {
            provisionForLiabilities = balanceSheet.getOtherLiabilitiesOrAssets().getProvisionForLiabilities();
        }

        return provisionForLiabilities;
    }

    private TotalAssetsLessCurrentLiabilities createTotalAssetsLessCurrentLiabilities(BalanceSheet balanceSheet) {

        TotalAssetsLessCurrentLiabilities totalAssetsLessCurrentLiabilities;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities() == null) {
            totalAssetsLessCurrentLiabilities = new TotalAssetsLessCurrentLiabilities();
            balanceSheet.getOtherLiabilitiesOrAssets().setTotalAssetsLessCurrentLiabilities(totalAssetsLessCurrentLiabilities);
        } else {
            totalAssetsLessCurrentLiabilities = balanceSheet.getOtherLiabilitiesOrAssets().getTotalAssetsLessCurrentLiabilities();
        }

        return totalAssetsLessCurrentLiabilities;
    }

    private TotalNetAssets createTotalNetAssets(BalanceSheet balanceSheet) {

        TotalNetAssets totalNetAssets;

        if (balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets() == null) {
            totalNetAssets = new TotalNetAssets();
            balanceSheet.getOtherLiabilitiesOrAssets().setTotalNetAssets(totalNetAssets);
        } else {
            totalNetAssets = balanceSheet.getOtherLiabilitiesOrAssets().getTotalNetAssets();
        }

        return totalNetAssets;
    }

    private Boolean hasCurrentPeriodOtherLiabilitiesOrAssets(BalanceSheet balanceSheet) {

        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();

        return Stream.of(otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome().getCurrentAmount(),
                    otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear().getCurrentAmount(),
                    otherLiabilitiesOrAssets.getNetCurrentAssets().getCurrentAmount(),
                    otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities().getCurrentAmount(),
                    otherLiabilitiesOrAssets.getCreditorsAfterOneYear().getCurrentAmount(),
                    otherLiabilitiesOrAssets.getProvisionForLiabilities().getCurrentAmount(),
                    otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome().getCurrentAmount()).
                anyMatch(Objects::nonNull);
    }

    private Boolean hasPreviousPeriodOtherLiabilitiesOrAssets(BalanceSheet balanceSheet) {

        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets = balanceSheet.getOtherLiabilitiesOrAssets();

        return Stream.of(otherLiabilitiesOrAssets.getPrepaymentsAndAccruedIncome().getPreviousAmount(),
                    otherLiabilitiesOrAssets.getCreditorsDueWithinOneYear().getPreviousAmount(),
                    otherLiabilitiesOrAssets.getNetCurrentAssets().getPreviousAmount(),
                    otherLiabilitiesOrAssets.getTotalAssetsLessCurrentLiabilities().getPreviousAmount(),
                    otherLiabilitiesOrAssets.getCreditorsAfterOneYear().getPreviousAmount(),
                    otherLiabilitiesOrAssets.getProvisionForLiabilities().getPreviousAmount(),
                    otherLiabilitiesOrAssets.getAccrualsAndDeferredIncome().getPreviousAmount()).
                anyMatch(Objects::nonNull);
    }
}
