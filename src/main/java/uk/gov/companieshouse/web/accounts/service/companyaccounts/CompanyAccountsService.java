package uk.gov.companieshouse.web.accounts.service.companyaccounts;

import java.util.Date;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;

public interface CompanyAccountsService {

    CompanyAccountsApi createCompanyAccounts(String transactionId, Date periodEndOn) throws ApiErrorResponseException;
}
