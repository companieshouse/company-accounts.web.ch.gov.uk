package uk.gov.companieshouse.web.accounts.model.loanstodirectors;

public class AddOrRemoveLoans {

    private Loan[] existingLoans;

    private LoanToAdd loanToAdd;

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
}
