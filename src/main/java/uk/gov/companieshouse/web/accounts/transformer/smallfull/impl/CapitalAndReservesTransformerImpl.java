package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CapitalAndReservesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.CapitalAndReserves;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalShareholdersFunds;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CalledUpShareCapital;
import uk.gov.companieshouse.web.accounts.model.smallfull.ProfitAndLossAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.SharePremiumAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.OtherReserves;

import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

@Component("capitalAndReservesTransformer")
public class CapitalAndReservesTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        CapitalAndReservesApi capitalAndReservesApi = new CapitalAndReservesApi();
        if (hasCurrentPeriodCapitalAndReserves(balanceSheet)) {
            capitalAndReservesApi.setCalledUpShareCapital(
                balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getCurrentAmount());
            capitalAndReservesApi.setOtherReserves(
                balanceSheet.getCapitalAndReserves().getOtherReserves().getCurrentAmount());
            capitalAndReservesApi.setProfitAndLoss(
                balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getCurrentAmount());
            capitalAndReservesApi.setSharePremiumAccount(
                balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getCurrentAmount());
            capitalAndReservesApi.setTotalShareholdersFunds(
                balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds()
                    .getCurrentAmount());
        }

        balanceSheetApi.setCapitalAndReserves(capitalAndReservesApi);
    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        CapitalAndReservesApi capitalAndReservesApi = new CapitalAndReservesApi();

        if (hasPreviousPeriodCapitalAndReserves(balanceSheet)) {
            capitalAndReservesApi.setCalledUpShareCapital(
                balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getPreviousAmount());
            capitalAndReservesApi.setOtherReserves(
                balanceSheet.getCapitalAndReserves().getOtherReserves().getPreviousAmount());
            capitalAndReservesApi.setProfitAndLoss(
                balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getPreviousAmount());
            capitalAndReservesApi.setSharePremiumAccount(
                balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getPreviousAmount());
            capitalAndReservesApi.setTotalShareholdersFunds(
                balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds()
                    .getPreviousAmount());
        }

        balanceSheetApi.setCapitalAndReserves(capitalAndReservesApi);
    }

    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        createCapitalAndReserves(balanceSheet);
        CapitalAndReservesApi capitalAndReservesApi = balanceSheetApi.getCapitalAndReserves();

        // Share capital
        if (capitalAndReservesApi.getCalledUpShareCapital() != null) {
            CalledUpShareCapital calledUpShareCapital = createCalledUpShareCapital(balanceSheet);
            calledUpShareCapital.setCurrentAmount(capitalAndReservesApi.getCalledUpShareCapital());
        }

        // Other reserves
        if (capitalAndReservesApi.getOtherReserves() != null) {
            OtherReserves otherReserves = createOtherReserves(balanceSheet);
            otherReserves.setCurrentAmount(capitalAndReservesApi.getOtherReserves());
        }

        // Profit and Loss
        if (capitalAndReservesApi.getProfitAndLoss() != null) {
            ProfitAndLossAccount profitAndLossAccount = createProfitAndLossAccount(balanceSheet);
            profitAndLossAccount.setCurrentAmount(capitalAndReservesApi.getProfitAndLoss());
        }

        // Share premium accounts
        if (capitalAndReservesApi.getSharePremiumAccount() != null) {
            SharePremiumAccount sharePremiumAccount = createSharePremiumAccounts(balanceSheet);
            sharePremiumAccount.setCurrentAmount(capitalAndReservesApi.getSharePremiumAccount());
        }

        // Shareholder funds
        if (capitalAndReservesApi.getTotalShareholdersFunds() != null) {
            TotalShareholdersFunds totalShareholdersFunds = createTotalShareholdersFund(balanceSheet);
            totalShareholdersFunds.setCurrentAmount(capitalAndReservesApi.getTotalShareholdersFunds());
        }
    }

    @Override
    public void addPreviousPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        createCapitalAndReserves(balanceSheet);
        CapitalAndReservesApi capitalAndReservesApi = balanceSheetApi.getCapitalAndReserves();

        // Share capital
        if (capitalAndReservesApi.getCalledUpShareCapital() != null) {
            CalledUpShareCapital calledUpShareCapital = createCalledUpShareCapital(balanceSheet);
            calledUpShareCapital.setPreviousAmount(capitalAndReservesApi.getCalledUpShareCapital());
        }

        // Other reserves
        if (capitalAndReservesApi.getOtherReserves() != null) {
            OtherReserves otherReserves = createOtherReserves(balanceSheet);
            otherReserves.setPreviousAmount(capitalAndReservesApi.getOtherReserves());
        }

        // Profit and Loss
        if (capitalAndReservesApi.getProfitAndLoss() != null) {
            ProfitAndLossAccount profitAndLossAccount = createProfitAndLossAccount(balanceSheet);
            profitAndLossAccount.setPreviousAmount(capitalAndReservesApi.getProfitAndLoss());
        }

        // Share premium accounts
        if (capitalAndReservesApi.getSharePremiumAccount() != null) {
            SharePremiumAccount sharePremiumAccount = createSharePremiumAccounts(balanceSheet);
            sharePremiumAccount.setPreviousAmount(capitalAndReservesApi.getSharePremiumAccount());
        }

        // Shareholder funds
        if (capitalAndReservesApi.getTotalShareholdersFunds() != null) {
            TotalShareholdersFunds totalShareholdersFunds = createTotalShareholdersFund(balanceSheet);
            totalShareholdersFunds.setPreviousAmount(capitalAndReservesApi.getTotalShareholdersFunds());
        }
    }

    private CapitalAndReserves createCapitalAndReserves(BalanceSheet balanceSheet) {

        CapitalAndReserves capitalAndReserves;

        if (balanceSheet.getCapitalAndReserves() == null) {
            capitalAndReserves = new CapitalAndReserves();
            balanceSheet.setCapitalAndReserves(capitalAndReserves);
        } else {
            capitalAndReserves = balanceSheet.getCapitalAndReserves();
        }

        return capitalAndReserves;
    }

    private CalledUpShareCapital createCalledUpShareCapital(BalanceSheet balanceSheet) {

        CalledUpShareCapital calledUpShareCapital;

        if (balanceSheet.getCapitalAndReserves().getCalledUpShareCapital() == null) {
            calledUpShareCapital = new CalledUpShareCapital();
            balanceSheet.getCapitalAndReserves().setCalledUpShareCapital(calledUpShareCapital);
        } else {
            calledUpShareCapital = balanceSheet.getCapitalAndReserves().getCalledUpShareCapital();
        }

        return calledUpShareCapital;
    }

    private ProfitAndLossAccount createProfitAndLossAccount(BalanceSheet balanceSheet) {

        ProfitAndLossAccount profitAndLossAccount;

        if (balanceSheet.getCapitalAndReserves().getProfitAndLossAccount() == null) {
            profitAndLossAccount = new ProfitAndLossAccount();
            balanceSheet.getCapitalAndReserves().setProfitAndLossAccount(profitAndLossAccount);
        } else {
            profitAndLossAccount = balanceSheet.getCapitalAndReserves().getProfitAndLossAccount();
        }

        return profitAndLossAccount;
    }

    private OtherReserves createOtherReserves(BalanceSheet balanceSheet) {

        OtherReserves otherReserves;

        if (balanceSheet.getCapitalAndReserves().getOtherReserves() == null) {
            otherReserves = new OtherReserves();
            balanceSheet.getCapitalAndReserves().setOtherReserves(otherReserves);
        } else {
            otherReserves = balanceSheet.getCapitalAndReserves().getOtherReserves();
        }

        return otherReserves;
    }

    private SharePremiumAccount createSharePremiumAccounts(BalanceSheet balanceSheet) {

        SharePremiumAccount sharePremiumAccount;

        if (balanceSheet.getCapitalAndReserves().getSharePremiumAccount() == null) {
            sharePremiumAccount = new SharePremiumAccount();
            balanceSheet.getCapitalAndReserves().setSharePremiumAccount(sharePremiumAccount);
        } else {
            sharePremiumAccount = balanceSheet.getCapitalAndReserves().getSharePremiumAccount();
        }

        return sharePremiumAccount;
    }

    private TotalShareholdersFunds createTotalShareholdersFund(BalanceSheet balanceSheet) {

        TotalShareholdersFunds totalShareholdersFunds;

        if (balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds() == null) {
            totalShareholdersFunds = new TotalShareholdersFunds();
            balanceSheet.getCapitalAndReserves().setTotalShareholdersFunds(totalShareholdersFunds);
        } else {
            totalShareholdersFunds = balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds();
        }

        return totalShareholdersFunds;
    }

    private Boolean hasCurrentPeriodCapitalAndReserves(BalanceSheet balanceSheet) {

        CapitalAndReserves capitalAndReserves = balanceSheet.getCapitalAndReserves();

        if (capitalAndReserves != null) {
            return Stream.of(capitalAndReserves.getCalledUpShareCapital().getCurrentAmount(),
                capitalAndReserves.getSharePremiumAccount().getCurrentAmount(),
                capitalAndReserves.getOtherReserves().getCurrentAmount(),
                capitalAndReserves.getProfitAndLossAccount().getCurrentAmount(),
                capitalAndReserves.getTotalShareholdersFunds().getCurrentAmount()).
                anyMatch(Objects::nonNull);
        }
        return false;
    }

    private Boolean hasPreviousPeriodCapitalAndReserves(BalanceSheet balanceSheet) {

        CapitalAndReserves capitalAndReserves = balanceSheet.getCapitalAndReserves();

        if (capitalAndReserves != null) {
            return Stream.of(capitalAndReserves.getCalledUpShareCapital().getPreviousAmount(),
                capitalAndReserves.getSharePremiumAccount().getPreviousAmount(),
                capitalAndReserves.getOtherReserves().getPreviousAmount(),
                capitalAndReserves.getProfitAndLossAccount().getPreviousAmount(),
                capitalAndReserves.getTotalShareholdersFunds().getPreviousAmount()).
                anyMatch(Objects::nonNull);
        }
        return false;
    }
}