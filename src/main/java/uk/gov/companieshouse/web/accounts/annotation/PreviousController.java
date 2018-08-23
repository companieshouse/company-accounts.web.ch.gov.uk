package uk.gov.companieshouse.web.accounts.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PreviousController {
    Class value();
}
