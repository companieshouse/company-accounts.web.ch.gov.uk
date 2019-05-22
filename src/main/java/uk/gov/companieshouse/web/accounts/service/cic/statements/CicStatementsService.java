package uk.gov.companieshouse.web.accounts.service.cic.statements;

import java.util.List;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface CicStatementsService {

    /**
     * Fetches the cic statements API resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a cic statements API resource
     * @throws ServiceException if there's an error while fetching the resource
     */
    CicStatementsApi getCicStatementsApi(String transactionId, String companyAccountsId) throws ServiceException;

    /**
     * Create a cic statements API resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty list if none are present
     * @throws ServiceException if there's an error while creating the resource
     */
    List<ValidationError> createCicStatementsApi(String transactionId, String companyAccountsId, CicStatementsApi cicStatementsApi)
            throws ServiceException;

    /**
     * Update a cic statements API resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty list if none are present
     * @throws ServiceException if there's an error while updating the resource
     */
    List<ValidationError> updateCicStatementsApi(String transactionId, String companyAccountsId, CicStatementsApi cicStatementsApi)
            throws ServiceException;
}
