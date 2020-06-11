package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class FixedAssetsInvestmentsHandler implements NoteResourceHandler<FixedAssetsInvestmentsApi> {

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate FIXED_ASSETS_INVESTMENTS_URI = new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/fixed-assets-investments"
    );

    @Override
    public String getUri(String transactionId, String companyAccountsId) {

        return FIXED_ASSETS_INVESTMENTS_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<FixedAssetsInvestmentsApi>> get(ApiClient apiClient, String uri) {
        return apiClient.smallFull().fixedAssetsInvestments().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi) {
        return apiClient.smallFull().fixedAssetsInvestments().update(uri, fixedAssetsInvestmentsApi);
    }

    @Override
    public Executor<ApiResponse<FixedAssetsInvestmentsApi>> create(ApiClient apiClient, String uri, FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi) {
        return apiClient.smallFull().fixedAssetsInvestments().create(uri, fixedAssetsInvestmentsApi);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {
        return apiClient.smallFull().fixedAssetsInvestments().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId) throws ServiceException {

        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getFixedAssetsInvestmentsNote());
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT;
    }
}
