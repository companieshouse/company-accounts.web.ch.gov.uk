package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface SmallFullService {

    /**
     * Create a small full resource
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException on creation failure
     */
    void createSmallFullAccounts(String transactionId, String companyAccountsId) throws ServiceException;

    /**
     * Retrieve a small full resource
     *
     * @param apiClient
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @return the small full resource
     * @throws ServiceException or retrieval failure
     */
    SmallFullApi getSmallFullAccounts(ApiClient apiClient,
            String transactionId, String companyAccountsId) throws ServiceException;
}
