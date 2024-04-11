package uk.gov.companieshouse.web.accounts.model.relatedpartytransactions;

import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class AddOrRemoveRptTransactions {
    public AddOrRemoveRptTransactions() {
        this.rptTransactionToAdd = new RptTransactionToAdd();
        this.rptTransactionToAdd.setBreakdown(new RptTransactionBreakdown());
    }

    private RptTransaction[] existingRptTransactions;

    private RptTransactionToAdd rptTransactionToAdd;

    private AccountingPeriodApi nextAccount;

    private Boolean isMultiYearFiler;

    public Boolean getIsMultiYearFiler() {
        return isMultiYearFiler;
    }

    public void setIsMultiYearFiler(Boolean multiYearFiler) {
        isMultiYearFiler = multiYearFiler;
    }

    public AccountingPeriodApi getNextAccount() {
        return nextAccount;
    }

    public void setNextAccount(AccountingPeriodApi nextAccount) {
        this.nextAccount = nextAccount;
    }

    public RptTransaction[] getExistingRptTransactions() {
        return existingRptTransactions;
    }

    public void setExistingRptTransactions(RptTransaction[] existingTransactions) {
        this.existingRptTransactions = existingTransactions;
    }

    public RptTransactionToAdd getRptTransactionToAdd() {
        return rptTransactionToAdd;
    }

    public void setRptTransactionToAdd(RptTransactionToAdd rptTransactionToAdd) {
        this.rptTransactionToAdd = rptTransactionToAdd;
    }
}
