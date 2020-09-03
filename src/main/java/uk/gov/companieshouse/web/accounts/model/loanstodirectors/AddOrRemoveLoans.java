package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;

import java.util.List;

@ValidationModel
public class AddOrRemoveLoans {

    private Loan[] existingLoans;

    private LoanToAdd loanToAdd;

    private AccountingPeriodApi nextAccount;

    private List<String> validDirectorNames;

    public AccountingPeriodApi getNextAccount() {
        return nextAccount;
    }

    public void setNextAccount(AccountingPeriodApi nextAccount) {
        this.nextAccount = nextAccount;
    }

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

    public List<String> getValidDirectorNames() {
        return validDirectorNames;
    }

    public void setValidDirectorNames(List<String> validDirectorNames) {
        this.validDirectorNames = validDirectorNames;
    }
}
