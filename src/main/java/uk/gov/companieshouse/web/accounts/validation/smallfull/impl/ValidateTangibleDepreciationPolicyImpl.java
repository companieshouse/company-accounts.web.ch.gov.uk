package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.validation.smallfull.ValidateTangibleDepreciationPolicy;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidateTangibleDepreciationPolicyImpl implements ConstraintValidator<ValidateTangibleDepreciationPolicy, TangibleDepreciationPolicy> {

    @Override
    public boolean isValid(TangibleDepreciationPolicy tangibleDepreciationPolicy, ConstraintValidatorContext context) {

        return !tangibleDepreciationPolicyNotProvided(tangibleDepreciationPolicy);
    }
    private boolean tangibleDepreciationPolicyNotProvided(TangibleDepreciationPolicy tangibleDepreciationPolicy) {
        return tangibleDepreciationPolicy.getHasTangibleDepreciationPolicySelected() &&
                StringUtils.isBlank(tangibleDepreciationPolicy.getTangibleDepreciationPolicyDetails());
    }
}
