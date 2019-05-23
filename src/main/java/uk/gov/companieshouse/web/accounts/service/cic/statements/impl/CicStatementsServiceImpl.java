package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class CicStatementsServiceImpl implements CicStatementsService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate CIC_STATEMENTS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/cic-report/cic-statements");

    private static final String RESOURCE_NAME = "CIC statements";

    /**
     * {@inheritDoc}
     */
    @Override
    public CicStatementsApi getCicStatementsApi(String transactionId, String companyAccountsId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CIC_STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.cicReport().statements().get(uri).execute().getData();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> createCicStatementsApi(String transactionId,
            String companyAccountsId, CicStatementsApi cicStatementsApi) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CIC_STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            ApiResponse<CicStatementsApi> apiResponse =
                    apiClient.cicReport().statements().create(uri, cicStatementsApi).execute();

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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> updateCicStatementsApi(String transactionId,
            String companyAccountsId, CicStatementsApi cicStatementsApi) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CIC_STATEMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            ApiResponse<Void> apiResponse =
                    apiClient.cicReport().statements().update(uri, cicStatementsApi).execute();

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
}
