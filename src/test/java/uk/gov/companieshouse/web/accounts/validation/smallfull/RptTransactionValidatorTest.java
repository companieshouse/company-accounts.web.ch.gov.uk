package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.web.accounts.model.relatedpartytransactions.RptTransactionToAdd;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RptTransactionValidatorTest {

    private static final String RPT_TRANSACTION_TO_ADD = "rptTransactionToAdd";

    private static final String TRANSACTION_RELATIONSHIP = "relationship";
    private static final String RELATIONSHIP = RPT_TRANSACTION_TO_ADD + ".relationship";
    private static final String RPT_TRANSACTIONS_RELATIONSHIP_NOT_PRESENT = "validation.element.missing." + RELATIONSHIP;

    private static final String TRANSACTION_DESCRIPTION = "description";
    private static final String DESCRIPTION_OF_TRANSACTION = RPT_TRANSACTION_TO_ADD + ".descriptionOfTransaction";
    private static final String RPT_TRANSACTIONS_DESCRIPTION_OF_TRANSACTION_NOT_PRESENT = "validation.element.missing." + DESCRIPTION_OF_TRANSACTION;

    private final RptTransactionValidator validator = new RptTransactionValidator();

    @Test
    @DisplayName("Validate RPT transaction - relationship success")
    void validateRptTransactionToAddRelationshipSuccess() {

        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();
        rptTransactionToAdd.setDescriptionOfTransaction(TRANSACTION_DESCRIPTION);
        rptTransactionToAdd.setRelationship(TRANSACTION_RELATIONSHIP);
        List<ValidationError> validationErrors = validator.validateRptTransactionToAdd(rptTransactionToAdd, false);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate RPT transaction - missing relationship")
    void validateRptTransactionToAddMissingRelationship() {

        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();

        rptTransactionToAdd.setDescriptionOfTransaction(TRANSACTION_DESCRIPTION);

        List<ValidationError> validationErrors = validator.validateRptTransactionToAdd(rptTransactionToAdd, false);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(RPT_TRANSACTIONS_RELATIONSHIP_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }

    @Test
    @DisplayName("Validate RPT transaction - description of transaction success")
    void validateRptTransactionToAddDescriptionOfTransactionSuccess() {

        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();
        rptTransactionToAdd.setDescriptionOfTransaction(TRANSACTION_DESCRIPTION);
        rptTransactionToAdd.setRelationship(TRANSACTION_DESCRIPTION);
        List<ValidationError> validationErrors = validator.validateRptTransactionToAdd(rptTransactionToAdd, false);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate RPT transaction -  missing description of transaction")
    void validateRptTransactionToAddMissingDescriptionOfTransaction() {

        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();

        rptTransactionToAdd.setRelationship(TRANSACTION_RELATIONSHIP);
        List<ValidationError> validationErrors = validator.validateRptTransactionToAdd(rptTransactionToAdd, false);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(RPT_TRANSACTIONS_DESCRIPTION_OF_TRANSACTION_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }
}