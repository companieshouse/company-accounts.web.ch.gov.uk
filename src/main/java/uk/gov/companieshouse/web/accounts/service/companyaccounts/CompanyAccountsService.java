package uk.gov.companieshouse.web.accounts.service.companyaccounts;

import com.google.api.client.util.DateTime;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface CompanyAccountsService {

    String createCompanyAccounts(String transactionId, DateTime periodEndOn) throws ServiceException;

    void createSmallFullAccounts(String transactionId, String companyAccountsId) throws ServiceException;
}
