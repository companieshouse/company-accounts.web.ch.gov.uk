package uk.gov.companieshouse.web.accounts.validation.smallfull;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Component
public class LoanValidator {

    private static final String LOAN_TO_ADD = "loanToAdd";

    private static final String DESCRIPTION = LOAN_TO_ADD + ".description";
    private static final String DESCRIPTION_NOT_PRESENT = "validation.element.missing.loanToAdd.description";

    private static final String BALANCE_AT_START = LOAN_TO_ADD + ".breakdown.balanceAtPeriodStart";
    private static final String BAS_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodStart";

    private static final String BALANCE_AT_END = LOAN_TO_ADD + ".breakdown.balanceAtPeriodEnd";
    private static final String BAE_NOT_PRESENT = "validation.element.missing.loanToAdd.breakdown.balanceAtPeriodEnd";

    private static final String AT_LEAST_ONE_LOAN_REQUIRED = "validation.addOrRemoveLoans.oneRequired";

    public List<ValidationError> validateLoanToAdd(LoanToAdd loanToAdd, Boolean isMultiYearFiler) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(loanToAdd.getDescription())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(DESCRIPTION);
            error.setMessageKey(DESCRIPTION_NOT_PRESENT);
            validationErrors.add(error);
        }

        if (Boolean.TRUE.equals(isMultiYearFiler) && loanToAdd.getBreakdown().getBalanceAtPeriodStart() == null) {

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

    public List<ValidationError> validateAtLeastOneLoan(AddOrRemoveLoans addOrRemoveLoans, boolean isSingleDirector) {

        List<ValidationError> validationErrors = new ArrayList<>();

        boolean isEmptyResource;
        if(isSingleDirector) {
            isEmptyResource = isSingleDirectorEmptyResource(addOrRemoveLoans.getLoanToAdd(), addOrRemoveLoans.getIsMultiYearFiler());
        } else {
            isEmptyResource = isEmptyResource(addOrRemoveLoans.getLoanToAdd(), addOrRemoveLoans.getIsMultiYearFiler());
        }

        if (addOrRemoveLoans.getExistingLoans() == null && isEmptyResource) {
            ValidationError error = new ValidationError();
            error.setFieldPath(LOAN_TO_ADD);
            error.setMessageKey(AT_LEAST_ONE_LOAN_REQUIRED);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public boolean isEmptyResource(LoanToAdd loanToAdd, Boolean isMultiYearFiler) {

        return loanToAdd == null || (StringUtils.isBlank(loanToAdd.getDirectorName()) && isDescriptionAndBreakdownEmpty(loanToAdd, isMultiYearFiler));
    }

    public boolean isSingleDirectorEmptyResource(LoanToAdd loanToAdd, Boolean isMultiYearFiler) {

        return loanToAdd == null || isDescriptionAndBreakdownEmpty(loanToAdd, isMultiYearFiler);
    }

    private boolean isDescriptionAndBreakdownEmpty(LoanToAdd loanToAdd, Boolean isMultiYearFiler) {
        return  (StringUtils.isBlank(loanToAdd.getDescription()) &&
                ((isMultiYearFiler && loanToAdd.getBreakdown().getBalanceAtPeriodStart() == null) || !isMultiYearFiler) &&
                loanToAdd.getBreakdown().getBalanceAtPeriodEnd() == null);
    }
}