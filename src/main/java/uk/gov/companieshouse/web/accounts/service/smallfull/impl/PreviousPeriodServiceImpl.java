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
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class PreviousPeriodServiceImpl implements PreviousPeriodService {
    private static final UriTemplate PREVIOUS_PERIOD_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/previous-period");

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final String PREVIOUS_PERIOD_RESOURCE = "previous period";

    @Override
    public PreviousPeriodApi getPreviousPeriod(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {
        try {
            return apiClient.smallFull().previousPeriod()
                    .get(PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId).toString())
                            .execute().getData();
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error retrieving resource: " + PREVIOUS_PERIOD_RESOURCE, e);
        } catch (URIValidationException e) {
            throw new ServiceException("Invalid URI for resource: " + PREVIOUS_PERIOD_RESOURCE, e);
        }
    }

    @Override
    public void submitPreviousPeriod(ApiClient apiClient, SmallFullApi smallFullApi, String transactionId,
            String companyAccountsId, PreviousPeriodApi previousPeriodApi, List<ValidationError> validationErrors) throws ServiceException {
        boolean resourceExists = StringUtils.isNotBlank(smallFullApi.getLinks().getPreviousPeriod());

        try {
            ApiResponse apiResponse;
            if (!resourceExists) {
                apiResponse = apiClient.smallFull().previousPeriod()
                        .create(PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId).toString(),
                                previousPeriodApi).execute();
            } else {
                apiResponse = apiClient.smallFull().previousPeriod()
                        .update(PREVIOUS_PERIOD_URI.expand(transactionId, companyAccountsId).toString(),
                                previousPeriodApi).execute();
            }

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, PREVIOUS_PERIOD_RESOURCE);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, PREVIOUS_PERIOD_RESOURCE);
        }
    }
}
