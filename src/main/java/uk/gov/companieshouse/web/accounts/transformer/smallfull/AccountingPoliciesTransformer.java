package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;

public interface AccountingPoliciesTransformer {

    /**
     * Gets the basis of accounts preparation from the accounting policies API resource
     *
     * @param accountingPoliciesApi The accounting policies API resource
     * @return The basis of accounts preparation
     */
    BasisOfPreparation getBasisOfPreparation(AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Sets the basis of accounts preparation on the accounting policies API resource
     *
     * @param basisOfPreparation The basis of accounts preparation
     * @param accountingPoliciesApi The accounting policies API resource
     */
    void setBasisOfPreparation(BasisOfPreparation basisOfPreparation,
            AccountingPoliciesApi accountingPoliciesApi);

}
