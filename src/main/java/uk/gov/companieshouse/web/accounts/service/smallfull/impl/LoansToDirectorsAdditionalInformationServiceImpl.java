package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInformationService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoansToDirectorsAdditionalInformationServiceImpl implements LoansToDirectorsAdditionalInformationService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate ADDITIONAL_INFORMATION_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors/additional-information");

    private static final String RESOURCE_NAME = "loans to directors additional information";

    @Override
    public AdditionalInformationApi getAdditionalInformation(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = ADDITIONAL_INFORMATION_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().loansToDirectors().additionalInformation().get(uri).execute().getData();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public List<ValidationError> createAdditionalInformation(String transactionId, String companyAccountsId, AdditionalInformationApi additionalInformationApi) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = ADDITIONAL_INFORMATION_URI.expand(transactionId, companyAccountsId).toString();

        try {
            ApiResponse<AdditionalInformationApi> apiResponse =
                    apiClient.smallFull().loansToDirectors().additionalInformation().create(uri, additionalInformationApi).execute();

            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    @Override
    public List<ValidationError> updateAdditionalInformation(String transactionId, String companyAccountsId, AdditionalInformationApi additionalInformationApi) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = ADDITIONAL_INFORMATION_URI.expand(transactionId, companyAccountsId).toString();

        try {
            ApiResponse<Void> apiResponse =
                    apiClient.smallFull().loansToDirectors().additionalInformation().update(uri, additionalInformationApi).execute();

            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    @Override
    public void deleteAdditionalInformation(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        if (StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getLoansToDirectors())) {
            String uri = ADDITIONAL_INFORMATION_URI.expand(transactionId, companyAccountsId).toString();

            try {
                apiClient.smallFull().loansToDirectors().additionalInformation().delete(uri).execute();
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
            }
        }
    }
}
