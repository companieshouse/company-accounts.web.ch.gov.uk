package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.FixedAssetsInvestmentsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Service
public class FixedAssetsInvestmentsServiceImpl implements FixedAssetsInvestmentsService {

    @Autowired
    private FixedAssetsInvestmentsTransformer fixedAssetsInvestmentsTransformer;

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final UriTemplate FIXED_ASSETS_INVESTMENTS_URI =
        new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/fixed-assets-investments");

    private static final String RESOURCE_NAME = "fixed assets investments";

    @Override
    public FixedAssetsInvestments getFixedAssetsInvestments(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException {

        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = getFixedAssetsInvestmentsApi(transactionId, companyAccountsId);

        return fixedAssetsInvestmentsTransformer.getFixedAssetsInvestments(fixedAssetsInvestmentsApi);
    }

    @Override
    public List<ValidationError> submitFixedAssetsInvestments(String transactionId, String companyAccountsId,
                                              FixedAssetsInvestments fixedAssetsInvestments, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = FIXED_ASSETS_INVESTMENTS_URI.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = fixedAssetsInvestmentsTransformer.getFixedAssetsInvestmentsApi(fixedAssetsInvestments);

        boolean fixedAssetsInvestmentsResourceExists = hasFixedAssetsInvestments(smallFullApi.getLinks());

        try {
            ApiResponse apiResponse;
            if (!fixedAssetsInvestmentsResourceExists) {
                apiResponse = apiClient.smallFull().fixedAssetsInvestments().create(uri, fixedAssetsInvestmentsApi).execute();
            } else {
                apiResponse = apiClient.smallFull().fixedAssetsInvestments().update(uri, fixedAssetsInvestmentsApi).execute();
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
    public void deleteFixedAssetsInvestments(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = FIXED_ASSETS_INVESTMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().fixedAssetsInvestments().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private FixedAssetsInvestmentsApi getFixedAssetsInvestmentsApi(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = FIXED_ASSETS_INVESTMENTS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().fixedAssetsInvestments().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }
        return null;
    }

    private boolean hasFixedAssetsInvestments(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getFixedAssetsInvestmentsNote() != null;
    }
}
