package uk.gov.companieshouse.web.accounts.model.smallfull;

public class MembersFunds {

    private MembersFundsProfitAndLossAccount profitAndLossAccount;

    private TotalMembersFunds totalMembersFunds;

    public MembersFundsProfitAndLossAccount getProfitAndLossAccount() {
        return profitAndLossAccount;
    }

    public void setProfitAndLossAccount(
        MembersFundsProfitAndLossAccount profitAndLossAccount) {
        this.profitAndLossAccount = profitAndLossAccount;
    }

    public TotalMembersFunds getTotalMembersFunds() {
        return totalMembersFunds;
    }

    public void setTotalMembersFunds(
        TotalMembersFunds totalMembersFunds) {
        this.totalMembersFunds = totalMembersFunds;
    }
}
