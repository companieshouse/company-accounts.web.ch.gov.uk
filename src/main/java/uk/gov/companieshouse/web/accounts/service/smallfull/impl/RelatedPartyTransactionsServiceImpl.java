package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RelatedPartyTransactionsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.RelatedPartyTransactionsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class RelatedPartyTransactionsServiceImpl implements RelatedPartyTransactionsService {
    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private SmallFullService smallFullService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    private static final UriTemplate RELATED_PARTY_TRANSACTIONS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/related-party-transactions");

    private static final String RESOURCE_NAME = "related party transactions";

    @Override
    public void createRelatedPartyTransactions(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = RELATED_PARTY_TRANSACTIONS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().relatedPartyTransactions().create(uri, new RelatedPartyTransactionsApi())
                    .execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }
    }

    @Override
    public RelatedPartyTransactionsApi getRelatedPartyTransactions(ApiClient apiClient, String transactionId,
            String companyAccountsId) throws ServiceException {
        String uri = RELATED_PARTY_TRANSACTIONS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().relatedPartyTransactions().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public void deleteRelatedPartyTransactions(String transactionId, String companyAccountsId)
            throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        if (StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                        .getLinks().getRelatedPartyTransactions())) {
            String uri = RELATED_PARTY_TRANSACTIONS_URI.expand(transactionId, companyAccountsId).toString();

            try {
                apiClient.smallFull().relatedPartyTransactions().delete(uri).execute();
            } catch (ApiErrorResponseException e) {
                serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
            } catch (URIValidationException e) {
                serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
            }
        }
    }
}
