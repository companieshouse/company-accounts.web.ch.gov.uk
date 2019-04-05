package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
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

    private static final String INVALID_URI_MESSAGE = "Invalid URI for fixedAssetsInvestments resource";

    private static final String RESOURCE_NAME = "fixed assets investments";

    @Override
    public FixedAssetsInvestments getFixedAssetsInvestments(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException {

        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = getFixedAssetsInvestmentsApi(transactionId, companyAccountsId);
        FixedAssetsInvestments fixedAssetsInvestments = fixedAssetsInvestmentsTransformer.getFixedAssetsInvestments(fixedAssetsInvestmentsApi);

        return fixedAssetsInvestments;
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
            if (!fixedAssetsInvestmentsResourceExists) {
                apiClient.smallFull().fixedAssetsInvestments().create(uri, fixedAssetsInvestmentsApi).execute();
            } else {
                apiClient.smallFull().fixedAssetsInvestments().update(uri, fixedAssetsInvestmentsApi).execute();
            }
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when creating fixedAssetsInvestments resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating fixedAssetsInvestments resource", e);
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
            return apiClient.smallFull().fixedAssetsInvestments().get(uri).execute();
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                return null;
            }
            throw new ServiceException("Error when retrieving fixedAssetsInvestments", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        }
    }

    private boolean hasFixedAssetsInvestments(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getFixedAssetsInvestmentsNote() != null;
    }
}
