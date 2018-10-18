package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface SmallFullService {

    void createSmallFullAccounts(String transactionId, String companyAccountsId) throws ServiceException;

    SmallFullApi getSmallFullAccounts(String transactionId, String companyAccountsId) throws ServiceException;
}
