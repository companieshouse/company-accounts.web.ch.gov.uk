package uk.gov.companieshouse.web.accounts.service.companyaccounts.impl;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.accounts.CompanyAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.service.companyaccounts.CompanyAccountsService;

public class CompanyAccountsServiceImpl implements CompanyAccountsService {

    @Autowired
    ApiClientService apiClientService;

    @Override
    public CompanyAccountsApi createCompanyAccounts(String transactionId, CompanyAccountsApi companyAccounts) throws ApiErrorResponseException {

        ApiClient apiClient = apiClientService.getApiClient();

        return apiClient.transaction(transactionId).companyAccounts().create(companyAccounts);
    }
}
