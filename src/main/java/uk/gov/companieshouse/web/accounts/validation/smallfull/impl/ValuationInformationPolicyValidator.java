package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class ValuationInformationPolicyValidator {

    private static final String VALUATION_INFORMATION_POLICY_FIELD_PATH =
            "valuationInformationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.valuation_information_and_policy";

    public List<ValidationError> validateValuationInformationPolicy(ValuationInformationPolicy valuationInformationPolicy) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (isValuationInformationPolicyNotProvided(valuationInformationPolicy)) {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(VALUATION_INFORMATION_POLICY_FIELD_PATH);
            validationError.setMessageKey(INVALID_STRING_SIZE_ERROR_MESSAGE);
            validationErrors.add(validationError);
        }

        return validationErrors;
    }

    private boolean isValuationInformationPolicyNotProvided(ValuationInformationPolicy valuationInformationPolicy) {

        return valuationInformationPolicy.getIncludeValuationInformationPolicy() &&
                StringUtils.isBlank(valuationInformationPolicy.getValuationInformationPolicyDetails());
    }
}
