package uk.gov.companieshouse.web.accounts.service.transaction;

import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.model.transaction.Transaction;

public interface TransactionService {

    Transaction createTransaction(String companyNumber) throws ApiErrorResponseException;
}
