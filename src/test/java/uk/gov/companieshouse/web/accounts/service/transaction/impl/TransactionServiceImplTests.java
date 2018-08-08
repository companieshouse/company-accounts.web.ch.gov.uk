package uk.gov.companieshouse.web.accounts.service.transaction.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import uk.gov.companieshouse.api.model.transaction.Transaction;
import uk.gov.companieshouse.web.accounts.service.transaction.TransactionService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceImplTests {

    private TransactionService transactionService = new TransactionServiceImpl();

    private static final String COMPANY_NUMBER = "123456";
    
    @Test
    void createTransactionSuccess() {

        Transaction transaction = transactionService.createTransaction(COMPANY_NUMBER);

        assertNotNull(transaction);
        assertEquals(COMPANY_NUMBER, transaction.getCompanyNumber());
    }

}
