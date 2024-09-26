package uk.gov.companieshouse.web.accounts.enumeration;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.exception.MissingMessageKeyException;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidationMessageTests {

    private static final String KNOWN_ERROR_STRING = "invalid_character";
    private static final String UNKNOWN_ERROR_STRING = "non_existent_error_string";

    @Test
    @DisplayName("Tests no exception thrown if message key mapping exists")
    void testValidationMessageMappingSuccess() {

        Assertions.assertAll(() ->
                ValidationMessage.getMessageForApiError(KNOWN_ERROR_STRING));
    }

    @Test
    @DisplayName("Tests MissingMessageKeyException thrown if message key mapping does not exist")
    void testValidationMessageMappingFailure() {

        assertThrows(MissingMessageKeyException.class, () ->
                ValidationMessage.getMessageForApiError(UNKNOWN_ERROR_STRING));
    }
}
