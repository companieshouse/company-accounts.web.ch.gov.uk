package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorsReportApproval;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface DirectorsReportApprovalService {

    List<ValidationError> submitDirectorsReportApproval(String transactionId, String companyAccountsId,
                                                        DirectorsReportApproval directorsReportApproval) throws ServiceException;

    DirectorsReportApproval getDirectorsReportApproval(String transactionId, String companyAccountsId) throws ServiceException;
}

