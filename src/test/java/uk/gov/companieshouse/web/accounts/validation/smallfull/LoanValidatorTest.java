package uk.gov.companieshouse.web.accounts.validation.smallfull;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoanValidatorTest {

    private LoanValidator validator = new LoanValidator();

    private static final String LOAN_TO_ADD = "loanToAdd";

    private static final String DIRECTOR_NAME = LOAN_TO_ADD + ".directorName";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.loan.director_name";

    @Test
    @DisplayName("Validate loan to add - success")
    void validateLoanToAddSuccess() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(loanToAdd);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate director to add - missing name")
    void validateDirectorToAddMissingName() {

        LoanToAdd loanToAdd = new LoanToAdd();

        List<ValidationError> validationErrors = validator.validateDirectorToAdd(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DIRECTOR_NAME, validationErrors.get(0).getFieldPath());
        assertEquals(NAME_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }
}