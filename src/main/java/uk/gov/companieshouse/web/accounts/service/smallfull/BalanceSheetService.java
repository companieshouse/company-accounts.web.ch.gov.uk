package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

public interface BalanceSheetService {

    BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId)
            throws ServiceException;

    void postBalanceSheet(String transactionId, String companyAccountsId, BalanceSheet balanceSheet)
            throws ServiceException;

    BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfile);
}
