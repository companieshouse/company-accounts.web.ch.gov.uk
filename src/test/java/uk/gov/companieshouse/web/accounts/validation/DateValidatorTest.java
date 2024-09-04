package uk.gov.companieshouse.web.accounts.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.smallfull.Date;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DateValidatorTest {

    private static final String FIELD_PATH = "fieldPath";
    private static final String JSON_PATH_SUFFIX = ".json_path_suffix";
    private static final String DATE_MISSING = "validation.date.missing";
    private static final String DATE_INCOMPLETE = "validation.date.incomplete";
    private static final String DATE_FORMAT_INVALID = "validation.date.format";
    private static final String DATE_INVALID = "validation.date.nonExistent";
    private final DateValidator dateValidator = new DateValidator();

    @Test
    @DisplayName("Validate Date - No Fields Provided")
    void validateApprovalDateNoFieldsProvided() {

        Date date = new Date();

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_MISSING + JSON_PATH_SUFFIX, validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Day Not Provided")
    void validateApprovalDateDayNotProvided() {

        Date date = new Date();
        date.setMonth("12");
        date.setYear("2018");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE + JSON_PATH_SUFFIX, validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Month Not Provided")
    void validateApprovalDateMonthNotProvided() {

        Date date = new Date();
        date.setDay("12");
        date.setYear("2018");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE + JSON_PATH_SUFFIX, validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Year Not Provided")
    void validateApprovalDateYearNotProvided() {

        Date date = new Date();
        date.setDay("12");
        date.setMonth("12");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INCOMPLETE + JSON_PATH_SUFFIX, validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Invalid Day Format")
    void validateApprovalDateInvalidDayFormat() {

        Date date = new Date();
        date.setDay("1st");
        date.setMonth("3");
        date.setYear("2018");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_FORMAT_INVALID + JSON_PATH_SUFFIX,
                validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Invalid Month Format")
    void validateApprovalDateInvalidMonthFormat() {

        Date date = new Date();
        date.setDay("12");
        date.setMonth("Mar");
        date.setYear("2018");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_FORMAT_INVALID + JSON_PATH_SUFFIX,
                validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Invalid Year Format")
    void validateApprovalDateInvalidYearFormat() {

        Date date = new Date();
        date.setDay("12");
        date.setMonth("3");
        date.setYear("18");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_FORMAT_INVALID + JSON_PATH_SUFFIX,
                validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Invalid Date")
    void validateApprovalDateInvalidDate() {

        Date date = new Date();
        date.setDay("12");
        date.setMonth("13");
        date.setYear("2018");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        assertEquals(DATE_INVALID, validationErrors.get(0).getMessageKey());
        assertEquals(FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Validate Date - Valid Date")
    void validateApprovalDateValidDate() {

        Date date = new Date();
        date.setDay("12");
        date.setMonth("10");
        date.setYear("2018");

        List<ValidationError> validationErrors = dateValidator.validateDate(date, FIELD_PATH,
                JSON_PATH_SUFFIX);

        assertEquals(0, validationErrors.size());
    }
}
