package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface BasisOfPreparationService {

    /**
     * Submit the basis of accounts preparation
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param basisOfPreparation Basis of preparation note to submit
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitBasisOfPreparation(String transactionId, String companyAccountsId,
            BasisOfPreparation
                    basisOfPreparation) throws ServiceException;

    /**
     * Fetch the basis of accounts preparation
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the basis of accounts preparation
     * @throws ServiceException if there's an error when retrieving the basis of accounts preparation
     */
    BasisOfPreparation getBasisOfPreparation(String transactionId, String companyAccountsId)
            throws ServiceException;

}
