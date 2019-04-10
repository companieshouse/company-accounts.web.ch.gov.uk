package uk.gov.companieshouse.web.accounts.service.companyaccounts;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface CompanyAccountsService {

    String createCompanyAccounts(String transactionId) throws ServiceException;
}
