package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CurrentAssetsInvestmentsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class CurrentAssetsInvestmentsServiceImpl implements CurrentAssetsInvestmentsService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private CurrentAssetsInvestmentsTransformer transformer;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate CURRENT_ASSETS_INVESTMENTS_URI =
        new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/current-assets-investments");

    private static final String RESOURCE_NAME = "current assets investments";

    @Override
    public CurrentAssetsInvestments getCurrentAssetsInvestments(String transactionId,
        String companyAccountsId, String companyNumber) throws ServiceException {

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = getCurrentAssetsInvestmentsApi(
            transactionId, companyAccountsId);

        return transformer.getCurrentAssetsInvestments(currentAssetsInvestmentsApi);
    }

    @Override
    public List<ValidationError> submitCurrentAssetsInvestments(String transactionId,
        String companyAccountsId, CurrentAssetsInvestments currentAssetsInvestments,
        String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CURRENT_ASSETS_INVESTMENTS_URI.expand(transactionId, companyAccountsId).toString();

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = transformer
            .getCurrentAssetsInvestmentsApi(currentAssetsInvestments);

        boolean currentAssetsInvestmentsResourceExists =
            hasCurrentAssetsInvestments(apiClient, transactionId, companyAccountsId);

        try {
            ApiResponse apiResponse;
            if(!currentAssetsInvestmentsResourceExists) {
                apiResponse =
                        apiClient.smallFull().currentAssetsInvestments()
                            .create(uri, currentAssetsInvestmentsApi).execute();
            } else {
                apiResponse =
                        apiClient.smallFull().currentAssetsInvestments()
                                .update(uri, currentAssetsInvestmentsApi).execute();
            }

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
    public void deleteCurrentAssetsInvestments(String transactionId,
        String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CURRENT_ASSETS_INVESTMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().currentAssetsInvestments().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private boolean hasCurrentAssetsInvestments(ApiClient apiClient, String transactionId,
        String companyAccountsId) throws ServiceException {

        SmallFullApi smallFullApi =
            smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        return smallFullApi.getLinks().getCurrentAssetsInvestmentsNote() != null;
    }

    private CurrentAssetsInvestmentsApi getCurrentAssetsInvestmentsApi(String transactionId,
        String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = CURRENT_ASSETS_INVESTMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().currentAssetsInvestments().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }
}
