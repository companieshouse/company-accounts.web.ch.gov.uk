package uk.gov.companieshouse.web.accounts.service.companyaccounts;

import java.time.LocalDate;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface CompanyAccountsService {

    String createCompanyAccounts(String transactionId, LocalDate periodEndOn) throws ServiceException;
}
