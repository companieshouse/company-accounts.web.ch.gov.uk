package uk.gov.companieshouse.web.accounts.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use the {@code ValidationPeriodMapping} annotation to associate an API
 * error for a resource period JSON path with a model object field for the
 * purposes of validation.
 *
 * @see ValidationModel
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationPeriodMapping {

    String value();
}
