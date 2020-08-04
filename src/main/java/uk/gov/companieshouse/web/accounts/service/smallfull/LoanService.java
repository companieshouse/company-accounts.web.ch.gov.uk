package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface LoanService {

    void deleteLoan(String transactionId, String companyAccountsId, String loanId) throws ServiceException;
}
