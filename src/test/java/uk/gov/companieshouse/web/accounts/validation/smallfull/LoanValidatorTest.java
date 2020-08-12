package uk.gov.companieshouse.web.accounts.validation.smallfull;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Breakdown;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoanValidatorTest {

    private static final String LOAN_TO_ADD = "loanToAdd";

    private static final String DIRECTOR_NAME = LOAN_TO_ADD + ".directorName";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.loanToAdd.directorName";
    private static final String DESCRIPTION = LOAN_TO_ADD + ".description";
    private static final String DESCRIPTION_NOT_PRESENT = "validation.element.missing.loanToAdd.description";
    private static final Long BREAKDOWN_BALANCE_AT_PERIOD_END_VALUE = 5000L;
    private static final String BREAKDOWN_BALANCE_AT_PERIOD_END = LOAN_TO_ADD + ".breakdown.balanceAtPeriodEnd";
    private static final String BREAKDOWN_BALANCE_AT_PERIOD_END_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodEnd";

    private LoanValidator validator = new LoanValidator();

    @Test
    @DisplayName("Validate loan to add - success")
    void validateLoanToAddSuccess() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(new Breakdown());
        loanToAdd.getBreakdown().setBalanceAtPeriodEnd(BREAKDOWN_BALANCE_AT_PERIOD_END_VALUE);

        List<ValidationError> validationErrors = validator.validateLoan(loanToAdd);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - missing director name")
    void validateLoanToAddMissingDirectorName() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(new Breakdown());
        loanToAdd.getBreakdown().setBalanceAtPeriodEnd(BREAKDOWN_BALANCE_AT_PERIOD_END_VALUE);

        List<ValidationError> validationErrors = validator.validateLoan(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DIRECTOR_NAME, validationErrors.get(0).getFieldPath());
        assertEquals(NAME_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - missing description")
    void validateLoanToAddMissingDescription() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setBreakdown(new Breakdown());
        loanToAdd.getBreakdown().setBalanceAtPeriodEnd(BREAKDOWN_BALANCE_AT_PERIOD_END_VALUE);

        List<ValidationError> validationErrors = validator.validateLoan(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DESCRIPTION, validationErrors.get(0).getFieldPath());
        assertEquals(DESCRIPTION_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - missing balance at period end")
    void validateLoanToAddMissingBalanceAtPeriodEnd() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setBreakdown(new Breakdown());
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);

        List<ValidationError> validationErrors = validator.validateLoan(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(BREAKDOWN_BALANCE_AT_PERIOD_END, validationErrors.get(0).getFieldPath());
        assertEquals(BREAKDOWN_BALANCE_AT_PERIOD_END_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }
}