package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.MembersFundsApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.CapitalAndReserves;
import uk.gov.companieshouse.web.accounts.model.smallfull.MembersFunds;
import uk.gov.companieshouse.web.accounts.model.smallfull.MembersFundsProfitAndLossAccount;
import uk.gov.companieshouse.web.accounts.model.smallfull.TotalMembersFunds;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.Transformer;

@Component("membersFundsTransformer")
public class MembersFundsTransformerImpl implements Transformer {

    @Override
    public void addCurrentPeriodToApiModel(
        BalanceSheetApi balanceSheetApi, BalanceSheet balanceSheet) {

        if (hasCurrentPeriodMembersFunds(balanceSheet)) {
            MembersFundsApi membersFundsApi = new MembersFundsApi();
            membersFundsApi.setProfitAndLossAccount(
                balanceSheet.getMembersFunds().getProfitAndLossAccount().getCurrentAmount());
            membersFundsApi.setTotalMembersFunds(
                balanceSheet.getMembersFunds().getTotalMembersFunds().getCurrentAmount());
            balanceSheetApi.setMembersFunds(membersFundsApi);
        }

    }

    @Override
    public void addPreviousPeriodToApiModel(BalanceSheetApi balanceSheetApi,
        BalanceSheet balanceSheet) {

        if (hasPreviousPeriodMembersFunds(balanceSheet)) {
            MembersFundsApi membersFundsApi = new MembersFundsApi();
            membersFundsApi.setProfitAndLossAccount(
                balanceSheet.getMembersFunds().getProfitAndLossAccount().getPreviousAmount());
            membersFundsApi.setTotalMembersFunds(
                balanceSheet.getMembersFunds().getTotalMembersFunds().getPreviousAmount());
            balanceSheetApi.setMembersFunds(membersFundsApi);
        }

    }

    @Override
    public void addCurrentPeriodToWebModel(BalanceSheet balanceSheet,
        BalanceSheetApi balanceSheetApi) {

        createMembersFunds(balanceSheet);
        MembersFundsApi membersFundsApi = balanceSheetApi.getMembersFunds();

        // Profit and Loss
        if (membersFundsApi.getProfitAndLossAccount() != null) {
            MembersFundsProfitAndLossAccount profitAndLossAccount = createProfitAndLossAccount(balanceSheet);
            profitAndLossAccount.setCurrentAmount(membersFundsApi.getProfitAndLossAccount());
        }

        // Members funds
        if (membersFundsApi.getTotalMembersFunds() != null) {
            TotalMembersFunds totalMembersFunds = createTotalMembersFunds(
                balanceSheet);
            totalMembersFunds
                .setCurrentAmount(membersFundsApi.getTotalMembersFunds());
        }
    }

    @Override
    public void addPreviousPeriodToWebModel(BalanceSheet balanceSheet,
        BalanceSheetApi balanceSheetApi) {

        createMembersFunds(balanceSheet);
        MembersFundsApi membersFundsApi = balanceSheetApi.getMembersFunds();

        // Profit and Loss
        if (membersFundsApi.getProfitAndLossAccount() != null) {
            MembersFundsProfitAndLossAccount profitAndLossAccount = createProfitAndLossAccount(balanceSheet);
            profitAndLossAccount.setPreviousAmount(membersFundsApi.getProfitAndLossAccount());
        }

        // Members funds
        if (membersFundsApi.getTotalMembersFunds() != null) {
            TotalMembersFunds totalMembersFunds = createTotalMembersFunds(
                balanceSheet);
            totalMembersFunds
                .setPreviousAmount(membersFundsApi.getTotalMembersFunds());
        }
    }

    private MembersFunds createMembersFunds(BalanceSheet balanceSheet) {

        MembersFunds membersFunds;

        if (balanceSheet.getMembersFunds() == null) {
            membersFunds = new MembersFunds();
            balanceSheet.setMembersFunds(membersFunds);
        } else {
            membersFunds = balanceSheet.getMembersFunds();
        }

        return membersFunds;
    }

    private MembersFundsProfitAndLossAccount createProfitAndLossAccount(BalanceSheet balanceSheet) {

        MembersFundsProfitAndLossAccount membersFundProfitAndLossAccount;

        if (balanceSheet.getMembersFunds().getProfitAndLossAccount() == null) {
            membersFundProfitAndLossAccount = new MembersFundsProfitAndLossAccount();
            balanceSheet.getMembersFunds().setProfitAndLossAccount(membersFundProfitAndLossAccount);
        } else {
            membersFundProfitAndLossAccount = balanceSheet.getMembersFunds().getProfitAndLossAccount();
        }

        return membersFundProfitAndLossAccount;
    }

    private TotalMembersFunds createTotalMembersFunds(BalanceSheet balanceSheet) {

        TotalMembersFunds totalMembersFunds;

        if (balanceSheet.getMembersFunds().getTotalMembersFunds() == null) {
            totalMembersFunds = new TotalMembersFunds();
            balanceSheet.getMembersFunds().setTotalMembersFunds(totalMembersFunds);
        } else {
            totalMembersFunds = balanceSheet.getMembersFunds()
                .getTotalMembersFunds();
        }

        return totalMembersFunds;
    }

    private Boolean hasCurrentPeriodMembersFunds(BalanceSheet balanceSheet) {

        MembersFunds membersFunds = balanceSheet.getMembersFunds();
        if (membersFunds != null) {
            return Stream.of(membersFunds.getProfitAndLossAccount().getCurrentAmount(),
                membersFunds.getTotalMembersFunds().getCurrentAmount()).
                anyMatch(Objects::nonNull);
        }
        return false;
    }

    private Boolean hasPreviousPeriodMembersFunds(BalanceSheet balanceSheet) {

        MembersFunds membersFunds = balanceSheet.getMembersFunds();

        if (membersFunds != null) {
            return Stream.of(membersFunds.getProfitAndLossAccount().getPreviousAmount(),
                membersFunds.getTotalMembersFunds().getPreviousAmount()).
                anyMatch(Objects::nonNull);
        }
        return false;
        }
}