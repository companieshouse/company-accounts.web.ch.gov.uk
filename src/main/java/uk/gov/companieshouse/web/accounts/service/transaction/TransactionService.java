package uk.gov.companieshouse.web.accounts.service.transaction;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface TransactionService {

    String createTransaction(String companyNumber) throws ServiceException;

    void closeTransaction(String transactionId) throws ServiceException;
}
