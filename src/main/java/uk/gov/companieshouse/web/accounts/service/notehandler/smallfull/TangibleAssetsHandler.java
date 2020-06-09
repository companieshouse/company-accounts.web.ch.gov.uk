package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class TangibleAssetsHandler implements
        NoteResourceHandler<TangibleApi> {

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate TANGIBLE_ASSETS_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/tangible-assets");

    @Override
    public String getUri(String transactionId, String companyAccountsId) {

        return TANGIBLE_ASSETS_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<TangibleApi>> get(ApiClient apiClient, String uri) {

        return apiClient.smallFull().tangible().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, TangibleApi apiResource) {

        return apiClient.smallFull().tangible().update(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<TangibleApi>> create(ApiClient apiClient, String uri, TangibleApi apiResource) {

        return apiClient.smallFull().tangible().create(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {

        return apiClient.smallFull().tangible().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException {

        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getTangibleAssetsNote());
    }

    @Override
    public NoteType getNoteType() {

        return NoteType.SMALL_FULL_TANGIBLE_ASSETS;
    }
}
