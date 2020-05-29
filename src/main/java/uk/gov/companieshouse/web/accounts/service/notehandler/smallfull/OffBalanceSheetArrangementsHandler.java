package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class OffBalanceSheetArrangementsHandler implements NoteResourceHandler<OffBalanceSheetApi> {

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate OFF_BALANCE_SHEET_ARRANGEMENTS_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/off-balance-sheet-arrangements");

    @Override
    public String getUri(String transactionId, String companyAccountsId) {

        return OFF_BALANCE_SHEET_ARRANGEMENTS_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<OffBalanceSheetApi>> get(ApiClient apiClient, String uri) {

        return apiClient.smallFull().offBalanceSheet().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, OffBalanceSheetApi apiResource) {

        return apiClient.smallFull().offBalanceSheet().update(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<OffBalanceSheetApi>> create(ApiClient apiClient, String uri, OffBalanceSheetApi apiResource) {

        return apiClient.smallFull().offBalanceSheet().create(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {

        return apiClient.smallFull().offBalanceSheet().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException {

        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getOffBalanceSheetArrangementsNote());
    }

    @Override
    public NoteType getNoteType() {

        return NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS;
    }
}
