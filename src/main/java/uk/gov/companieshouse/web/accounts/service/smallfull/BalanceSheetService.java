package uk.gov.companieshouse.web.accounts.service.smallfull;


import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;

public interface BalanceSheetService {

    BalanceSheet getBalanceSheet(String transactionId, String companyAccountsId);
}
