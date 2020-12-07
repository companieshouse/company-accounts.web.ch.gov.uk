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

    private final RptTransactionValidator validator = new RptTransactionValidator();

    @Test
    @DisplayName("Validate RPT transaction - success")
    void validateRptTransactionToAddSuccess() {

        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();
        rptTransactionToAdd.setRelationship(TRANSACTION_RELATIONSHIP);
        List<ValidationError> validationErrors = validator.validateRptTransactionToAdd(rptTransactionToAdd);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Validate loan to add for multi year filer - missing relationship")
    void validateRptTransactionToAddMissingRelationship() {

        RptTransactionToAdd rptTransactionToAdd = new RptTransactionToAdd();

        List<ValidationError> validationErrors = validator.validateRptTransactionToAdd(rptTransactionToAdd);

        assertFalse(validationErrors.isEmpty());
        assertEquals(1, validationErrors.size());
        assertEquals(RPT_TRANSACTIONS_RELATIONSHIP_NOT_PRESENT, validationErrors.get(0).getMessageKey());
    }
}