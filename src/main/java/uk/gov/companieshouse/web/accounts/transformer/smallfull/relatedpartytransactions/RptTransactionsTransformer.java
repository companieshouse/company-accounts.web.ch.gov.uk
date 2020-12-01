package uk.gov.companieshouse.web.accounts.transformer.smallfull.relatedpartytransactions;

import uk.gov.companieshouse.api.model.accounts.smallfull.relatedpartytransactions.RptTransactionApi;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;

public interface RptTransactionsTransformer {

    RptTransactionApi getRptTransactionsApi(RptTransactionToAdd rptTransactionToAdd);

    RptTransaction[] getAllRptTransactions(RptTransactionApi[] rptTransactions);
}
