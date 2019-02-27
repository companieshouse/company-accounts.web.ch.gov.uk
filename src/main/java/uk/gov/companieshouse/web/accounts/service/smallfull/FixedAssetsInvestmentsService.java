package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface FixedAssetsInvestmentsService {

    /**
     * Fetch fixedAssetsInvestments note
     *
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber     The company identifier
     * @return the fixedAssetsInvestments note
     */
    FixedAssetsInvestments getFixedAssetsInvestments(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException;

    /**
     * Submit the fixedAssetsInvestments note
     *
     * @param transactionId     The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param fixedAssetsInvestments fixedAssetsInvestments note to submit
     * @param companyNumber     The company number
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitFixedAssetsInvestments(String transactionId, String companyAccountsId, FixedAssetsInvestments fixedAssetsInvestments, String companyNumber)
        throws ServiceException;

 }
