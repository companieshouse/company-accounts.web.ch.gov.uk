package uk.gov.companieshouse.web.accounts.validation.smallfull;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Component
public class LoanValidator {

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

    public List<ValidationError> validateLoanToAdd(LoanToAdd loanToAdd) {

        List<ValidationError> validationErrors = new ArrayList<>();

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

        if (loanToAdd.getBreakdown().getBalanceAtPeriodStart() == null) {

            ValidationError error = new ValidationError();
            error.setFieldPath(BALANCE_AT_START);
            error.setMessageKey(BAS_NOT_PRESENT);
            validationErrors.add(error);
        }

        if (loanToAdd.getBreakdown().getBalanceAtPeriodEnd() == null) {

            ValidationError error = new ValidationError();
            error.setFieldPath(BALANCE_AT_END);
            error.setMessageKey(BAE_NOT_PRESENT);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public boolean isEmptyResource(LoanToAdd loanToAdd) {

        return loanToAdd == null || (StringUtils.isBlank(loanToAdd.getDirectorName()) &&
                StringUtils.isBlank(loanToAdd.getDescription()) &&
                loanToAdd.getBreakdown().getBalanceAtPeriodStart() == null &&
                loanToAdd.getBreakdown().getBalanceAtPeriodEnd() == null);
    }

    public List<ValidationError> validateAtLeastOneLoan(AddOrRemoveLoans addOrRemoveLoans) throws ServiceException {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (addOrRemoveLoans.getExistingLoans() == null && isEmptyResource(addOrRemoveLoans.getLoanToAdd())) {
            ValidationError error = new ValidationError();
            error.setFieldPath(LOAN_TO_ADD);
            error.setMessageKey(AT_LEAST_ONE_LOAN_REQUIRED);
            validationErrors.add(error);
        }

        return validationErrors;
    }
}
