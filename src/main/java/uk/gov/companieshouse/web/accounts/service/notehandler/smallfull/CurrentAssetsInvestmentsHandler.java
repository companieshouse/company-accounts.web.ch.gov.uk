package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class CurrentAssetsInvestmentsHandler implements
        NoteResourceHandler<CurrentAssetsInvestmentsApi> {

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate STOCKS_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/current-assets-investments");

    @Override
    public String getUri(String transactionId, String companyAccountsId) {

        return STOCKS_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<CurrentAssetsInvestmentsApi>> get(ApiClient apiClient, String uri) {

        return apiClient.smallFull().currentAssetsInvestments().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, CurrentAssetsInvestmentsApi apiResource) {

        return apiClient.smallFull().currentAssetsInvestments().update(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<CurrentAssetsInvestmentsApi>> create(ApiClient apiClient, String uri, CurrentAssetsInvestmentsApi apiResource) {

        return apiClient.smallFull().currentAssetsInvestments().create(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {

        return apiClient.smallFull().currentAssetsInvestments().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException {

        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getCurrentAssetsInvestmentsNote());
    }

    @Override
    public NoteType getNoteType() {

        return NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS;
    }
}
