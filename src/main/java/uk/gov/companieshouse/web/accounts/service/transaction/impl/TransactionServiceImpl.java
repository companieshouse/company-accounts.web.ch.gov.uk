package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ApiClientService apiClientService;

    private static final UriTemplate TRANSACTIONS_URI = new UriTemplate("/transactions/{transactionId}");

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String createTransaction(String companyNumber) throws ServiceException {

        return createTransactionWithDescription(companyNumber, "Small Full Accounts");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createTransactionWithDescription(String companyNumber, String description) throws ServiceException {

        Transaction transaction = new Transaction();
        transaction.setCompanyNumber(companyNumber);

        transaction.setDescription(description);

        ApiClient apiClient = apiClientService.getApiClient();

        try {
            transaction = apiClient.transactions().create("/transactions", transaction).execute().getData();
        } catch (ApiErrorResponseException e) {
            
            throw new ServiceException("Error creating transaction", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for transactions resource", e);
        }

        return transaction.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeTransaction(String transactionId) throws ServiceException {

        String uri = TRANSACTIONS_URI.expand(transactionId).toString();

        try {
            Transaction transaction = apiClientService.getApiClient().transactions().get(uri).execute().getData();
            transaction.setStatus(TransactionStatus.CLOSED);
            apiClientService.getApiClient().transactions().update(uri, transaction).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error closing transaction", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for transactions resource", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createResumeLink(String companyNumber, String transactionId, String companyAccountsId) throws ServiceException {

        String uri = TRANSACTIONS_URI.expand(transactionId).toString();

        String resumeLink = "/company/" + companyNumber +
                "/transaction/" + transactionId +
                "/company-accounts/" + companyAccountsId +
                "/resume";

        Transaction transaction = new Transaction();
        transaction.setResumeJourneyUri(resumeLink);

        try {
            apiClientService.getApiClient().transactions().update(uri, transaction).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error updating transaction", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for updating transactions resource", e);
        }
    }
}
