package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Override
    public Transaction createTransaction(String companyNumber) {

        Transaction transaction = new Transaction();
        transaction.setCompanyNumber(companyNumber);

        return transaction;
    }
}
