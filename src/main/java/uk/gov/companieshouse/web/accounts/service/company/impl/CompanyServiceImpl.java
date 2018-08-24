package uk.gov.companieshouse.web.accounts.service.company.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    ApiClientService apiClientService;

    @Override
    public CompanyProfileApi getCompanyProfile(String companyNumber) throws ApiErrorResponseException {

        ApiClient apiClient = apiClientService.getApiClient();

        return apiClient.company(companyNumber).get();
    }
}
