package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.enumeration.AccountingRegulatoryStandard;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

@Component
public class AccountingPoliciesTransformerImpl implements NoteTransformer<AccountingPolicies, AccountingPoliciesApi> {

    @Override
    public AccountingPolicies toWeb(AccountingPoliciesApi apiResource) {

        AccountingPolicies accountingPolicies = new AccountingPolicies();

        if (apiResource == null) {
            return accountingPolicies;
        }

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        if(apiResource.getBasisOfMeasurementAndPreparation() != null) {
            if (apiResource.getBasisOfMeasurementAndPreparation().trim()
                    .equalsIgnoreCase(AccountingRegulatoryStandard.FRS102.toString())) {

                basisOfPreparation.setAccountingRegulatoryStandard(AccountingRegulatoryStandard.FRS102);

            } else if (apiResource.getBasisOfMeasurementAndPreparation().trim()
                    .equalsIgnoreCase(AccountingRegulatoryStandard.FRS101.toString())) {

                basisOfPreparation.setAccountingRegulatoryStandard(AccountingRegulatoryStandard.FRS101);
            } else {

                basisOfPreparation.setAccountingRegulatoryStandard(AccountingRegulatoryStandard.OTHER);
            }
        }

        accountingPolicies.setBasisOfPreparation(basisOfPreparation);

        TurnoverPolicy turnoverPolicy = new TurnoverPolicy();
        if (apiResource.getTurnoverPolicy() != null) {
            turnoverPolicy.setIsIncludeTurnoverSelected(true);
            turnoverPolicy.setTurnoverPolicyDetails(apiResource.getTurnoverPolicy());
        }
        accountingPolicies.setTurnoverPolicy(turnoverPolicy);

        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        if (apiResource.getTangibleFixedAssetsDepreciationPolicy() != null) {
            tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
            tangibleDepreciationPolicy.setTangibleDepreciationPolicyDetails(
                    apiResource.getTangibleFixedAssetsDepreciationPolicy());
        }
        accountingPolicies.setTangibleDepreciationPolicy(tangibleDepreciationPolicy);

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        if (apiResource.getIntangibleFixedAssetsAmortisationPolicy() != null) {

            intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(true);
            intangibleAmortisationPolicy.setIntangibleAmortisationPolicyDetails(
                    apiResource.getIntangibleFixedAssetsAmortisationPolicy());
        }
        accountingPolicies.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy);

        ValuationInformationPolicy valuationInformationPolicy = new ValuationInformationPolicy();
        if (apiResource.getValuationInformationAndPolicy() != null) {

            valuationInformationPolicy.setIncludeValuationInformationPolicy(true);
            valuationInformationPolicy.setValuationInformationPolicyDetails(
                    apiResource.getValuationInformationAndPolicy());
        }
        accountingPolicies.setValuationInformationPolicy(valuationInformationPolicy);

        OtherAccountingPolicy otherAccountingPolicy = new OtherAccountingPolicy();
        if (apiResource.getOtherAccountingPolicy() != null) {
            otherAccountingPolicy.setHasOtherAccountingPolicySelected(true);
            otherAccountingPolicy.setOtherAccountingPolicyDetails(
                    apiResource.getOtherAccountingPolicy());
        }
        accountingPolicies.setOtherAccountingPolicy(otherAccountingPolicy);

        return accountingPolicies;
    }

    @Override
    public AccountingPoliciesApi toApi(AccountingPolicies webResource) {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        if (webResource.getBasisOfPreparation().getAccountingRegulatoryStandard().toString().equalsIgnoreCase(AccountingRegulatoryStandard.FRS102.toString())) {
            accountingPoliciesApi.setBasisOfMeasurementAndPreparation(AccountingRegulatoryStandard.FRS102.toString());
        } else if (webResource.getBasisOfPreparation().getAccountingRegulatoryStandard().toString().equalsIgnoreCase(AccountingRegulatoryStandard.FRS101.toString())) {
            accountingPoliciesApi.setBasisOfMeasurementAndPreparation(AccountingRegulatoryStandard.FRS101.toString());
        }

        if (BooleanUtils.isTrue(webResource.getTurnoverPolicy().getIsIncludeTurnoverSelected())) {
            accountingPoliciesApi.setTurnoverPolicy(webResource.getTurnoverPolicy().getTurnoverPolicyDetails());
        } else {
            accountingPoliciesApi.setTurnoverPolicy(null);
        }

        if (BooleanUtils.isTrue(webResource.getTangibleDepreciationPolicy().getHasTangibleDepreciationPolicySelected())) {
            accountingPoliciesApi.setTangibleFixedAssetsDepreciationPolicy(
                    webResource.getTangibleDepreciationPolicy().getTangibleDepreciationPolicyDetails());
        } else {
            accountingPoliciesApi.setTangibleFixedAssetsDepreciationPolicy(null);
        }

        if (BooleanUtils.isTrue(webResource.getIntangibleAmortisationPolicy().getIncludeIntangibleAmortisationPolicy())) {

            accountingPoliciesApi.setIntangibleFixedAssetsAmortisationPolicy(
                    webResource.getIntangibleAmortisationPolicy().getIntangibleAmortisationPolicyDetails());
        } else {
            accountingPoliciesApi.setIntangibleFixedAssetsAmortisationPolicy(null);
        }

        if (BooleanUtils.isTrue(webResource.getValuationInformationPolicy().getIncludeValuationInformationPolicy())) {

            accountingPoliciesApi.setValuationInformationAndPolicy(
                    webResource.getValuationInformationPolicy().getValuationInformationPolicyDetails());
        } else {

            accountingPoliciesApi.setValuationInformationAndPolicy(null);
        }

        if (BooleanUtils.isTrue(webResource.getOtherAccountingPolicy().getHasOtherAccountingPolicySelected())) {
            accountingPoliciesApi.setOtherAccountingPolicy(
                    webResource.getOtherAccountingPolicy().getOtherAccountingPolicyDetails());
        } else {
            accountingPoliciesApi.setOtherAccountingPolicy(null);
        }

        return accountingPoliciesApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_ACCOUNTING_POLICIES;
    }
}
