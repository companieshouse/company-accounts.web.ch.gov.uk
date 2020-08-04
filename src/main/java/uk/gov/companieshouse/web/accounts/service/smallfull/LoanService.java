package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface LoanService {

    Loan[] getAllLoans(String transactionId, String companyAccountsId) throws ServiceException;

    List<ValidationError> createLoan(String transactionId, String companyAccountsId, LoanToAdd loanToAdd) throws ServiceException;

    void deleteLoan(String transactionId, String companyAccountsId, String loanId) throws ServiceException;

    List<ValidationError> submitAddOrRemoveLoans(String transactionId, String companyAccountsId, AddOrRemoveLoans addOrRemoveLoans) throws  ServiceException;

}
