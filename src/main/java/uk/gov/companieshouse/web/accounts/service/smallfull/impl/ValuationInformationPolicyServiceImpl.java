package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ValuationInformationPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class ValuationInformationPolicyServiceImpl implements ValuationInformationPolicyService {

    @Autowired
    private AccountingPoliciesService accountingPoliciesService;

    @Autowired
    private AccountingPoliciesTransformer accountingPoliciesTransformer;

    private static final String VALUATION_INFORMATION_POLICY_FIELD_PATH =
            "valuationInformationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.valuation_information_and_policy";

    /**
     * {@inheritDoc}
     */
    @Override
    public ValuationInformationPolicy getValuationInformationPolicy(String transactionId,
            String companyAccountsId) throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi =
                accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        return accountingPoliciesTransformer.getValuationInformationPolicy(accountingPoliciesApi);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> submitValuationInformationPolicy(String transactionId,
            String companyAccountsId, ValuationInformationPolicy valuationInformationPolicy)
            throws ServiceException {

        List<ValidationError> validationErrors = validateValuationInformationPolicy(valuationInformationPolicy);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        AccountingPoliciesApi accountingPoliciesApi =
                accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        accountingPoliciesTransformer
                .setValuationInformationPolicy(valuationInformationPolicy, accountingPoliciesApi);

        return accountingPoliciesService
                .updateAccountingPoliciesApi(transactionId, companyAccountsId, accountingPoliciesApi);
    }

    /**
     * Validate a submitted valuation information policy
     * @param valuationInformationPolicy The policy to validate
     * @return a list of validation errors, or an empty array list if none are present
     */
    private List<ValidationError> validateValuationInformationPolicy(ValuationInformationPolicy valuationInformationPolicy) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (valuationInformationPolicyNotProvided(valuationInformationPolicy)) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(VALUATION_INFORMATION_POLICY_FIELD_PATH);
            validationError.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(validationError);
        }

        return validationErrors;
    }

    /**
     * Check whether a valuation information has not been provided
     * @param valuationInformationPolicy The policy to check
     * @return true if the 'include policy' boolean is set to true, but no details are provided
     */
    private boolean valuationInformationPolicyNotProvided(ValuationInformationPolicy valuationInformationPolicy) {

        return valuationInformationPolicy.getIncludeValuationInformationPolicy() &&
                StringUtils.isBlank(valuationInformationPolicy.getValuationInformationPolicyDetails());
    }
}
