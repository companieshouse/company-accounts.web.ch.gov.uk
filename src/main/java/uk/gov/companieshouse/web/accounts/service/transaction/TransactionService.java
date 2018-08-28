package uk.gov.companieshouse.web.accounts.service.transaction;

import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.transaction.Transaction;

public interface TransactionService {

    String createTransaction(String companyNumber) throws ApiErrorResponseException;

    void closeTransaction(String transactionId) throws ApiErrorResponseException;
}
