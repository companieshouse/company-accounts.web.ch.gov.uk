package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;

public interface BalanceSheetService {

    BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId)
            throws ServiceException;

    void postBalanceSheet(String transactionId, String companyAccountsId, BalanceSheet balanceSheet)
            throws ServiceException;
}
