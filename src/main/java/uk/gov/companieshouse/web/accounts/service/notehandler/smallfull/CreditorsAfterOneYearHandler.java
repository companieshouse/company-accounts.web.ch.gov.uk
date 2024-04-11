package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorsafteroneyear.CreditorsAfterOneYearApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class CreditorsAfterOneYearHandler implements NoteResourceHandler<CreditorsAfterOneYearApi> {
    private static final UriTemplate CREDITORS_AFTER_ONE_YEAR_URI = new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/creditors-after-more-than-one-year"
            );

    private final SmallFullService smallFullService;

    @Autowired
    public CreditorsAfterOneYearHandler(SmallFullService smallFullService) {
        this.smallFullService = smallFullService;
        
    }

    @Override
    public String getUri(String transactionId, String companyAccountsId) {
        return CREDITORS_AFTER_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<CreditorsAfterOneYearApi>> get(ApiClient apiClient, String uri) {
        return apiClient.smallFull().creditorsAfterOneYear().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri,
                    CreditorsAfterOneYearApi apiResource) {
        return apiClient.smallFull().creditorsAfterOneYear().update(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<CreditorsAfterOneYearApi>> create(ApiClient apiClient, String uri,
                    CreditorsAfterOneYearApi apiResource) {
        return apiClient.smallFull().creditorsAfterOneYear().create(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {
        return apiClient.smallFull().creditorsAfterOneYear().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId,
                    String companyAccountsId) throws ServiceException {
        return StringUtils.isNotBlank(smallFullService
                        .getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getCreditorsAfterMoreThanOneYearNote());
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_CREDITORS_AFTER_ONE_YEAR;
    }
}
