package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface AccountingPoliciesService {

    AccountingPoliciesApi getAccountingPoliciesApi(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> createAccountingPoliciesApi(String transactionId,
            String companyAccountsId,
            AccountingPoliciesApi accountingPoliciesApi) throws ServiceException;

    List<ValidationError> updateAccountingPoliciesApi(String transactionId,
            String companyAccountsId,
            AccountingPoliciesApi accountingPoliciesApi) throws ServiceException;

}
