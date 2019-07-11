package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

public class BankLoansAndOverdrafts {

    @ValidationMapping("$.creditors_after_one_year.current_period.bank_loans_and_overdrafts")
    private Long currentBankLoansAndOverdrafts;

    @ValidationMapping("$.creditors_after_one_year.previous_period.bank_loans_and_overdrafts")
    private Long previousBankLoansAndOverdrafts;

    public Long getCurrentBankLoansAndOverdrafts() {
        return currentBankLoansAndOverdrafts;
    }

    public void setCurrentBankLoansAndOverdrafts(Long currentBankLoansAndOverdrafts) {
        this.currentBankLoansAndOverdrafts = currentBankLoansAndOverdrafts;
    }

    public Long getPreviousBankLoansAndOverdrafts() {
        return previousBankLoansAndOverdrafts;
    }

    public void setPreviousBankLoansAndOverdrafts(Long previousBankLoansAndOverdrafts) {
        this.previousBankLoansAndOverdrafts = previousBankLoansAndOverdrafts;
    }
}
