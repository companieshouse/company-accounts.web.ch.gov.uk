package uk.gov.companieshouse.web.accounts.enumeration;

import org.apache.commons.lang.StringUtils;
import uk.gov.companieshouse.web.accounts.exception.MissingMessageKeyException;

import java.util.HashMap;
import java.util.Map;


/**
 * The {@code ValidationMessage} enumeration provides mappings between API
 * validation error strings and validation message keys, for use when
 * binding validation errors.
 */
public enum ValidationMessage {

    // API validation error to message key mappings
    VALUE_OUTSIDE_RANGE("value_outside_range", "validation.range.outside"),
    INVALID_CHARACTER("invalid_character", "validation.character.invalid"),
    INCORRECT_TOTAL("incorrect_total", "validation.total.invalid");

    private String messageKey;
    private String apiError;

    private static Map<String, String> mapping = initializeMapping();

    ValidationMessage(String apiError, String messageKey) {
        this.apiError = apiError;
        this.messageKey = messageKey;
    }

    /**
     * Returns a message key associated with the {@code apiError} parameter
     * for use when binding API validation errors to model object fields.
     *
     * @param apiError the api validation error string
     * @return         the message key
     */
    public static String getMessageKeyForApiError(String apiError) {
        String key = mapping.get(apiError);
        if (StringUtils.isBlank(key)) {
            throw new MissingMessageKeyException("No message key mapping for API validation error: " + apiError);
        }
        return key;
    }

    /**
     * Returns a map of API validation error strings mapped to message keys.
     *
     * @return a map of validation errors mapped to message keys
     */
    private static Map<String, String> initializeMapping() {
        HashMap<String, String> mapping = new HashMap<>();
        for (ValidationMessage validationMessage : ValidationMessage.values()) {
            mapping.put(validationMessage.apiError, validationMessage.messageKey);
        }
        return mapping;
    }

}
