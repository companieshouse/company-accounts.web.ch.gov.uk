package uk.gov.companieshouse.web.accounts.validation.smallfull;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
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

    private LoanValidator validator = new LoanValidator();

    private static final String LOAN_TO_ADD = "loanToAdd";

    private static final String DIRECTOR_NAME = LOAN_TO_ADD + ".directorName";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.loanToAdd.directorName";

    private static final String DESCRIPTION = LOAN_TO_ADD + ".description";
    private static final String DESCRIPTION_NOT_PRESENT = "validation.element.missing.loanToAdd.description";

    private static final String BALANCE_AT_START = LOAN_TO_ADD + ".breakdown.balanceAtPeriodStart";
    private static final String BAS_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodStart";

    private static final String BALANCE_AT_END = LOAN_TO_ADD + ".breakdown.balanceAtPeriodEnd";
    private static final String BAE_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodEnd";

    private static final String AT_LEAST_ONE_LOAN_REQUIRED = "validation.addOrRemoveLoans.oneRequired";

    @Test
    @DisplayName("Validate loan to add - success")
    void validateLoanToAddToAddSuccess() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - missing director name")
    void validateLoanToAddToAddMissingDirectorName() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DIRECTOR_NAME, validationErrors.get(0).getFieldPath());
        assertEquals(NAME_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - missing description")
    void validateLoanToAddToAddMissingDescription() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DESCRIPTION, validationErrors.get(0).getFieldPath());
        assertEquals(DESCRIPTION_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - missing period start")
    void validateLoanToAddToAddMissingPeriodStart() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(false, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(BALANCE_AT_START, validationErrors.get(0).getFieldPath());
        assertEquals(BAS_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - missing period end")
    void validateLoanToAddToAddMissingPeriodEnd() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, false));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(BALANCE_AT_END, validationErrors.get(0).getFieldPath());
        assertEquals(BAE_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - Empty resource")
    void validateLoanToAddToAddIsEmpty() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setBreakdown(createBreakdown(false, false));

        boolean isEmpty = validator.isEmptyResource(loanToAdd);

        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Validate loan to add - Not Empty resource")
    void validateLoanToAddToAddIsNotEmpty() {

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setBreakdown(createBreakdown(false, false));

        boolean isEmpty = validator.isEmptyResource(loanToAdd);

        assertFalse(isEmpty);
    }

    @Test
    @DisplayName("Validate loan to add- At least one loan, no fields populated")
    void validateAtLeastOneLoanNoFieldsFilledIn() {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(LOAN_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(AT_LEAST_ONE_LOAN_REQUIRED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add- At least one loan, a field populated")
    void validateAtLeastOneLoanFieldsFilledIn() {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);

        addOrRemoveLoans.setLoanToAdd(loanToAdd);
        
        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
    }

    private Breakdown createBreakdown(boolean includePeriodStart, boolean includePeriodEnd) {

        Breakdown validBreakdown = new Breakdown();

        if (includePeriodStart) {
            validBreakdown.setBalanceAtPeriodStart(1L);
        }

        if (includePeriodEnd) {
            validBreakdown.setBalanceAtPeriodEnd(1L);
        }

        return validBreakdown;
    }

}