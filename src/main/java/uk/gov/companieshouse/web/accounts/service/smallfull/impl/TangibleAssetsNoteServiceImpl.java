package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.api.model.company.CompanyProfileApi;
import uk.gov.companieshouse.api.model.company.account.CompanyAccountApi;
import uk.gov.companieshouse.api.model.company.account.LastAccountsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class TangibleAssetsNoteServiceImpl implements TangibleAssetsNoteService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private TangibleAssetsTransformer tangibleAssetsTransformer;

    private static final UriTemplate TANGIBLE_ASSET_NOTE =
        new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/tangible-assets");

    private static final String RESOURCE_NAME = "tangible assets";

    @Override
    public TangibleAssets getTangibleAssets(String transactionId, String companyAccountsId,
        String companyNumber) throws ServiceException {

        TangibleAssets tangibleAssets;

        TangibleApi tangibleApi = getTangibleApi(transactionId, companyAccountsId);
        if (tangibleApi != null) {
            tangibleAssets = tangibleAssetsTransformer.getTangibleAssets(tangibleApi);
        } else {
            tangibleAssets = new TangibleAssets();
        }

        addCompanyDatesToTangibleAssets(tangibleAssets, getCompanyProfile(companyNumber));

        return tangibleAssets;
    }

    private TangibleApi getTangibleApi(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = TANGIBLE_ASSET_NOTE.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().tangible().get(uri).execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public List<ValidationError> postTangibleAssets(String transactionId, String companyAccountsId,
        TangibleAssets tangibleAssets, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = TANGIBLE_ASSET_NOTE.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService
            .getSmallFullAccounts(apiClient, transactionId, companyAccountsId);

        TangibleApi tangibleApi = tangibleAssetsTransformer.getTangibleApi(tangibleAssets);

        boolean tangibleResourceExists = hasTangibleAssetNote(smallFullApi.getLinks());

        try {
            if (!tangibleResourceExists) {
                apiClient.smallFull().tangible().create(uri, tangibleApi).execute();
            } else {
                apiClient.smallFull().tangible().update(uri, tangibleApi).execute();
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            return serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    private boolean hasTangibleAssetNote(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getTangibleAssetsNote() != null;
    }

    private CompanyProfileApi getCompanyProfile(String companyNumber) throws ServiceException {
        return companyService.getCompanyProfile(companyNumber);
    }

    private void addCompanyDatesToTangibleAssets(TangibleAssets tangibleAssets, CompanyProfileApi companyProfile) {
        tangibleAssets.setLastAccountsPeriodEndOn(Optional.of(companyProfile)
            .map(CompanyProfileApi::getAccounts)
            .map(CompanyAccountApi::getLastAccounts)
            .map(LastAccountsApi::getPeriodEndOn)
            .orElse(null));
        tangibleAssets.setNextAccountsPeriodStartOn(companyProfile.getAccounts().getNextAccounts().getPeriodStartOn());
        tangibleAssets.setNextAccountsPeriodEndOn(companyProfile.getAccounts().getNextAccounts().getPeriodEndOn());
    }

    public void deleteTangibleAssets(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = TANGIBLE_ASSET_NOTE.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().tangible().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }
}