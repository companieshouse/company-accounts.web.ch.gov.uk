package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.Debtors;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface DebtorsService {

    /**
     * Fetch debtors note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber The company identifier
     * @return the debtros note
     * @throws ServiceException if there's an error when retrieving the debtors note
     */
    Debtors getDebtors(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException;

    /**
     * Submit the debtors note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param debtors tangible depreciation policy note to submit
     * @param companyNumber The company number
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
        */
    List<ValidationError> submitDebtors(String transactionId, String companyAccountsId, Debtors debtors, String companyNumber)
        throws ServiceException;
}
