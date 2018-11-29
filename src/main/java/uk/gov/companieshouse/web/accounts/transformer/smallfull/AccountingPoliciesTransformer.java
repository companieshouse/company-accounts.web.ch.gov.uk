package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;

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

    /**
     * Gets the turn over policy from the accounting policies API resource
     *
     * @param accountingPoliciesApi The accounting policies API resource
     * @return {@link TurnoverPolicy}
     */
    TurnoverPolicy getTurnoverPolicy(AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Updates the turn over policy on the accounting policies API resource
     *
     * @param turnoverPolicy Turnover Policy details
     * @param accountingPoliciesApi The accounting policies API resource
     */
    void setTurnoverPolicy(TurnoverPolicy turnoverPolicy,
        AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Gets the tangible depreciation policy from the accounting policies API resource
     *
     * @param accountingPoliciesApi The accounting policies API resource
     * @return {@link TangibleDepreciationPolicy}
     */
    TangibleDepreciationPolicy getTangibleDepreciationPolicy(AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Updates the tangible depreciation policy on the accounting policies API resource
     *
     * @param tangibleDepreciationPolicy Tangible depreciation policy details
     * @param accountingPoliciesApi The accounting policies API resource
     */
    void setTangibleDepreciationPolicy(TangibleDepreciationPolicy tangibleDepreciationPolicy,
        AccountingPoliciesApi accountingPoliciesApi);

}
