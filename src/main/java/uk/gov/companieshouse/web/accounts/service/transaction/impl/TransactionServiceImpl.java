package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    ApiClientService apiClientService;

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
            transaction = apiClient.transactions().create(transaction);
        } catch (ApiErrorResponseException e) {
            
            throw new ServiceException(e);
        }

        return transaction.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeTransaction(String transactionId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        Transaction transaction;

        try {
            transaction = apiClient.transaction(transactionId).get();
            transaction.setStatus(TransactionStatus.CLOSED);
            apiClient.transaction(transactionId).update(transaction);
        } catch (ApiErrorResponseException e) {

            throw new ServiceException(e);
        }
    }
}
