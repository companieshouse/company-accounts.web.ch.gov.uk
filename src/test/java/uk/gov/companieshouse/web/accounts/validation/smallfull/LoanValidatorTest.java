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
    private final LoanValidator validator = new LoanValidator();

    private static final String LOAN_TO_ADD = "loanToAdd";

    private static final String DIRECTOR_NAME = LOAN_TO_ADD + ".directorName";

    private static final String DESCRIPTION = LOAN_TO_ADD + ".description";
    private static final String DESCRIPTION_NOT_PRESENT = "validation.element.missing.loanToAdd.description";

    private static final String BALANCE_AT_START = LOAN_TO_ADD + ".breakdown.balanceAtPeriodStart";
    private static final String BAS_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodStart";

    private static final String BALANCE_AT_END = LOAN_TO_ADD + ".breakdown.balanceAtPeriodEnd";
    private static final String BAE_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodEnd";

    private static final String AT_LEAST_ONE_LOAN_REQUIRED = "validation.addOrRemoveLoans.oneRequired";
    private static final String DIRECTOR_REPORT_PRESENT_NAME_MISSING = "validation.element.missing.loanToAdd.directorName";

    @Test
    @DisplayName("Validate loan to add for multi year filer - success")
    void validateLoanToAddForMultiYearFilerSuccess() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, true, true);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add for single year filer - success")
    void validateLoanToAddForSingleYearFilerSuccess() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(false, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, false, true);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add for multi year filer - missing director name")
    void validateLoanToAddForMultiYearFilerMissingDirectorName() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, true, false);

        assertTrue(validationErrors.isEmpty());
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("Validate loan to add for single year filer - missing director name")
    void validateLoanToAddForSingleYearMissingDirectorName() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, false, false);

        assertTrue(validationErrors.isEmpty());
        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("Validate loan to add for single year filer - missing director name and directors report present")
    void validateLoanToAddForSingleYearMissingDirectorNameWithDirectorsReportPresent() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, false, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DIRECTOR_REPORT_PRESENT_NAME_MISSING, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add for multi year filer - missing description")
    void validateLoanToAddForMultiYearFilerMissingDescription() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, true, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DESCRIPTION, validationErrors.get(0).getFieldPath());
        assertEquals(DESCRIPTION_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add for single year filer - missing description")
    void validateLoanToAddForSingleYearFilerMissingDescription() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setBreakdown(createBreakdown(true, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, false, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(DESCRIPTION, validationErrors.get(0).getFieldPath());
        assertEquals(DESCRIPTION_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add for multi year filer - missing period start")
    void validateLoanToAddForMultiYearFilerMissingPeriodStart() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(false, true));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, true, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(BALANCE_AT_START, validationErrors.get(0).getFieldPath());
        assertEquals(BAS_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add for multi year filer - missing period end")
    void validateLoanToAddForMultiYearFilerMissingPeriodEnd() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, false));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, true, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(BALANCE_AT_END, validationErrors.get(0).getFieldPath());
        assertEquals(BAE_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add for single year filer - missing period end")
    void validateLoanToAddForSingleYearFilerMissingPeriodEnd() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setDescription(DESCRIPTION);
        loanToAdd.setBreakdown(createBreakdown(true, false));

        List<ValidationError> validationErrors = validator.validateLoanToAdd(loanToAdd, false, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(BALANCE_AT_END, validationErrors.get(0).getFieldPath());
        assertEquals(BAE_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add for multi year filer - Empty resource")
    void validateLoanToAddForMultiYearFilerIsEmpty() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setBreakdown(createBreakdown(false, false));

        boolean isEmpty = validator.isEmptyResource(loanToAdd, true);

        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Validate loan to add for single year filer - Empty resource")
    void validateLoanToAddForSingleYearFilerIsEmpty() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setBreakdown(createBreakdown(false, false));

        boolean isEmpty = validator.isEmptyResource(loanToAdd, false);

        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Validate loan to add for multi year filer - Not Empty resource")
    void validateLoanToAddForMultiYearFilerIsNotEmpty() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setBreakdown(createBreakdown(false, false));

        boolean isEmpty = validator.isEmptyResource(loanToAdd, true);

        assertFalse(isEmpty);
    }

    @Test
    @DisplayName("Validate loan to add for single year filer - Not Empty resource")
    void validateLoanToAddForSingleYearFilerIsNotEmpty() {
        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);
        loanToAdd.setBreakdown(createBreakdown(false, false));

        boolean isEmpty = validator.isEmptyResource(loanToAdd, false);

        assertFalse(isEmpty);
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, no fields populated for multi year filer")
    void validateAtLeastOneLoanNoFieldsFilledInForMultiYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();
        addOrRemoveLoans.setIsMultiYearFiler(true);

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, false);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(LOAN_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(AT_LEAST_ONE_LOAN_REQUIRED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add- At least one loan, no fields populated for single year filer")
    void validateAtLeastOneLoanNoFieldsFilledInForSingleYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();
        addOrRemoveLoans.setIsMultiYearFiler(false);

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, false);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(LOAN_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(AT_LEAST_ONE_LOAN_REQUIRED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, no fields populated - isSingle director true for multi year filer")
    void validateAtLeastOneLoanNoFieldsFilledInIsSingleDirectorTrueForMultiYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(true);
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(LOAN_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(AT_LEAST_ONE_LOAN_REQUIRED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, no fields populated - isSingle director true for single year filer")
    void validateAtLeastOneLoanNoFieldsFilledInIsSingleDirectorTrueForSingleYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(false);
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(LOAN_TO_ADD, validationErrors.get(0).getFieldPath());
        assertEquals(AT_LEAST_ONE_LOAN_REQUIRED, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated for multi year filer")
    void validateAtLeastOneLoanFieldsFilledInForMultiYear() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(true);

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);

        addOrRemoveLoans.setLoanToAdd(loanToAdd);
        
        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, false);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated for single year filer")
    void validateAtLeastOneLoanFieldsFilledInForSingleYear() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(false);

        LoanToAdd loanToAdd = new LoanToAdd();
        loanToAdd.setDirectorName(DIRECTOR_NAME);

        addOrRemoveLoans.setLoanToAdd(loanToAdd);
        
        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, false);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated - isSingleDirector true for multi year filer")
    void validateAtLeastOneLoanFieldsFilledInIsSingleDirectorTrueForMultiYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(true);
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);
        addOrRemoveLoans.getLoanToAdd().setDescription(DESCRIPTION);

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated - isSingleDirector true for single year filer")
    void validateAtLeastOneLoanFieldsFilledInIsSingleDirectorTrueForSingleFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(false);
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);
        addOrRemoveLoans.getLoanToAdd().setDescription(DESCRIPTION);

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated - isSingleDirector true for multi year filer")
    void validateAtLeastOneLoanPeriodStartFieldsFilledInIsSingleDirectorTrueForMultiYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(true);
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);
        addOrRemoveLoans.getLoanToAdd().setBreakdown(createBreakdown(true, false));

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated - isSingleDirector true for single year filer")
    void validateAtLeastOneLoanPeriodStartFieldsFilledInIsSingleDirectorTrueForSingleYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(false);
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);
        addOrRemoveLoans.getLoanToAdd().setBreakdown(createBreakdown(false, true));

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated - isSingleDirector true for multi year filer")
    void validateAtLeastOneLoanPeriodEndFieldsFilledInIsSingleDirectorTrueForMultiYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(true);
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);
        addOrRemoveLoans.getLoanToAdd().setBreakdown(createBreakdown(false, true));

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add - At least one loan, a field populated - isSingleDirector true for single year filer")
    void validateAtLeastOneLoanPeriodEndFieldsFilledInIsSingleDirectorTrueForSingleYearFiler() {
        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(false);
        addOrRemoveLoans.getLoanToAdd().setBreakdown(createBreakdown(false, true));

        List<ValidationError> validationErrors = validator.validateAtLeastOneLoan(addOrRemoveLoans, true);

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