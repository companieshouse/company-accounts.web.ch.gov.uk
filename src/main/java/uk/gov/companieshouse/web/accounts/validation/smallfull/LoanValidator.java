package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class LoanValidator {

    private static final String LOAN_TO_ADD = "loanToAdd";

    private static final String DIRECTOR_NAME = LOAN_TO_ADD + ".directorName";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.loanToAdd.directorName";
    private static final String DESCRIPTION = LOAN_TO_ADD + ".description";
    private static final String DESCRIPTION_NOT_PRESENT = "validation.element.missing.loanToAdd.description";
    private static final String BREAKDOWN_BALANCE_AT_PERIOD_END = LOAN_TO_ADD + ".breakdown.balanceAtPeriodEnd";
    private static final String BREAKDOWN_BALANCE_AT_PERIOD_END_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodEnd";

    public List<ValidationError> validateLoan(LoanToAdd loanToAdd) {
        List<ValidationError> validationErrors = new ArrayList<>();
        
        validateDirectorToAdd(validationErrors, loanToAdd);
        validateBreakdown(validationErrors, loanToAdd);
        
        return validationErrors;
    }

    private List<ValidationError> validateDirectorToAdd(List<ValidationError> validationErrors, LoanToAdd loanToAdd) {

        if (StringUtils.isBlank(loanToAdd.getDirectorName())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(DIRECTOR_NAME);
            error.setMessageKey(NAME_NOT_PRESENT);
            validationErrors.add(error);
        }

        if (StringUtils.isBlank(loanToAdd.getDescription())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(DESCRIPTION);
            error.setMessageKey(DESCRIPTION_NOT_PRESENT);
            validationErrors.add(error);
        }

        return validationErrors;
    }
    
    private List<ValidationError> validateBreakdown(List<ValidationError> validationErrors,
            LoanToAdd loanToAdd) {

        if (loanToAdd.getBreakdown().getBalanceAtPeriodEnd() == null) {

            ValidationError error = new ValidationError();
            error.setFieldPath(BREAKDOWN_BALANCE_AT_PERIOD_END);
            error.setMessageKey(BREAKDOWN_BALANCE_AT_PERIOD_END_NOT_PRESENT);
            validationErrors.add(error);
        }

        return validationErrors;
    }
}
