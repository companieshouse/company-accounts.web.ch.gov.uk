package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class OtherAccountingPolicyServiceImpl implements OtherAccountingPolicyService {

    @Autowired
    private AccountingPoliciesService accountingPoliciesService;

    @Autowired
    private AccountingPoliciesTransformer accountingPoliciesTransformer;

    private static final String OTHER_ACCOUNTING_POLICY_FIELD_PATH =
        "otherAccountingPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
        "validation.length.minInvalid.accounting_policies.other_accounting_policy";

    /**
     * {@inheritDoc}
     */
    @Override
    public OtherAccountingPolicy getOtherAccountingPolicy(String transactionId,
        String companyAccountsId) throws ServiceException {
        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);
        return accountingPoliciesTransformer.getOtherAccountingPolicy(accountingPoliciesApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitOtherAccountingPolicy(String transactionId,
        String companyAccountsId, OtherAccountingPolicy otherAccountingPolicy)
        throws ServiceException {
        List<ValidationError> validationErrors = validateOtherAccountingPolicy(
            otherAccountingPolicy);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);
        accountingPoliciesTransformer
            .setOtherAccountingPolicy(otherAccountingPolicy, accountingPoliciesApi);
        return accountingPoliciesService
            .updateAccountingPoliciesApi(transactionId, companyAccountsId, accountingPoliciesApi);
    }

    /**
     * Validate a submitted other accounting policy
     *
     * @param otherAccountingPolicy The policy to validate
     * @return a list of validation errors, or an empty array list if none are present
     */
    private List<ValidationError> validateOtherAccountingPolicy(
        OtherAccountingPolicy otherAccountingPolicy) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (otherAccountingPolicyNotProvided(otherAccountingPolicy)) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(OTHER_ACCOUNTING_POLICY_FIELD_PATH);
            validationError.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(validationError);
        }
        return validationErrors;
    }

    /**
     * Check whether a other accounting policy has not been provided
     *
     * @param otherAccountingPolicy The policy to check
     * @return true if the 'include policy' boolean is set to true, but no details are provided
     */
    private boolean otherAccountingPolicyNotProvided(
        OtherAccountingPolicy otherAccountingPolicy) {
        return otherAccountingPolicy.getHasOtherAccountingPolicySelected() &&
            StringUtils.isBlank(otherAccountingPolicy.getOtherAccountingPolicyDetails());
    }
}