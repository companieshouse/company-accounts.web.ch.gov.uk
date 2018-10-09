package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Review;
import uk.gov.companieshouse.web.accounts.service.smallfull.ReviewService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.ReviewTransformer;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewTransformer transformer;

    @Autowired
    ApiClientService apiClientService;

    private static final UriTemplate CURRENT_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/current-period");

    public Review getReview(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        CurrentPeriodApi currentPeriod;

        String uri = CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString();

        try {
            currentPeriod = apiClient.smallFull().currentPeriod().get(uri).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error retrieving current period resource", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for current period resource", e);
        }

        return transformer.getReview(currentPeriod);
    }
}
