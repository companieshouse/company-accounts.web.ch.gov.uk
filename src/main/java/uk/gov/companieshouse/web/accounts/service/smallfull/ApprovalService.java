package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface ApprovalService {
    /**
     * Submit an approval model to the company accounts api
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param approval Approval to submit
     * @return A list of validation errors. This will be empty for a successful submission
     * @throws ServiceException on submission failure
     */
    List<ValidationError> submitApproval(String transactionId, String companyAccountsId,
            Approval approval)
        throws ServiceException;

    Approval getApproval(String transactionId, String companyAccountsId) throws ServiceException;
}
