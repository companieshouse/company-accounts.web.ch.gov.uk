package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TurnoverPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Service
public class TurnoverPolicyServiceImpl implements TurnoverPolicyService {

    private static final String TURNOVER_POLICY_DETAILS_FIELD_PATH = "turnoverPolicyDetails";
    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE = "validation.length.minInvalid.accounting_policies.turnover_policy";
    private final AccountingPoliciesService accountingPoliciesService;
    private final AccountingPoliciesTransformer accountingPoliciesTransformer;

    @Autowired
    public TurnoverPolicyServiceImpl(
        AccountingPoliciesService accountingPoliciesService,
        AccountingPoliciesTransformer accountingPoliciesTransformer) {
        this.accountingPoliciesService = accountingPoliciesService;
        this.accountingPoliciesTransformer = accountingPoliciesTransformer;
    }

    @Override
    public TurnoverPolicy getTurnOverPolicy(String transactionId, String companyAccountsId)
        throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        return accountingPoliciesTransformer.getTurnoverPolicy(accountingPoliciesApi);
    }

    @Override
    public List<ValidationError> postTurnoverPolicy(String transactionId, String companyAccountsId,
        TurnoverPolicy turnoverPolicy) throws ServiceException {

        List<ValidationError> validationErrors = validateTurnoverPolicyDetails(turnoverPolicy);

        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        AccountingPoliciesApi accountingPoliciesApi =
            accountingPoliciesService.getAccountingPoliciesApi(transactionId, companyAccountsId);

        accountingPoliciesTransformer.setTurnoverPolicy(turnoverPolicy, accountingPoliciesApi);

        return accountingPoliciesService
            .updateAccountingPoliciesApi(transactionId, companyAccountsId,
                accountingPoliciesApi);
    }

    /**
     * Validate the turnover policy details' length before calling the api. This string field should
     * not be empty or contain empty characters.
     *
     * @param turnoverPolicy The turnover policy model that needs to be validated
     * @return A list of validation errors. Or empty when turnoverPolicy contains valid information
     */
    private List<ValidationError> validateTurnoverPolicyDetails(TurnoverPolicy turnoverPolicy) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (turnoverPolicy.getIsIncludeTurnoverSelected() &&
            IsRequiredFieldEmpty(turnoverPolicy.getTurnoverPolicyDetails())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(TURNOVER_POLICY_DETAILS_FIELD_PATH);
            error.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    /**
     * Check if field contains invalid information when it is not null: empty or contain empty
     * characters.
     *
     * @param turnoverPolicyDetails the string field that needs to be assessed
     * @return
     */
    private boolean IsRequiredFieldEmpty(String turnoverPolicyDetails) {
        return turnoverPolicyDetails != null && StringUtils.trim(turnoverPolicyDetails).isEmpty();
    }
}
