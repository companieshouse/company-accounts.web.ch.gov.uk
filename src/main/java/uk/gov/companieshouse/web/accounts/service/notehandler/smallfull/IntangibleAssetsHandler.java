package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class IntangibleAssetsHandler implements NoteResourceHandler<IntangibleApi> {
    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate INTANGIBLE_ASSETS_URI = new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/intangible-assets"
    );

    @Override
    public String getUri(String transactionId, String companyAccountsId) {
        return INTANGIBLE_ASSETS_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<IntangibleApi>> get(ApiClient apiClient, String uri) {
        return apiClient.smallFull().intangible().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, IntangibleApi intangibleApi) {
        return apiClient.smallFull().intangible().update(uri, intangibleApi);
    }

    @Override
    public Executor<ApiResponse<IntangibleApi>> create(ApiClient apiClient, String uri, IntangibleApi intangibleApi) {
        return apiClient.smallFull().intangible().create(uri, intangibleApi);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {
        return apiClient.smallFull().intangible().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId) throws ServiceException {
        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                .getLinks().getIntangibleAssetsNote());
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_INTANGIBLE_ASSETS;
    }
}