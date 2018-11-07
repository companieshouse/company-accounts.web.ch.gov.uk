package uk.gov.companieshouse.web.accounts.enumeration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.exception.MissingMessageKeyException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ValidationMessageTests {

    private static final String KNOWN_ERROR_STRING = "invalid_character";
    private static final String UNKNOWN_ERROR_STRING = "non_existent_error_string";

    @Test
    @DisplayName("Tests no exception thrown if message key mapping exists")
    public void testValidationMessageMappingSuccess() {

        Assertions.assertAll(() ->
                ValidationMessage.getMessageKeyForApiError(KNOWN_ERROR_STRING));
    }

    @Test
    @DisplayName("Tests MissingMessageKeyException thrown if message key mapping does not exist")
    public void testValidationMessageMappingFailure() {

        assertThrows(MissingMessageKeyException.class, () ->
                ValidationMessage.getMessageKeyForApiError(UNKNOWN_ERROR_STRING));
    }
}
