package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;

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

    /**
     * Gets the intangible amortisation policy from the accounting policies API resource
     *
     * @param accountingPoliciesApi The accounting policies API resource
     * @return {@link IntangibleAmortisationPolicy}
     */
    IntangibleAmortisationPolicy getIntangibleAmortisationPolicy(AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Updates the intangible amortisation policy on the accounting policies API resource
     *
     * @param intangibleAmortisationPolicy Intangible amortisation policy details
     * @param accountingPoliciesApi The accounting policies API resource
     */
    void setIntangibleAmortisationPolicy(IntangibleAmortisationPolicy intangibleAmortisationPolicy,
            AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Gets the other accounting policy from the accounting policies API resource
     *
     * @param accountingPoliciesApi The accounting policies API resource
     * @return {@link OtherAccountingPolicy}
     */
    OtherAccountingPolicy getOtherAccountingPolicy(AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Updates the other accounting policy on the accounting policies API resource
     *
     * @param otherAccountingPolicy Other accounting policy details
     * @param accountingPoliciesApi The accounting policies API resource
     */
    void setOtherAccountingPolicy(OtherAccountingPolicy otherAccountingPolicy,
        AccountingPoliciesApi accountingPoliciesApi);


    /**
     * Gets the valuation information policy from the accounting policies API resource
     *
     * @param accountingPoliciesApi The accounting policies API resource
     * @return {@link ValuationInformationPolicy}
     */
    ValuationInformationPolicy getValuationInformationPolicy(AccountingPoliciesApi accountingPoliciesApi);

    /**
     * Updates the valuation information policy on the accounting policies API resource
     *
     * @param valuationInformationPolicy Valuation information policy details
     * @param accountingPoliciesApi The accounting policies API resource
     */
    void setValuationInformationPolicy(ValuationInformationPolicy valuationInformationPolicy,
            AccountingPoliciesApi accountingPoliciesApi);
}
