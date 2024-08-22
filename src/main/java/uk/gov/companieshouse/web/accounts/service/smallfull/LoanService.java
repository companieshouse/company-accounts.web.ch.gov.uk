package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface LoanService {

    Loan[] getAllLoans(String transactionId, String companyAccountsId) throws ServiceException;

    List<ValidationError> createLoan(String transactionId, String companyAccountsId, AddOrRemoveLoans addOrRemoveLoans) throws ServiceException;

    void deleteLoan(String transactionId, String companyAccountsId, String loanId) throws ServiceException;

    List<ValidationError> submitAddOrRemoveLoans(String transactionId, String companyAccountsId, AddOrRemoveLoans addOrRemoveLoans) throws  ServiceException;
}
