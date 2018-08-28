package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.api.model.transaction.TransactionStatus;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    ApiClientService apiClientService;

    @Override
    public String createTransaction(String companyNumber) throws ApiErrorResponseException {

        Transaction transaction = new Transaction();
        transaction.setCompanyNumber(companyNumber);

        transaction.setDescription("Small Full Accounts");

        ApiClient apiClient = apiClientService.getApiClient();

        transaction = apiClient.transactions().create(transaction);

        return transaction.getId();
    }

    @Override
    public void closeTransaction(String transactionId) throws ApiErrorResponseException {

        ApiClient apiClient = apiClientService.getApiClient();

        Transaction transaction = apiClient.transaction(transactionId).get();

        transaction.setStatus(TransactionStatus.CLOSED);

        apiClient.transaction(transactionId).update(transaction);
    }
}
