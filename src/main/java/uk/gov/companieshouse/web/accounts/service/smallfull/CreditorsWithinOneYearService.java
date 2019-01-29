package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.creditorswithinoneyear.CreditorsWithinOneYear;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface CreditorsWithinOneYearService {

    /**
     * Fetch creditors within one year note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber The company identifier
     * @return the creditors within one year note
     * @throws ServiceException if there's an error when retrieving the debtors note
     */
    CreditorsWithinOneYear getCreditorsWithinOneYear(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException;

    /**
     * Submit the creditors within one year note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param creditors within one year note to submit
     * @param companyNumber The company number
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
        */
    List<ValidationError> submitCreditorsWithinOneYear(String transactionId, String companyAccountsId, CreditorsWithinOneYear creditorsWithinOneYear, String companyNumber)
        throws ServiceException;
    
    /**
     * Delete the creditors within one year note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on deletion
     */
    List<ValidationError> deleteCreditorsWithinOneYear(String transactionId, String companyAccountsId) throws ServiceException;
    
}
