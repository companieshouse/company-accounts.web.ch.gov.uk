package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class RptTransactionValidator {

    private static final String RPT_TRANSACTION_TO_ADD = "rptTransactionToAdd";

    private static final String RELATIONSHIP = RPT_TRANSACTION_TO_ADD + ".relationship";
    private static final String RPT_TRANSACTIONS_RELATIONSHIP_NOT_PRESENT = "validation.element.missing." + RELATIONSHIP;

    public List<ValidationError> validateRptTransactionToAdd(RptTransactionToAdd rptTransactionToAdd) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(rptTransactionToAdd.getRelationship())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(RELATIONSHIP);
            error.setMessageKey(RPT_TRANSACTIONS_RELATIONSHIP_NOT_PRESENT);
            validationErrors.add(error);
        }

        return validationErrors;
    }
}