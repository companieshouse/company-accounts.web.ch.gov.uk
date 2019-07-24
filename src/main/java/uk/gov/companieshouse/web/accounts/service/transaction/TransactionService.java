package uk.gov.companieshouse.web.accounts.service.transaction;

import java.util.Optional;
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
     * Closes an accounts filing transaction
     *
     * @param transactionId The ID of the transaction to close
     * @throws ServiceException
     * @return an {@link boolean} indicating whether a transaction is payable
     */
    boolean closeTransaction(String transactionId) throws ServiceException;

    /**
     * Updates a resume link for a transaction. Used for resuming the web journey at
     * a later time.
     *
     * @param transactionId     the ID of the transaction to update
     * @param companyAccountsId the company accounts ID of the transaction to update
     * @throws ServiceException
     */
    void updateResumeLink(String transactionId, String resumeLink) throws ServiceException;

    /**
     * Determine whether a transaction is payable
     *
     * @param transactionId     the ID of the CHS transaction
     * @param companyAccountsId the company accounts identifier
     * @return true if the transaction is payable
     * @throws ServiceException if there's an error when fetching a transaction
     */
    boolean isPayableTransaction(String transactionId, String companyAccountsId) throws ServiceException;
}
