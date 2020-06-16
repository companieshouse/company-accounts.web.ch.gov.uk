package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class OtherAccountingPoliciesValidator {

    private static final String OTHER_ACCOUNTING_POLICY_FIELD_PATH =
            "otherAccountingPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.other_accounting_policy";

    public List<ValidationError> validateOtherAccountingPolicy(
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

    private boolean otherAccountingPolicyNotProvided(
            OtherAccountingPolicy otherAccountingPolicy) {

        return otherAccountingPolicy.getHasOtherAccountingPolicySelected() &&
                StringUtils.isBlank(otherAccountingPolicy.getOtherAccountingPolicyDetails());
    }
}
