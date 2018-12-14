package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface OtherAccountingPolicyService {

    /**
     * Fetch the Other Accounting Policy
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the other accounting policy
     * @throws ServiceException if there's an error when retrieving the other accounting policy
     */
    OtherAccountingPolicy getOtherAccountingPolicy(String transactionId,
        String companyAccountsId) throws ServiceException;

    /**
     * Submit the Other Accounting Policy
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param otherAccountingPolicy other accounting policy note to submit
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitOtherAccountingPolicy(String transactionId,
        String companyAccountsId, OtherAccountingPolicy otherAccountingPolicy)
        throws ServiceException;
}