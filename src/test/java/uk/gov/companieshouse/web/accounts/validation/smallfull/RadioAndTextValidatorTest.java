package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RadioAndTextValidatorTest {
    @Mock
    private BindingResult bindingResult;

    private static final String ERROR_LOCATION = "turnoverErrorLocation";
    private static final String ERROR_MESSAGE = "turnoverErrorMessage";
    private static final String DETAILS = "details";

    @Test
    @DisplayName("RadioAndTextValidator - verify method never gets called with unexpected arguments")
    void verifyMethodNeverGetCalledWithUnexpectedArguments() {
        RadioAndTextValidator radioAndTextValidator = new RadioAndTextValidator();

        radioAndTextValidator.validate(true, DETAILS, bindingResult, ERROR_MESSAGE, ERROR_LOCATION);

        verify(bindingResult, never()).rejectValue( ERROR_LOCATION, ERROR_MESSAGE, null, null);

        radioAndTextValidator.validate(false, null, bindingResult, ERROR_MESSAGE, ERROR_LOCATION);

        verify(bindingResult, never()).rejectValue( ERROR_LOCATION, ERROR_MESSAGE, null, null);
    }

    @Test
    @DisplayName("RadioAndTextValidator - verify method gets called with expected arguments")
    void verifyMethodNeverGetCalledWithExpectedArguments() {
        RadioAndTextValidator radioAndTextValidator = new RadioAndTextValidator();

        radioAndTextValidator.validate(true, null, bindingResult, ERROR_MESSAGE, ERROR_LOCATION);

        verify(bindingResult).rejectValue( ERROR_LOCATION, ERROR_MESSAGE, null, null);
    }
}