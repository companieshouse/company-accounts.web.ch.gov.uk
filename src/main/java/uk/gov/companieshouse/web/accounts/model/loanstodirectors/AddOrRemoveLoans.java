package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

@ValidationModel
public class AddOrRemoveLoans {

    private Loan[] existingLoans;

    private LoanToAdd loanToAdd;

    private AccountingPeriodApi nextAccounts;

    public Loan[] getExistingLoans() {
        return existingLoans;
    }

    public void setExistingLoans(Loan[] existingLoans) {
        this.existingLoans = existingLoans;
    }

    public LoanToAdd getLoanToAdd() {
        return loanToAdd;
    }

    public void setLoanToAdd(LoanToAdd loanToAdd) {
        this.loanToAdd = loanToAdd;
    }

    public AccountingPeriodApi getNextAccounts() {
        return nextAccounts;
    }

    public void setNextAccounts(AccountingPeriodApi nextAccounts) {
        this.nextAccounts = nextAccounts;
    }
}
