package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RelatedPartyTransactionsApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface RelatedPartyTransactionsService {

    void createRelatedPartyTransactions(String transactionId, String companyAccountsId)
            throws ServiceException;

    RelatedPartyTransactionsApi getRelatedPartyTransactions(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException;

    void deleteRelatedPartyTransactions(String transactionId, String companyAccountsId)
            throws ServiceException;

}
