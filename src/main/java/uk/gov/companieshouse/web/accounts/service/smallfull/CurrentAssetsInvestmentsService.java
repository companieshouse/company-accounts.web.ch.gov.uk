package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface CurrentAssetsInvestmentsService {

    /**
     * Fetch the current assets investments note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber The company identifier
     * @return the current assets investments note
     * @throws ServiceException if there's an error when retrieving the current assets investments note
     */
    CurrentAssetsInvestments getCurrentAssetsInvestments(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException;

    /**
     * Submit the current assets investments note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param currentAssetsInvestments current assets investments note to submit
     * @param companyNumber The company identifier
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitCurrentAssetsInvestments(String transactionId, String companyAccountsId, CurrentAssetsInvestments currentAssetsInvestments, String companyNumber)
        throws  ServiceException;

    /**
     * Delete the current assets investments note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException if there's an error on deletion
     */
    void deleteCurrentAssetsInvestments(String transactionId, String companyAccountsId) throws ServiceException;
}
