package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class TurnoverPolicyValidator {

    private static final String TURNOVER_POLICY_DETAILS_FIELD_PATH = "turnoverPolicyDetails";
    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE = "validation.length.minInvalid.accounting_policies.turnover_policy";

    public List<ValidationError> validateTurnoverPolicy(
            TurnoverPolicy turnoverPolicy) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (turnoverPolicyNotProvided(turnoverPolicy)) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(TURNOVER_POLICY_DETAILS_FIELD_PATH);
            validationError.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(validationError);
        }
        return validationErrors;
    }

    private boolean turnoverPolicyNotProvided(
            TurnoverPolicy turnoverPolicy) {

        return turnoverPolicy.getIsIncludeTurnoverSelected() &&
                StringUtils.isBlank(turnoverPolicy.getTurnoverPolicyDetails());
    }
}
