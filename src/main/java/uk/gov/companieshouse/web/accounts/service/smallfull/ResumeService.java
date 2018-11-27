package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface ResumeService {

    String getResumeRedirect(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException;
}
