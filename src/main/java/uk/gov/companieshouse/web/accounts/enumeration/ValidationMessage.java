package uk.gov.companieshouse.web.accounts.enumeration;

import uk.gov.companieshouse.web.accounts.exception.MissingMessageKeyException;


/**
 * The {@code ValidationMessage} enumeration provides mappings between API
 * validation error strings and validation message keys, for use when
 * binding validation errors.
 */
public enum ValidationMessage {

    // API validation error to message key mappings
    VALUE_OUTSIDE_RANGE("value_outside_range", "validation.range.outside", true),
    INVALID_CHARACTER("invalid_character", "validation.characters.invalid", false),
    INVALID_INPUT_LENGTH("invalid_input_length", "validation.length.invalidInputLength", false),
    INCORRECT_TOTAL("incorrect_total", "validation.total.invalid", true),
    MANDATORY_ELEMENT_MISSING("mandatory_element_missing", "validation.element.missing", false),
    DATE_CANNOT_BE_FUTURE("date_cannot_be_in_future", "validation.date.cannotBeFuture", true),
    DATE_INVALID("date_is_invalid", "validation.date.invalid", false),
    MAX_LENGTH_EXCEEDED("max_length_exceeded", "validation.length.maxExceeded", false),
    SHAREHOLDER_FUND_MISMATCH("shareholder_funds_mismatch", "validation.shareholderFunds.mismatch", false),
    MEMBERS_FUND_MISMATCH("members_funds_mismatch", "validation.membersFunds.mismatch", false),
    CURRENT_BALANCESHEET_NOT_EQUAL("value_not_equal_to_current_period_on_balance_sheet", "current.balancesheet.not.equal", false),
    PREVIOUS_BALANCESHEET_NOT_EQUAL("value_not_equal_to_previous_period_on_balance_sheet", "previous.balancesheet.not.equal", false),
    VALUE_REQUIRED("value_required", "value.required", true),
    UNEXPECTED_DATA("unexpected_data", "validation.unexpected.data", false),
    DR_LTD_CROSS_VALIDATION("must_match_a_director_given_in_directors_report", "validation.cross_validation_dr_ltd", false),
    DATE_OUTSIDE_CURRENT_PERIOD("date_outside_current_period", "validation.date.outside.currentPeriod", false);

    private String messageKey;
    private String apiError;
    private boolean genericError;

    ValidationMessage(String apiError, String messageKey, boolean genericError) {
        this.apiError = apiError;
        this.messageKey = messageKey;
        this.genericError = genericError;
    }

    /**
     * Returns a {@code ValidationMessage} associated with the {@code apiError} parameter
     * for use when binding API validation errors to model object fields.
     *
     * @param apiError the api validation error string
     * @return         the message key
     */
    public static ValidationMessage getMessageForApiError(String apiError) {

        for (ValidationMessage validationMessage : ValidationMessage.values()) {
            if (validationMessage.apiError.equals(apiError)) {
                return validationMessage;
            }
        }

        throw new MissingMessageKeyException("No message key mapping for API validation error: " + apiError);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public boolean isGenericError() {
        return genericError;
    }
}
