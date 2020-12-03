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

    private static final String TRANSACTION_TYPE = RPT_TRANSACTION_TO_ADD + ".transactionType";
    private static final String RPT_TRANSACTIONS_TRANSACTION_TYPE_NOT_PRESENT = "validation.element.missing." + TRANSACTION_TYPE;

    public List<ValidationError> validateRptTransactionToAdd(RptTransactionToAdd rptTransactionToAdd) {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(rptTransactionToAdd.getTransactionType())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(TRANSACTION_TYPE);
            error.setMessageKey(RPT_TRANSACTIONS_TRANSACTION_TYPE_NOT_PRESENT);
            validationErrors.add(error);
        }

        return validationErrors;
    }
}
