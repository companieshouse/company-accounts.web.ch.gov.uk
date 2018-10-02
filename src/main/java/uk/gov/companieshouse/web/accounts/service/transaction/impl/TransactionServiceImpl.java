package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.transaction.TransactionsResourceHandler;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    ApiClientService apiClientService;

    private static final UriTemplate CLOSE_TRANSACTIONS_URI =
            new UriTemplate("/transactions/{transactionId}");

    /**
     * {@inheritDoc}
     */
    @Override
    public String createTransaction(String companyNumber) throws ServiceException {

        Transaction transaction = new Transaction();
        transaction.setCompanyNumber(companyNumber);

        transaction.setDescription("Small Full Accounts");

        ApiClient apiClient = apiClientService.getApiClient();

        try {
            transaction = apiClient.transactions().create("/transactions", transaction).execute();
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

        String uri = CLOSE_TRANSACTIONS_URI.expand(transactionId).toString();

        try {
            Transaction transaction = apiClientService.getApiClient().transactions().get(uri).execute();
            transaction.setStatus(TransactionStatus.CLOSED);
            apiClientService.getApiClient().transactions().update(uri, transaction).execute();
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error closing transaction", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for transactions resource", e);
        }
    }
}
