package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Component
public class BasisOfPreparationServiceImpl implements BasisOfPreparationService {

    @Autowired
    private AccountingPoliciesService accountingPoliciesService;

    @Autowired
    private AccountingPoliciesTransformer transformer;

    /**
     * {@inheritDoc}
     */
    @Override
    public BasisOfPreparation getBasisOfPreparation(String transactionId, String companyAccountsId)
            throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi =
                accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        return transformer.getBasisOfPreparation(accountingPoliciesApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitBasisOfPreparation(String transactionId, String companyAccountsId,
            BasisOfPreparation basisOfPreparation) throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi =
                accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        if (accountingPoliciesApi == null) {
            accountingPoliciesApi = new AccountingPoliciesApi();
            transformer.setBasisOfPreparation(basisOfPreparation, accountingPoliciesApi);
            return accountingPoliciesService.createAccountingPoliciesApi(transactionId, companyAccountsId, accountingPoliciesApi);
        } else {
            transformer.setBasisOfPreparation(basisOfPreparation, accountingPoliciesApi);
            return accountingPoliciesService.updateAccountingPoliciesApi(transactionId, companyAccountsId, accountingPoliciesApi);
        }
    }
}
