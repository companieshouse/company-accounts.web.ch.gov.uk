package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface TangibleDepreciationPolicyService {

    /**
     * Fetch the tangible policy
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the tangible depreciation policy
     * @throws ServiceException if there's an error when retrieving the tangible depreciation
     * policy
     */
    TangibleDepreciationPolicy getTangibleDepreciationPolicy(String transactionId,
        String companyAccountsId) throws ServiceException;

    /**
     * Submit the tangible depreciation policy
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param tangibleDepreciationPolicy tangible depreciation policy note to submit
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> postTangibleDepreciationPolicy(String transactionId,
        String companyAccountsId, TangibleDepreciationPolicy tangibleDepreciationPolicy)
        throws ServiceException;

}