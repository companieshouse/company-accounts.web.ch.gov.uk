package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Service
public class SmallFullServiceImpl implements SmallFullService {

    @Autowired
    ApiClientService apiClientService;

    private static final UriTemplate SMALL_FULL_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full");

    @Override
    public void createSmallFullAccounts(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = SMALL_FULL_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().create(uri, new SmallFullApi()).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error creating small full accounts", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for small full resource", e);
        }
    }

    @Override
    public SmallFullApi getSmallFullAccounts(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = SMALL_FULL_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().get(uri).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error retrieving small full accounts", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for small full resource", e);
        }
    }
}
