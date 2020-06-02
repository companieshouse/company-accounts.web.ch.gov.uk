package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.creditorswithinoneyear.CreditorsWithinOneYearApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class CreditorsWithinOneYearHandler implements NoteResourceHandler<CreditorsWithinOneYearApi> {

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate CREDITORS_WITHIN_ONE_YEAR_URI = new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/creditors-within-one-year"
    );

    @Override
    public String getUri(String transactionId, String companyAccountsId) {
        return CREDITORS_WITHIN_ONE_YEAR_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<CreditorsWithinOneYearApi>> get(ApiClient apiClient, String uri) {
        return apiClient.smallFull().creditorsWithinOneYear().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, CreditorsWithinOneYearApi apiResource) {
        return apiClient.smallFull().creditorsWithinOneYear().update(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<CreditorsWithinOneYearApi>> create(ApiClient apiClient, String uri, CreditorsWithinOneYearApi apiResource) {
        return apiClient.smallFull().creditorsWithinOneYear().create(uri, apiResource);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {
        return apiClient.smallFull().creditorsWithinOneYear().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId) throws ServiceException {
        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getCreditorsWithinOneYearNote()
        );
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_CREDITORS_WITHIN_ONE_YEAR;
    }
}
