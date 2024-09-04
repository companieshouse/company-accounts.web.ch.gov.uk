package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class DebtorsHandler implements NoteResourceHandler<DebtorsApi> {

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate DEBTORS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/debtors");


    @Override
    public String getUri(String transactionId, String companyAccountsId) {
        return DEBTORS_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<DebtorsApi>> get(ApiClient apiClient, String uri) {

        return apiClient.smallFull().debtors().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, DebtorsApi apiResource) {

        return apiClient.smallFull().debtors().update(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<DebtorsApi>> create(ApiClient apiClient, String uri, DebtorsApi apiResource) {

        return apiClient.smallFull().debtors().create(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {

        return apiClient.smallFull().debtors().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId)
            throws ServiceException {

        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getDebtorsNote());
    }

    @Override
    public NoteType getNoteType() {

        return NoteType.SMALL_FULL_DEBTORS;
    }
}
