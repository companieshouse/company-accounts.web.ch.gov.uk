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

    private static final String DIRECTOR_NAME = LOAN_TO_ADD + ".director_name";
    private static final String NAME_NOT_PRESENT = "validation.element.missing.director.name";

    public List<ValidationError> validateDirectorToAdd(LoanToAdd loanToAdd) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(loanToAdd.getDirectorName())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(DIRECTOR_NAME);
            error.setMessageKey(NAME_NOT_PRESENT);
            validationErrors.add(error);
        }

        return validationErrors;
    }
}
