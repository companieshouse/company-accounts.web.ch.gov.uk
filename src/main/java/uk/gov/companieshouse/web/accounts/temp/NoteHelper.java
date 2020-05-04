package uk.gov.companieshouse.web.accounts.temp;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface NoteHelper<A extends ApiResource> {

    String getUri(String transactionId, String companyAccountsId);

    Executor<ApiResponse<A>> get(ApiClient apiClient, String uri);

    Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, A apiResource);

    Executor<ApiResponse<A>> create(ApiClient apiClient, String uri, A apiResource);

    Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri);

    boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId) throws ServiceException;

    NoteType getNoteType();
}
