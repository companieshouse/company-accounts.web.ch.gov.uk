package uk.gov.companieshouse.web.accounts.service.companyaccounts;

import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;

public interface CompanyAccountsService {

    CompanyAccountsApi createCompanyAccounts(String transactionId, CompanyAccountsApi companyAccounts) throws ApiErrorResponseException;
}
