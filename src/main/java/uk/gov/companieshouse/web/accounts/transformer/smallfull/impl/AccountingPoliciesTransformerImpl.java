package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;

@Component
public class AccountingPoliciesTransformerImpl implements AccountingPoliciesTransformer {

    /**
     * {@inheritDoc}
     */
    @Override
    public BasisOfPreparation getBasisOfPreparation(AccountingPoliciesApi accountingPoliciesApi) {

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        if (accountingPoliciesApi == null) {
            return basisOfPreparation;
        }

        if (accountingPoliciesApi.getBasisOfMeasurementAndPreparation()
                .equalsIgnoreCase(basisOfPreparation.getPreparedStatement())) {

            basisOfPreparation.setIsPreparedInAccordanceWithStandards(true);
        } else {

            basisOfPreparation.setIsPreparedInAccordanceWithStandards(false);
            basisOfPreparation.setCustomStatement(accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
        }

        return basisOfPreparation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBasisOfPreparation(BasisOfPreparation basisOfPreparation,
            AccountingPoliciesApi accountingPoliciesApi) {

        if (basisOfPreparation.getIsPreparedInAccordanceWithStandards()) {
            accountingPoliciesApi.setBasisOfMeasurementAndPreparation(basisOfPreparation.getPreparedStatement());
        } else {
            accountingPoliciesApi.setBasisOfMeasurementAndPreparation(basisOfPreparation.getCustomStatement());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TurnoverPolicy getTurnoverPolicy(AccountingPoliciesApi accountingPoliciesApi) {

        TurnoverPolicy turnoverPolicy = new TurnoverPolicy();
        if (accountingPoliciesApi == null) {
            return turnoverPolicy;
        }

        if (accountingPoliciesApi.getTurnoverPolicy() != null) {
            turnoverPolicy.setIsIncludeTurnoverSelected(true);
            turnoverPolicy.setTurnoverPolicyDetails(accountingPoliciesApi.getTurnoverPolicy());
        }

        return turnoverPolicy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTurnoverPolicy(TurnoverPolicy turnoverPolicy,
        AccountingPoliciesApi accountingPoliciesApi) {

        if (turnoverPolicy.getIsIncludeTurnoverSelected()) {
            accountingPoliciesApi.setTurnoverPolicy(turnoverPolicy.getTurnoverPolicyDetails());
        } else {
            accountingPoliciesApi.setTurnoverPolicy(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TangibleDepreciationPolicy getTangibleDepreciationPolicy(
        AccountingPoliciesApi accountingPoliciesApi) {
        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        if (accountingPoliciesApi.getTangibleFixedAssetsDepreciationPolicy() != null) {
            tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
            tangibleDepreciationPolicy.setTangibleDepreciationPolicyDetails(
                accountingPoliciesApi.getTangibleFixedAssetsDepreciationPolicy());
        }
        return tangibleDepreciationPolicy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTangibleDepreciationPolicy(TangibleDepreciationPolicy tangibleDepreciationPolicy,
        AccountingPoliciesApi accountingPoliciesApi) {

        if (tangibleDepreciationPolicy.getHasTangibleDepreciationPolicySelected()) {
            accountingPoliciesApi.setTangibleFixedAssetsDepreciationPolicy(
                tangibleDepreciationPolicy.getTangibleDepreciationPolicyDetails());
        } else {
            accountingPoliciesApi.setTangibleFixedAssetsDepreciationPolicy(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IntangibleAmortisationPolicy getIntangibleAmortisationPolicy(
            AccountingPoliciesApi accountingPoliciesApi) {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();

        if (accountingPoliciesApi.getIntangibleFixedAssetsAmortisationPolicy() != null) {

            intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(true);
            intangibleAmortisationPolicy.setIntangibleAmortisationPolicyDetails(
                    accountingPoliciesApi.getIntangibleFixedAssetsAmortisationPolicy());
        }

        return intangibleAmortisationPolicy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setIntangibleAmortisationPolicy(IntangibleAmortisationPolicy intangibleAmortisationPolicy,
            AccountingPoliciesApi accountingPoliciesApi) {

        if (intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy()) {

            accountingPoliciesApi.setIntangibleFixedAssetsAmortisationPolicy(
                    intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails());
        } else {

            accountingPoliciesApi.setIntangibleFixedAssetsAmortisationPolicy(null);
        }

    }
}
