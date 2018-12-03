package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface IntangibleAmortisationPolicyService {

    /**
     * Fetch the intangible amortisation policy
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the intangible amortisation policy
     * @throws ServiceException if there's an error when retrieving the intangible amortisation policy
     */
    IntangibleAmortisationPolicy getIntangibleAmortisationPolicy(String transactionId,
            String companyAccountsId) throws ServiceException;

    /**
     * Submit an intangible amortisation policy
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param intangibleAmortisationPolicy Intangible amortisation policy to submit
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitIntangibleAmortisationPolicy(String transactionId,
            String companyAccountsId, IntangibleAmortisationPolicy intangibleAmortisationPolicy) throws ServiceException;

}
