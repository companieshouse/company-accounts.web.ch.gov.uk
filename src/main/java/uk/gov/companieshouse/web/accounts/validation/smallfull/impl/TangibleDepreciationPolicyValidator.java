package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class TangibleDepreciationPolicyValidator {

    private static final String TANGIBLE_DEPRECIATION_POLICY_FIELD_PATH =
            "tangibleDepreciationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.tangible_fixed_assets_depreciation_policy";

    public List<ValidationError> validateTangibleDepreciationPolicy(
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

    private boolean tangibleDepreciationPolicyNotProvided(
            TangibleDepreciationPolicy tangibleDepreciationPolicy) {

        return tangibleDepreciationPolicy.getHasTangibleDepreciationPolicySelected() &&
                StringUtils.isBlank(tangibleDepreciationPolicy.getTangibleDepreciationPolicyDetails());
    }
}
