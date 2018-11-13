package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.AccountingPoliciesTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountingPoliciesTransformerImplTests {

    private AccountingPoliciesTransformer transformer = new AccountingPoliciesTransformerImpl();

    private static final String BASIS_OF_PREPARATION_PREPARED_STATEMENT =
            "These financial statements have been prepared in accordance with the provisions of "
                    + "Section 1A (Small Entities) of Financial Reporting Standard 102";

    private static final String BASIS_OF_PREPARATION_CUSTOM_STATEMENT = "customStatement";

    @Test
    @DisplayName("Get Basis of Preparation - Null AccountingPoliciesApi")
    void getBasisOfPreparationNullAccountingPoliciesApi() {

        BasisOfPreparation basisOfPreparation = transformer.getBasisOfPreparation(null);

        assertNotNull(basisOfPreparation);
        assertNull(basisOfPreparation.getIsPreparedInAccordanceWithStandards());
        assertNull(basisOfPreparation.getCustomStatement());
    }

    @Test
    @DisplayName("Get Basis of Preparation - Selected prepared statement")
    void getBasisOfPreparationSelectedPreparedStatement() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setBasisOfMeasurementAndPreparation(BASIS_OF_PREPARATION_PREPARED_STATEMENT);

        BasisOfPreparation basisOfPreparation = transformer.getBasisOfPreparation(accountingPoliciesApi);

        assertNotNull(basisOfPreparation);
        assertTrue(basisOfPreparation.getIsPreparedInAccordanceWithStandards());
        assertNull(basisOfPreparation.getCustomStatement());
    }

    @Test
    @DisplayName("Get Basis of Preparation - Custom statement")
    void getBasisOfPreparationCustomStatement() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setBasisOfMeasurementAndPreparation(BASIS_OF_PREPARATION_CUSTOM_STATEMENT);

        BasisOfPreparation basisOfPreparation = transformer.getBasisOfPreparation(accountingPoliciesApi);

        assertNotNull(basisOfPreparation);
        assertFalse(basisOfPreparation.getIsPreparedInAccordanceWithStandards());
        assertEquals(BASIS_OF_PREPARATION_CUSTOM_STATEMENT, basisOfPreparation.getCustomStatement());
    }

    @Test
    @DisplayName("Set Basis of Preparation - Selected prepared statement")
    void setBasisOfPreparationSelectedPreparedStatement() {

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        basisOfPreparation.setIsPreparedInAccordanceWithStandards(true);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        transformer.setBasisOfPreparation(basisOfPreparation, accountingPoliciesApi);

        assertEquals(BASIS_OF_PREPARATION_PREPARED_STATEMENT, accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
    }

    @Test
    @DisplayName("Set Basis of Preparation - Custom statement")
    void setBasisOfPreparationCustomStatement() {

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        basisOfPreparation.setIsPreparedInAccordanceWithStandards(false);
        basisOfPreparation.setCustomStatement(BASIS_OF_PREPARATION_CUSTOM_STATEMENT);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        transformer.setBasisOfPreparation(basisOfPreparation, accountingPoliciesApi);

        assertEquals(BASIS_OF_PREPARATION_CUSTOM_STATEMENT, accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
    }


}
