package uk.gov.companieshouse.web.accounts.model.smallfull;

import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class BalanceSheet {
    private BalanceSheetHeadings balanceSheetHeadings;

    private Boolean lbg;

    private CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid;

    private FixedAssets fixedAssets;

    private CurrentAssets currentAssets;

    private CapitalAndReserves capitalAndReserves;

    private OtherLiabilitiesOrAssets otherLiabilitiesOrAssets;

    private MembersFunds membersFunds;

    public BalanceSheetHeadings getBalanceSheetHeadings() {
        return balanceSheetHeadings;
    }

    public void setBalanceSheetHeadings(
        BalanceSheetHeadings balanceSheetHeadings) {
        this.balanceSheetHeadings = balanceSheetHeadings;
    }

    public Boolean getLbg() {
        return lbg;
    }

    public void setLbg(Boolean lbg) {
        this.lbg = lbg;
    }

    public CalledUpShareCapitalNotPaid getCalledUpShareCapitalNotPaid() {
        return calledUpShareCapitalNotPaid;
    }

    public void setCalledUpShareCapitalNotPaid(
        CalledUpShareCapitalNotPaid calledUpShareCapitalNotPaid) {
        this.calledUpShareCapitalNotPaid = calledUpShareCapitalNotPaid;
    }

    public FixedAssets getFixedAssets() {
        return fixedAssets;
    }

    public void setFixedAssets(FixedAssets fixedAssets) {
        this.fixedAssets = fixedAssets;
    }

    public CurrentAssets getCurrentAssets() {
        return currentAssets;
    }

    public void setCurrentAssets(
        CurrentAssets currentAssets) {
        this.currentAssets = currentAssets;
    }

    public CapitalAndReserves getCapitalAndReserves() {
        return capitalAndReserves;
    }

    public void setCapitalAndReserves(
        CapitalAndReserves capitalAndReserves) {
        this.capitalAndReserves = capitalAndReserves;
    }

    public OtherLiabilitiesOrAssets getOtherLiabilitiesOrAssets() {
        return otherLiabilitiesOrAssets;
    }

    public void setOtherLiabilitiesOrAssets(
        OtherLiabilitiesOrAssets otherLiabilitiesOrAssets) {
        this.otherLiabilitiesOrAssets = otherLiabilitiesOrAssets;
    }

    public MembersFunds getMembersFunds() {
        return membersFunds;
    }

    public void setMembersFunds(MembersFunds membersFunds) {
        this.membersFunds = membersFunds;
    }
}