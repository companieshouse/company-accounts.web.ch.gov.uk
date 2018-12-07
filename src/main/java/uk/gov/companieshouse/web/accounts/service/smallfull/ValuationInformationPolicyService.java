package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface ValuationInformationPolicyService {

    /**
     * Fetch the valuation information policy.
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return The valuation information policy
     * @throws ServiceException if there's an error when retrieving the valuation information policy
     */
    ValuationInformationPolicy getValuationInformationPolicy(String transactionId,
            String companyAccountsId) throws ServiceException;

    /**
     * Submit an valuation information policy.
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param valuationInformationPolicy Valuation information policy to submit
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitValuationInformationPolicy(String transactionId,
            String companyAccountsId, ValuationInformationPolicy valuationInformationPolicy) throws ServiceException;

}
