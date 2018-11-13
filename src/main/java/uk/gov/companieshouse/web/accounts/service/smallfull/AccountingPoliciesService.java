package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface AccountingPoliciesService {

    /**
     * Fetches the accounting policies API resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the accounting policies API model
     * @throws ServiceException if there's an error while fetching the resource
     */
    AccountingPoliciesApi getAccountingPoliciesApi(String transactionId, String companyAccountsId)
            throws ServiceException;

    /**
     * Creates the accounting policies API resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error while creating the resource
     */
    List<ValidationError> createAccountingPoliciesApi(String transactionId,
            String companyAccountsId,
            AccountingPoliciesApi accountingPoliciesApi) throws ServiceException;

    /**
     * Updates the accounting policies API resource
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return a list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error while updating the resource
     */
    List<ValidationError> updateAccountingPoliciesApi(String transactionId,
            String companyAccountsId,
            AccountingPoliciesApi accountingPoliciesApi) throws ServiceException;

}
