package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.AddOrRemoveRptTransactions;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

@Component
public class RptTransactionValidator {

    private static final String RPT_TRANSACTION_TO_ADD = "rptTransactionToAdd";

    private static final String RELATIONSHIP = RPT_TRANSACTION_TO_ADD + ".relationship";
    private static final String RPT_TRANSACTIONS_RELATIONSHIP_NOT_PRESENT = "validation.element.missing." + RELATIONSHIP;

    private static final String DESCRIPTION_OF_TRANSACTION = RPT_TRANSACTION_TO_ADD + ".descriptionOfTransaction";
    private static final String RPT_TRANSACTIONS_DESCRIPTION_OF_TRANSACTION_NOT_PRESENT = "validation.element.missing." + DESCRIPTION_OF_TRANSACTION;

    private static final String AT_LEAST_ONE_RPT_TRANSACTION_REQUIRED = "validation.addOrRemoveRptTransactions.oneRequired";
    private static final String RPT_TRANSACTION_MUST_BE_ADDED = "validation.RptTransactionToAdd.submissionRequired";

    private static final String BALANCE_AT_START = RPT_TRANSACTION_TO_ADD + ".breakdown.balanceAtPeriodStart";
    private static final String BAS_NOT_PRESENT = "validation.element.missing.rptTransactionToAdd.breakdown.balanceAtPeriodStart";

    private static final String BALANCE_AT_END = RPT_TRANSACTION_TO_ADD + ".breakdown.balanceAtPeriodEnd";
    private static final String BAE_NOT_PRESENT = "validation.element.missing.rptTransactionToAdd.breakdown.balanceAtPeriodEnd";

    public List<ValidationError> validateRptTransactionToAdd(RptTransactionToAdd rptTransactionToAdd, Boolean isMultiYearFiler) {
        List<ValidationError> validationErrors = new ArrayList<>();

        if (StringUtils.isBlank(rptTransactionToAdd.getRelationship())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(RELATIONSHIP);
            error.setMessageKey(RPT_TRANSACTIONS_RELATIONSHIP_NOT_PRESENT);
            validationErrors.add(error);
        }

        if (StringUtils.isBlank(rptTransactionToAdd.getDescriptionOfTransaction())) {

            ValidationError error = new ValidationError();
            error.setFieldPath(DESCRIPTION_OF_TRANSACTION);
            error.setMessageKey(RPT_TRANSACTIONS_DESCRIPTION_OF_TRANSACTION_NOT_PRESENT);
            validationErrors.add(error);
        }

        if (Boolean.TRUE.equals(isMultiYearFiler) && rptTransactionToAdd.getBreakdown().getBalanceAtPeriodStart() == null) {

            ValidationError error = new ValidationError();
            error.setFieldPath(BALANCE_AT_START);
            error.setMessageKey(BAS_NOT_PRESENT);
            validationErrors.add(error);
        }

        if (rptTransactionToAdd.getBreakdown().getBalanceAtPeriodEnd() == null) {

            ValidationError error = new ValidationError();
            error.setFieldPath(BALANCE_AT_END);
            error.setMessageKey(BAE_NOT_PRESENT);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public List<ValidationError> validateAtLeastOneRptTransaction(AddOrRemoveRptTransactions addOrRemoveRptTransactions) {

        List<ValidationError> validationErrors = new ArrayList<>();

        boolean isEmptyResource = isEmptyResource(addOrRemoveRptTransactions.getRptTransactionToAdd(), addOrRemoveRptTransactions.getIsMultiYearFiler());

        if (addOrRemoveRptTransactions.getExistingRptTransactions() == null && isEmptyResource) {
            ValidationError error = new ValidationError();
            error.setFieldPath(RPT_TRANSACTION_TO_ADD);
            error.setMessageKey(AT_LEAST_ONE_RPT_TRANSACTION_REQUIRED);
            validationErrors.add(error);
        } else if (!isEmptyResource) {
            ValidationError error = new ValidationError();
            error.setFieldPath(RPT_TRANSACTION_TO_ADD);
            error.setMessageKey(RPT_TRANSACTION_MUST_BE_ADDED);
            validationErrors.add(error);
        }

        return validationErrors;
    }

    public boolean isEmptyResource(RptTransactionToAdd rptTransactionToAdd, Boolean isMultiYearFiler) {

        return rptTransactionToAdd == null || (StringUtils.isBlank(rptTransactionToAdd.getNameOfRelatedParty()) && isDescriptionAndBreakdownEmpty(rptTransactionToAdd, isMultiYearFiler));
    }

    private boolean isDescriptionAndBreakdownEmpty(RptTransactionToAdd rptTransactionToAdd, Boolean isMultiYearFiler) {
        return  (StringUtils.isBlank(rptTransactionToAdd.getDescriptionOfTransaction()) &&
                StringUtils.isBlank(rptTransactionToAdd.getRelationship()) &&
                ((isMultiYearFiler && rptTransactionToAdd.getBreakdown().getBalanceAtPeriodStart() == null) || !isMultiYearFiler) &&
                rptTransactionToAdd.getBreakdown().getBalanceAtPeriodEnd() == null);
    }
}