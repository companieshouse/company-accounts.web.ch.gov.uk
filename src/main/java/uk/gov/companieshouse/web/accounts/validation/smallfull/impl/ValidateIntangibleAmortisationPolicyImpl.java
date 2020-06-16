package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.validation.smallfull.ValidateIntangibleAmortisationPolicy;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidateIntangibleAmortisationPolicyImpl implements ConstraintValidator<ValidateIntangibleAmortisationPolicy, IntangibleAmortisationPolicy> {

    @Override
    public boolean isValid(IntangibleAmortisationPolicy intangibleAmortisationPolicy, ConstraintValidatorContext context) {

        return !intangibleAmortisationPolicyNotProvided(intangibleAmortisationPolicy);
    }
    private boolean intangibleAmortisationPolicyNotProvided(IntangibleAmortisationPolicy intangibleAmortisationPolicy) {
        return intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy() &&
                StringUtils.isBlank(intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails());
    }
}
