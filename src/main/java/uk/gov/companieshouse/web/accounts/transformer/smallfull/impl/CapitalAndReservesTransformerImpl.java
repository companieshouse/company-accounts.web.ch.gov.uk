package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.CapitalAndReservesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.*;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

@Component("capitalAndReservesTransformer")
public class CapitalAndReservesTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        CapitalAndReservesApi capitalAndReservesApi = new CapitalAndReservesApi();
        capitalAndReservesApi.setCalledUpShareCapital(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getCurrentAmount());
        capitalAndReservesApi.setOtherReserves(balanceSheet.getCapitalAndReserves().getOtherReserves().getCurrentAmount());
        capitalAndReservesApi.setProfitAndLoss(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getCurrentAmount());
        capitalAndReservesApi.setSharePremiumAccount(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getCurrentAmount());
        capitalAndReservesApi.setTotalShareholdersFunds(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getCurrentAmount());
        balanceSheetApi.setCapitalAndReservesApi(capitalAndReservesApi);

    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        CapitalAndReservesApi capitalAndReservesApi = new CapitalAndReservesApi();
        capitalAndReservesApi.setCalledUpShareCapital(balanceSheet.getCapitalAndReserves().getCalledUpShareCapital().getPreviousAmount());
        capitalAndReservesApi.setOtherReserves(balanceSheet.getCapitalAndReserves().getOtherReserves().getPreviousAmount());
        capitalAndReservesApi.setProfitAndLoss(balanceSheet.getCapitalAndReserves().getProfitAndLossAccount().getPreviousAmount());
        capitalAndReservesApi.setSharePremiumAccount(balanceSheet.getCapitalAndReserves().getSharePremiumAccount().getPreviousAmount());
        capitalAndReservesApi.setTotalShareholdersFunds(balanceSheet.getCapitalAndReserves().getTotalShareholdersFunds().getPreviousAmount());

        balanceSheetApi.setCapitalAndReservesApi(capitalAndReservesApi);

    }

    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet, BalanceSheetApi balanceSheetApi) {

        CapitalAndReserves capitalAndReserves = createCapitalAndReserves(balanceSheet);
        CapitalAndReservesApi capitalAndReservesApi = balanceSheetApi.getCapitalAndReservesApi();

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

        CapitalAndReserves capitalAndReserves = createCapitalAndReserves(balanceSheet);
        CapitalAndReservesApi capitalAndReservesApi = balanceSheetApi.getCapitalAndReservesApi();

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
            totalShareholdersFunds.setPreviousAmount(totalShareholdersFunds.getPreviousAmount());
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
}