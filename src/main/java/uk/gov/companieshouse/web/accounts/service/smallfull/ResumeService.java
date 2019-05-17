package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface ResumeService {

    /**
     * Return a redirect to continue the web journey for a given transaction.
     *
     * @param companyNumber     the company number
     * @param transactionId     the transaction identifier
     * @param companyAccountsId the company accounts identifier
     */
    String getResumeRedirect(String companyNumber, String transactionId, String companyAccountsId)  throws ServiceException;
}
