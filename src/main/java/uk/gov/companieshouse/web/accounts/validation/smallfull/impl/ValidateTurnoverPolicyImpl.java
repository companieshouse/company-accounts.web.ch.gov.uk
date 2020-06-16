package uk.gov.companieshouse.web.accounts.validation.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.validation.smallfull.ValidateTurnoverPolicy;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ValidateTurnoverPolicyImpl implements ConstraintValidator<ValidateTurnoverPolicy, TurnoverPolicy> {

    @Override
    public boolean isValid(TurnoverPolicy turnoverPolicy, ConstraintValidatorContext context) {

        return !turnoverPolicyNotProvided(turnoverPolicy);
    }
    private boolean turnoverPolicyNotProvided(TurnoverPolicy turnoverPolicy) {
        return turnoverPolicy.getIsIncludeTurnoverSelected() &&
                StringUtils.isBlank(turnoverPolicy.getTurnoverPolicyDetails());
    }
}