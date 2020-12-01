package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.AddOrRemoveRptTransactions;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransaction;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

public interface RptTransactionService {

    RptTransaction[] getAllRptTransactions(String transactionId, String companyAccountsId) throws ServiceException;

    List<ValidationError> createRptTransaction(String transactionId, String companyAccountsId, AddOrRemoveRptTransactions addOrRemoveRptTransactions) throws ServiceException;

    void deleteRptTransaction(String transactionId, String companyAccountsId, String rptTransactionId) throws ServiceException;

    List<ValidationError> submitAddOrRemoveRptTransactions(String transactionId, String companyAccountsId, AddOrRemoveRptTransactions addOrRemoveRptTransactions) throws  ServiceException;
}
