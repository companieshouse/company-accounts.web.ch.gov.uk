package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class PreviousPeriodServiceImpl implements PreviousPeriodService {

    private static final UriTemplate PREVIOUS_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/previous-period");

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final String PREVIOUS_PERIOD_RESOURCE = "previous period";

    @Override
    public PreviousPeriodApi getPreviousPeriod(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {

        try {
            return apiClient.smallFull().previousPeriod()
                    .get(PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId).toString())
                            .execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, PREVIOUS_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, PREVIOUS_PERIOD_RESOURCE);
        }

        return null;
    }
}