package uk.gov.companieshouse.web.accounts.validation;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The {@code ValidationError} class encapsulates the data that represents
 * an API validation error, and is used when binding validation errors to
 * presentation model fields.
 */
@Getter
@Setter
public class ValidationError {

    private String messageKey;
    private Map<String, String> messageArguments;
    private String fieldPath;
}
