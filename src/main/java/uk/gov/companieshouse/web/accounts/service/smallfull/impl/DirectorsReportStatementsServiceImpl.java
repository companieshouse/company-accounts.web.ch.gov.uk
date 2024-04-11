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
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class DirectorsReportStatementsServiceImpl implements DirectorsReportStatementsService {
    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate DIRECTORS_REPORT_STATEMENTS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/statements");

    private static final String RESOURCE_NAME = "directors report statements";

    @Override
    public StatementsApi getDirectorsReportStatements(String transactionId,
                                                      String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DIRECTORS_REPORT_STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().directorsReport().statements().get(uri).execute().getData();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public List<ValidationError> createDirectorsReportStatements(String transactionId,
                                                                 String companyAccountsId,
                                                                 StatementsApi statementsApi)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DIRECTORS_REPORT_STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            ApiResponse<StatementsApi> apiResponse =
                    apiClient.smallFull().directorsReport().statements().create(uri, statementsApi).execute();

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
    public List<ValidationError> updateDirectorsReportStatements(String transactionId,
                                                                 String companyAccountsId,
                                                                 StatementsApi statementsApi)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DIRECTORS_REPORT_STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            ApiResponse<Void> apiResponse =
                    apiClient.smallFull().directorsReport().statements().update(uri, statementsApi).execute();

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
    public void deleteDirectorsReportStatements(String transactionId,
                                                String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DIRECTORS_REPORT_STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().directorsReport().statements().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }
}
