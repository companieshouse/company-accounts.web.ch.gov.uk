package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportLinks;
import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SecretaryService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.SecretaryTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class SecretaryServiceImpl implements SecretaryService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SecretaryTransformer secretaryTransformer;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private DirectorsReportService directorsReportService;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate SECRETARY_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/secretary");

    private static final String RESOURCE_NAME = "secretaries";

    @Override
    public String getSecretary(String transactionId, String companyAccountsId)
        throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = SECRETARY_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().directorsReport().secretary().get(uri).execute().getData().getName();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public List<ValidationError> submitSecretary(String transactionId, String companyAccountsId,
                                                 AddOrRemoveDirectors addOrRemoveDirectors)
            throws ServiceException {

        List<ValidationError> validationErrors = new ArrayList<>();

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = SECRETARY_URI.expand(transactionId, companyAccountsId).toString();
        SecretaryApi secretaryApi = secretaryTransformer.getSecretaryApi(addOrRemoveDirectors);

        try {
            ApiResponse apiResponse;
            if (hasSecretary(directorsReportService.getDirectorsReport(apiClient, transactionId, companyAccountsId).getLinks())) {

                apiResponse = apiClient.smallFull().directorsReport().secretary().update(uri, secretaryApi).execute();
            } else {

                apiResponse = apiClient.smallFull().directorsReport().secretary().create(uri, secretaryApi).execute();
            }

                if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);

        } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);

        }

        return validationErrors;
    }

    @Override
    public void deleteSecretary(String transactionId, String companyAccountsId)
        throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        if (hasSecretary(directorsReportService.
                getDirectorsReport(apiClient, transactionId, companyAccountsId).getLinks())) {

            String uri = SECRETARY_URI.expand(transactionId, companyAccountsId).toString();

            try {
                apiClient.smallFull().directorsReport().secretary().delete(uri).execute();
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
            }
        }
    }

    private boolean hasSecretary(DirectorsReportLinks directorsReportLinks) {

        return directorsReportLinks.getSecretary() != null;
    }
}
