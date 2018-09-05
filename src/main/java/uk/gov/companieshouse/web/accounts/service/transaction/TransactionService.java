package uk.gov.companieshouse.web.accounts.service.transaction;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface TransactionService {

    /**
     * Create a CHS transaction record. A transaction is comprised of one or more filing resources.
     * Once the status is set to 'closed', the transaction filings will be processed by CHIPS.
     *
     * @param companyNumber The company number for which accounts are to be filed
     * @return The ID of the created transaction
     * @throws ServiceException
     */
    String createTransaction(String companyNumber) throws ServiceException;

    /**
     * Set the status to 'closed' for an accounts filing transaction
     *
     * @param transactionId The ID of the transaction to close
     * @throws ServiceException
     */
    void closeTransaction(String transactionId) throws ServiceException;
}
