package uk.gov.companieshouse.web.accounts.service.company;

import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;

public interface CompanyService {

    CompanyProfileApi getCompanyProfile(String companyNumber) throws ApiErrorResponseException;
}
