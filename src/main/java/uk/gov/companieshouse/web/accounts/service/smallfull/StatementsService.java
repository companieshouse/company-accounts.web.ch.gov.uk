package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;

public interface StatementsService {
    /**
     * Create a balance sheet statements resource with the 'has_agreed_to_legal_statements' flag set to false
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException when failing to create the resource
     */
    void createBalanceSheetStatementsResource(String transactionId, String companyAccountsId)
            throws ServiceException;

    /**
     * Update the balance sheet statements resource setting the 'has_agreed_to_legal_statements' flag to true
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException when failing to update the resource
     */
    void acceptBalanceSheetStatements(String transactionId, String companyAccountsId)
            throws ServiceException;

    /**
     * Retrieve the balance sheet statements resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException when failing to fetch the resource
     */
    Statements getBalanceSheetStatements(String transactionId, String companyAccountsId)
            throws ServiceException;

}
