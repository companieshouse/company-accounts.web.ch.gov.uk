package uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors;

import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;

public interface LoanTransformer {

    LoanApi getLoanApi(LoanToAdd loanToAdd);

    Loan[] getAllLoans(LoanApi[] Loans);
}
