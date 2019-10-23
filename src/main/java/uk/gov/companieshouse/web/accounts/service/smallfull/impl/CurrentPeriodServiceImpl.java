package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class CurrentPeriodServiceImpl implements CurrentPeriodService {

    private static final UriTemplate CURRENT_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/current-period");

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final String CURRENT_PERIOD_RESOURCE = "current period";

    @Override
    public CurrentPeriodApi getCurrentPeriod(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {

        try {
            return apiClient.smallFull().currentPeriod()
                    .get(CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString())
                            .execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, CURRENT_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, CURRENT_PERIOD_RESOURCE);
        }

        return null;
    }
}
