package uk.gov.companieshouse.web.accounts.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

/**
 * Defines the previous controller in the linear journey to support the 'back' page
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PreviousController {
    Class<? extends BaseController>[] value();
}
