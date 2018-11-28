package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAmortisationPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class IntangibleAmortisationPolicyServiceImpl implements IntangibleAmortisationPolicyService {

    @Autowired
    private AccountingPoliciesService accountingPoliciesService;

    @Autowired
    private AccountingPoliciesTransformer accountingPoliciesTransformer;

    private static final String INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH =
            "intangibleAmortisationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.intangible_fixed_assets_amortisation_policy";

    /**
     * {@inheritDoc}
     */
    @Override
    public IntangibleAmortisationPolicy getIntangibleAmortisationPolicy(String transactionId,
            String companyAccountsId) throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi =
                accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        return accountingPoliciesTransformer.getIntangibleAmortisationPolicy(accountingPoliciesApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitIntangibleAmortisationPolicy(String transactionId,
            String companyAccountsId, IntangibleAmortisationPolicy intangibleAmortisationPolicy)
            throws ServiceException {

        List<ValidationError> validationErrors = validateIntangibleAmortisationPolicy(intangibleAmortisationPolicy);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        AccountingPoliciesApi accountingPoliciesApi =
                accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        accountingPoliciesTransformer
                .setIntangibleAmortisationPolicy(intangibleAmortisationPolicy, accountingPoliciesApi);

        return accountingPoliciesService
                .updateAccountingPoliciesApi(transactionId, companyAccountsId, accountingPoliciesApi);
    }

    /**
     * Validate a submitted intangible amortisation policy
     * @param intangibleAmortisationPolicy The policy to validate
     * @return a list of validation errors, or an empty array list if none are present
     */
    private List<ValidationError> validateIntangibleAmortisationPolicy(IntangibleAmortisationPolicy intangibleAmortisationPolicy) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (intangibleAmortisationPolicyNotProvided(intangibleAmortisationPolicy)) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH);
            validationError.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(validationError);
        }

        return validationErrors;
    }

    /**
     * Check whether an intangible amortisation has not been provided
     * @param intangibleAmortisationPolicy The policy to check
     * @return true if the 'include policy' boolean is set to true, but no details are provided
     */
    private boolean intangibleAmortisationPolicyNotProvided(IntangibleAmortisationPolicy intangibleAmortisationPolicy) {

        return intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy() &&
                StringUtils.isBlank(intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails());
    }
}
