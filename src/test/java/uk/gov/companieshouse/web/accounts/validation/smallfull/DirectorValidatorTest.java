package uk.gov.companieshouse.web.accounts.validation.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectorValidatorTest {

    private DirectorValidator validator = new DirectorValidator();

    private static final String DIRECTOR_NAME = "directorName";

    private static final String NAME = "directorToAdd.name";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.director.name";

    private static final String WAS_DIRECTOR_APPOINTED = "directorToAdd.wasDirectorAppointedDuringPeriod";
    private static final String APPOINTED_NOT_SELECTED = "validation.directorToAdd.appointment.selectionNotMade";

    private static final String DID_DIRECTOR_RESIGN = "directorToAdd.didDirectorResignDuringPeriod";
    private static final String RESIGNATION_NOT_SELECTED = "validation.directorToAdd.resignation.selectionNotMade";

    @Test
    @DisplayName("Validate director to add - success")
    void validateDirectorToAddSuccess() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(DIRECTOR_NAME);
        directorToAdd.setWasDirectorAppointedDuringPeriod(true);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate director to add - missing name")
    void validateDirectorToAddMissingName() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setWasDirectorAppointedDuringPeriod(true);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(NAME, validationErrors.get(0).getFieldPath());
        assertEquals(NAME_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate director to add - missing was director appointed")
    void validateDirectorToAddMissingWasDirectorAppointed() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(DIRECTOR_NAME);
        directorToAdd.setDidDirectorResignDuringPeriod(false);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(WAS_DIRECTOR_APPOINTED, validationErrors.get(0).getFieldPath());
        assertEquals(APPOINTED_NOT_SELECTED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate director to add - missing did director resign")
    void validateDirectorToAddMissingDidDirectorResign() {

        DirectorToAdd directorToAdd = new DirectorToAdd();
        directorToAdd.setName(DIRECTOR_NAME);
        directorToAdd.setWasDirectorAppointedDuringPeriod(true);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(directorToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DID_DIRECTOR_RESIGN, validationErrors.get(0).getFieldPath());
        assertEquals(RESIGNATION_NOT_SELECTED, validationErrors.get(0).getMessageKey());
    }
}
