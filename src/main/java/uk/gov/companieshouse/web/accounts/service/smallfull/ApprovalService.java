package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Approval;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface ApprovalService {

    List<ValidationError> submitApproval(String transactionId, String companyAccountsId,
            Approval approval)
        throws ServiceException;
}
