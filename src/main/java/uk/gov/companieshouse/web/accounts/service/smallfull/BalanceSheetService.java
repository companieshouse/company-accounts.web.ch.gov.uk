package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

import java.util.List;

public interface BalanceSheetService {

    BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> postBalanceSheet(String transactionId, String companyAccountsId, BalanceSheet balanceSheet, String companyNumber)
            throws ServiceException;

    BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfile);
}
