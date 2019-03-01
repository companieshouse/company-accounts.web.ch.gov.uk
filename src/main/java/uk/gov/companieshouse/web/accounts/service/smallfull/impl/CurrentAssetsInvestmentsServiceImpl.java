package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CurrentAssetsInvestmentsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

public class CurrentAssetsInvestmentsServiceImpl implements CurrentAssetsInvestmentsService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private CurrentAssetsInvestmentsTransformer transformer;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ValidationContext validationContext;

    private static final UriTemplate CURRENT_ASSETS_INVESTMENTS_URI =
        new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/current-assets-investments");

    private static final String INVALID_URI_MESSAGE =
        "Invalid URI for current assets investments resource";

    @Override
    public CurrentAssetsInvestments getCurrentAssetsInvestments(String transactionId,
        String companyAccountsId, String companyNumber) throws ServiceException {

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = getCurrentAssetsInvestmentsApi(
            transactionId, companyAccountsId);

        CurrentAssetsInvestments currentAssetsInvestments = transformer.getCurrentAssetsInvestments(
            currentAssetsInvestmentsApi);

        return currentAssetsInvestments;
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

            if(!currentAssetsInvestmentsResourceExists) {
                apiClient.smallFull().currentAssetsInvestments()
                    .create(uri, currentAssetsInvestmentsApi)
                    .execute();
            } else {
                apiClient.smallFull().currentAssetsInvestments()
                    .update(uri, currentAssetsInvestmentsApi)
                    .execute();
            }

        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException(
                        "Bad request when creating current assets investments resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating current assets investments resource", e);
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
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error deleting current assets investments note resource", e);
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
            return apiClient.smallFull().currentAssetsInvestments().get(uri).execute();

        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Error when retrieving current assets investments note", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        }
    }
}
