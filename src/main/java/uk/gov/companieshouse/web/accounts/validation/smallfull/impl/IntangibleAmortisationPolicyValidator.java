package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class IntangibleAmortisationPolicyValidator {

    private static final String INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH =
            "intangibleAmortisationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.intangible_fixed_assets_amortisation_policy";

    public List<ValidationError> validateIntangibleAmortisationPolicy(
            IntangibleAmortisationPolicy intangibleAmortisationPolicy) {
        List<ValidationError> validationErrors = new ArrayList<>();
        if (intangibleAmortisationPolicyNotProvided(intangibleAmortisationPolicy)) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH);
            validationError.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(validationError);
        }
        return validationErrors;
    }

    private boolean intangibleAmortisationPolicyNotProvided(
            IntangibleAmortisationPolicy intangibleAmortisationPolicy) {

        return intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy() &&
                StringUtils.isBlank(intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails());
    }
}
