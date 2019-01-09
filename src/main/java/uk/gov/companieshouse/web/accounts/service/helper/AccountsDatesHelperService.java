package uk.gov.companieshouse.web.accounts.service.helper;

import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;

public interface AccountsDatesHelperService {

    BalanceSheetHeadings getBalanceSheetHeadings(CompanyProfileApi companyProfile);

}
