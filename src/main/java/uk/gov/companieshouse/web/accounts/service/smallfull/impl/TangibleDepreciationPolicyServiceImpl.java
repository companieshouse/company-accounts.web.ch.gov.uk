package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleDepreciationPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class TangibleDepreciationPolicyServiceImpl implements TangibleDepreciationPolicyService {

    @Autowired
    private AccountingPoliciesService accountingPoliciesService;

    @Autowired
    private AccountingPoliciesTransformer accountingPoliciesTransformer;

    private static final String TANGIBLE_DEPRECIATION_POLICY_FIELD_PATH =
        "tangibleDepreciationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
        "validation.length.minInvalid.accounting_policies.tangible_fixed_assets_depreciation_policy";

    /**
     * {@inheritDoc}
     */
    @Override
    public TangibleDepreciationPolicy getTangibleDepreciationPolicy(String transactionId,
        String companyAccountsId) throws ServiceException {
        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);
        return accountingPoliciesTransformer.getTangibleDepreciationPolicy(accountingPoliciesApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitTangibleDepreciationPolicy(String transactionId,
        String companyAccountsId, TangibleDepreciationPolicy tangibleDepreciationPolicy)
        throws ServiceException {
        List<ValidationError> validationErrors = validateTangibleDepreciationPolicy(
            tangibleDepreciationPolicy);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);
        accountingPoliciesTransformer
            .setTangibleDepreciationPolicy(tangibleDepreciationPolicy, accountingPoliciesApi);
        return accountingPoliciesService
            .updateAccountingPoliciesApi(transactionId, companyAccountsId, accountingPoliciesApi);
    }

    /**
     * Validate a submitted intangible amortisation policy
     *
     * @param tangibleDepreciationPolicy The policy to validate
     * @return a list of validation errors, or an empty array list if none are present
     */
    private List<ValidationError> validateTangibleDepreciationPolicy(
        TangibleDepreciationPolicy tangibleDepreciationPolicy) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (tangibleDepreciationPolicyNotProvided(tangibleDepreciationPolicy)) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(TANGIBLE_DEPRECIATION_POLICY_FIELD_PATH);
            validationError.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(validationError);
        }
        return validationErrors;
    }

    /**
     * Check whether a tangible depreciation has not been provided
     *
     * @param tangibleDepreciationPolicy The policy to check
     * @return true if the 'include policy' boolean is set to true, but no details are provided
     */
    private boolean tangibleDepreciationPolicyNotProvided(
        TangibleDepreciationPolicy tangibleDepreciationPolicy) {
        return tangibleDepreciationPolicy.getHasTangibleDepreciationPolicySelected() &&
            StringUtils.isBlank(tangibleDepreciationPolicy.getTangibleDepreciationPolicyDetails());
    }
}
