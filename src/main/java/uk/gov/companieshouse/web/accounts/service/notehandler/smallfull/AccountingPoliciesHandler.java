package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class AccountingPoliciesHandler implements NoteResourceHandler<AccountingPoliciesApi> {
    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate ACCOUNTING_POLICIES_URI = new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/accounting-policies"
    );

    @Override
    public String getUri(String transactionId, String companyAccountsId) {
        return ACCOUNTING_POLICIES_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<AccountingPoliciesApi>> get(ApiClient apiClient, String uri) {
        return apiClient.smallFull().accountingPolicies().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, AccountingPoliciesApi accountingPoliciesApi) {
        return apiClient.smallFull().accountingPolicies().update(uri, accountingPoliciesApi);
    }

    @Override
    public Executor<ApiResponse<AccountingPoliciesApi>> create(ApiClient apiClient, String uri, AccountingPoliciesApi accountingPoliciesApi) {
        return apiClient.smallFull().accountingPolicies().create(uri, accountingPoliciesApi);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {
        return apiClient.smallFull().accountingPolicies().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId) throws ServiceException {
        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getAccountingPoliciesNote());
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_ACCOUNTING_POLICIES;
    }
}
