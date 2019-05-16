package uk.gov.companieshouse.web.accounts.service.transaction;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface TransactionService {

    /**
     * Create a CHS transaction record
     *
     * @param companyNumber The company number for which accounts are to be filed against the transaction
     * @return The ID of the created transaction
     * @throws ServiceException
     */
    String createTransaction(String companyNumber) throws ServiceException;

    String createTransactionWithDescription(String companyNumber, String description) throws ServiceException;

    /**
     * Set the status to 'closed' for an accounts filing transaction
     *
     * @param transactionId The ID of the transaction to close
     * @throws ServiceException
     */
    void closeTransaction(String transactionId) throws ServiceException;

    /**
     * Add a resume link to the transaction for resuming the web journey at
     * a later time.
     *
     * @param companyNumber     the company number
     * @param transactionId     the ID of the transaction to update
     * @param companyAccountsId the company accounts ID of the transaction to update
     * @throws ServiceException
     */
    void createResumeLink(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException;
}
