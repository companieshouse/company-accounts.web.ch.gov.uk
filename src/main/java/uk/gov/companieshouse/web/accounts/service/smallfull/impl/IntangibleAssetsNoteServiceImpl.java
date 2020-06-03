package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IntangibleAssetsNoteServiceImpl implements IntangibleAssetsNoteService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private IntangibleAssetsTransformer intangibleAssetsTransformer;

    private static final UriTemplate INTANGIBLE_ASSET_NOTE =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/intangible-assets");

    private static final String RESOURCE_NAME = "intangible assets";


    @Override
    public IntangibleAssets getIntangibleAssets(String transactionId, String companyAccountsId, String companyNumber) throws ServiceException {
        IntangibleAssets intangibleAssets;

        ApiClient apiClient = apiClientService.getApiClient();

        IntangibleApi intangibleApi = getIntangibleApi(apiClient, transactionId, companyAccountsId);
        if (intangibleApi != null) {
            intangibleAssets = intangibleAssetsTransformer.getIntangibleAssets(intangibleApi);
        } else {
            intangibleAssets = new IntangibleAssets();
        }

        addCompanyDatesToIntangibleAssets(intangibleAssets, smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId));

        return intangibleAssets;
    }

    private IntangibleApi getIntangibleApi(ApiClient apiClient, String transactionId, String companyAccountsId) throws ServiceException {

        String uri = INTANGIBLE_ASSET_NOTE.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().intangible().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public List<ValidationError> postIntangibleAssets(String transactionId, String companyAccountsId,
                                                      IntangibleAssets intangibleAssets, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = INTANGIBLE_ASSET_NOTE.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService
                .getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        IntangibleApi intangibleApi = intangibleAssetsTransformer.getIntangibleApi(intangibleAssets);

        boolean intangibleResourceExists = hasIntangibleAssetNote(smallFullApi.getLinks());


        try {
            ApiResponse apiResponse;
            if (!intangibleResourceExists) {
                apiResponse = apiClient.smallFull().intangible().create(uri, intangibleApi).execute();
            } else {
                apiResponse = apiClient.smallFull().intangible().update(uri, intangibleApi).execute();
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
    public void deleteIntangibleAssets(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();
        String uri = INTANGIBLE_ASSET_NOTE.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().intangible().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private void addCompanyDatesToIntangibleAssets(IntangibleAssets intangibleAssets, SmallFullApi smallFullApi) {
        intangibleAssets.setNextAccountsPeriodStartOn(smallFullApi.getNextAccounts().getPeriodStartOn());
        intangibleAssets.setNextAccountsPeriodEndOn(smallFullApi.getNextAccounts().getPeriodEndOn());
        intangibleAssets.setLastAccountsPeriodEndOn(Optional.of(smallFullApi)
                .map(SmallFullApi::getLastAccounts)
                .map(AccountingPeriodApi::getPeriodEndOn)
                .orElse(null));
    }

    private boolean hasIntangibleAssetNote(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getIntangibleAssetsNote() != null;
    }
}
