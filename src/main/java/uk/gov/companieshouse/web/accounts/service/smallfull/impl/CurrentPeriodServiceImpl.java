package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class CurrentPeriodServiceImpl implements CurrentPeriodService {
    private static final UriTemplate CURRENT_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/current-period");

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final String CURRENT_PERIOD_RESOURCE = "current period";

    @Override
    public CurrentPeriodApi getCurrentPeriod(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {
        try {
            return apiClient.smallFull().currentPeriod()
                    .get(CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString())
                            .execute().getData();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error retrieving resource: " + CURRENT_PERIOD_RESOURCE, e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for resource: " + CURRENT_PERIOD_RESOURCE, e);
        }
    }

    @Override
    public void submitCurrentPeriod(ApiClient apiClient, SmallFullApi smallFullApi, String transactionId,
            String companyAccountsId, CurrentPeriodApi currentPeriodApi, List<ValidationError> validationErrors) throws ServiceException {
        boolean resourceExists = StringUtils.isNotBlank(smallFullApi.getLinks().getCurrentPeriod());

        try {
            ApiResponse apiResponse;
            if (!resourceExists) {
                apiResponse = apiClient.smallFull().currentPeriod()
                        .create(CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString(),
                                currentPeriodApi).execute();
            } else {
                apiResponse = apiClient.smallFull().currentPeriod()
                        .update(CURRENT_PERIOD_URI.expand(transactionId, companyAccountsId).toString(),
                                currentPeriodApi).execute();
            }

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, CURRENT_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, CURRENT_PERIOD_RESOURCE);
        }
    }
}
