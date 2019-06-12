package uk.gov.companieshouse.web.accounts.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import uk.gov.companieshouse.web.accounts.controller.BaseController;

/**
 * Defines the next controller to navigate to in the linear journey
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NextController {
    Class<? extends BaseController>[] value();
}
