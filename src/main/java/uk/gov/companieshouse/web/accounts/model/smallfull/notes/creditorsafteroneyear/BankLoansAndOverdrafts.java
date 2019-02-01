package uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorsafteroneyear;

import lombok.Getter;
import lombok.Setter;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;

@Getter
@Setter
public class BankLoansAndOverdrafts {

    @ValidationMapping("$.creditors_after_one_year.current_period.bank_loans_and_overdrafts")
    private Long currentBankLoansAndOverdrafts;

    @ValidationMapping("$.creditors_after_one_year.previous_period.bank_loans_and_overdrafts")
    private Long previousBankLoansAndOverdrafts;
}
