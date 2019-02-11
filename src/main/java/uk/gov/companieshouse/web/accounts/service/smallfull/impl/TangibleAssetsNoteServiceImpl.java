package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.DepreciationAtPeriodStart;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.depreciation.TangibleAssetsDepreciation;
import uk.gov.companieshouse.web.accounts.service.company.CompanyService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class TangibleAssetsNoteServiceImpl implements TangibleAssetsNoteService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private TangibleAssetsTransformer tangibleAssetsTransformer;

    private static final UriTemplate TANGIBLE_ASSET_NOTE =
        new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/tangible-assets");

    private static final String INVALID_URI_MESSAGE = "Invalid URI for tangible assets note resource";

    @Override
    public TangibleAssets getTangibleAssets(String transactionId, String companyAccountsId,
        String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = TANGIBLE_ASSET_NOTE.expand(transactionId, companyAccountsId).toString();

        TangibleAssets tangibleAssets;

        try {
            TangibleApi tangibleApi = apiClient.smallFull().tangible().get(uri).execute();

            tangibleAssets = tangibleAssetsTransformer.getTangibleAssets(tangibleApi);

        } catch (ApiErrorResponseException e) {

            if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
                tangibleAssets = new TangibleAssets();
            } else {
                throw new ServiceException("Error retrieving tangible assets note resource", e);
            }
        } catch (URIValidationException e) {

            throw new ServiceException(INVALID_URI_MESSAGE, e);
        }

        addCompanyDatesToTangibleAssets(tangibleAssets, getCompanyProfile(companyNumber));

        return tangibleAssets;
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
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException(
                        "Bad request when creating tangible assets note resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating tangible assets note resource", e);
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
}