package uk.gov.companieshouse.web.accounts.validation.smallfull;

import uk.gov.companieshouse.web.accounts.validation.smallfull.impl.ValidateTangibleDepreciationPolicyImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Constraint(validatedBy = ValidateTangibleDepreciationPolicyImpl.class)
public @interface ValidateTangibleDepreciationPolicy {

    String message() default "{validation.length.minInvalid.accounting_policies.tangible_depreciation_policy}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
