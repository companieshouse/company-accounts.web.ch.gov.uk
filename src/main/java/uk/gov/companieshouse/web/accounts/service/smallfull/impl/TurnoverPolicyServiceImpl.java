package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TurnoverPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class TurnoverPolicyServiceImpl implements TurnoverPolicyService {

    private final AccountingPoliciesService accountingPoliciesService;
    private final AccountingPoliciesTransformer accountingPoliciesTransformer;

    @Autowired
    public TurnoverPolicyServiceImpl(
        AccountingPoliciesService accountingPoliciesService,
        AccountingPoliciesTransformer accountingPoliciesTransformer) {
        this.accountingPoliciesService = accountingPoliciesService;
        this.accountingPoliciesTransformer = accountingPoliciesTransformer;
    }

    @Override
    public TurnoverPolicy getTurnOverPolicy(String transactionId, String companyAccountsId)
        throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        return accountingPoliciesTransformer.getTurnoverPolicy(accountingPoliciesApi);
    }

    @Override
    public List<ValidationError> postTurnoverPolicy(String transactionId, String companyAccountsId,
        TurnoverPolicy turnoverPolicy) throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        //TODO remove when basis of preparation controller is there.
        if (accountingPoliciesApi == null) {
            accountingPoliciesApi = new AccountingPoliciesApi();
            accountingPoliciesTransformer.setTurnoverPolicy(turnoverPolicy, accountingPoliciesApi);

            return accountingPoliciesService.createAccountingPoliciesApi(transactionId, companyAccountsId, accountingPoliciesApi);

        } else {
            accountingPoliciesTransformer.setTurnoverPolicy(turnoverPolicy, accountingPoliciesApi);
            return accountingPoliciesService
                .updateAccountingPoliciesApi(transactionId, companyAccountsId,
                    accountingPoliciesApi);
        }

    }
}
