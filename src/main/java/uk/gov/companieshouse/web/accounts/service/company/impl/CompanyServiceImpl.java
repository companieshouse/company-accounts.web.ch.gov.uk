package uk.gov.companieshouse.web.accounts.service.company.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    ApiClientService apiClientService;

    private static final UriTemplate GET_COMPANY_URI =
            new UriTemplate("/company/{companyNumber}");

    @Override
    public CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CompanyProfileApi companyProfileApi;

        String uri = GET_COMPANY_URI.expand(companyNumber).toString();

        try {
            companyProfileApi = apiClient.company().get(uri).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error retieving company profile", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for company resource", e);
        }

        return companyProfileApi;
    }
}
