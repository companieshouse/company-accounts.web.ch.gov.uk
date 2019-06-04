package uk.gov.companieshouse.web.accounts.service.cic;


import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.cic.CicApproval;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface CicApprovalService {

    /**
     * Submit an approval model to the company accounts api
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param approval Approval to submit
     * @return A list of validation errors. This will be empty for a successful submission
     * @throws ServiceException on submission failure
     */
    List<ValidationError> submitCicApproval(String transactionId, String companyAccountsId,
        CicApproval cicApproval)
        throws ServiceException;

    /**
     * Validate the provided approval date
     *
     * @param approval The approval to be submitted
     * @return a list of validation errors, or an empty list if none are present
     */
    List<ValidationError> validateCicApprovalDate(CicApproval cicAproval);

    /**
     * Get CicApproval object if it exists
     * @param transactionId
     * @param companyAccountsId
     * @return
     * @throws ServiceException
     */
    CicApproval getCicApproval(String transactionId, String companyAccountsId) throws ServiceException;
}

