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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.AccountingPoliciesTransformerImpl;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AccountingPoliciesTransformerImplTests {

    private static final String BASIS_OF_PREPARATION_PREPARED_STATEMENT =
        "These financial statements have been prepared in accordance with section 396 of the Companies Act";

    private static final String BASIS_OF_PREPARATION_CUSTOM_STATEMENT = "customStatement";
    private static final String TURNOVER_POLICY_DETAILS = "turnoverPolicyDetails";
    private static final String INTANGIBLE_AMORTISATION_POLICY_DETAILS = "intangibleAmortisationPolicyDetails";

    private AccountingPoliciesTransformer transformer = new AccountingPoliciesTransformerImpl();

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

        BasisOfPreparation basisOfPreparation = createBasisOfPreparation(true);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        transformer.setBasisOfPreparation(basisOfPreparation, accountingPoliciesApi);

        assertEquals(BASIS_OF_PREPARATION_PREPARED_STATEMENT, accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
    }

    @Test
    @DisplayName("Set Basis of Preparation - Custom statement")
    void setBasisOfPreparationCustomStatement() {

        BasisOfPreparation basisOfPreparation = createBasisOfPreparation(false);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        transformer.setBasisOfPreparation(basisOfPreparation, accountingPoliciesApi);

        assertEquals(BASIS_OF_PREPARATION_CUSTOM_STATEMENT, accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
    }

    @Test
    @DisplayName("Returned empty turnover policy object when accounting policies api is null")
    void shouldCreateTurnoverPolicyObjectWhenAccountingPoliciesApiIsNull() {

        TurnoverPolicy turnoverPolicy = transformer.getTurnoverPolicy(null);

        assertNotNull(turnoverPolicy);
        assertNull(turnoverPolicy.getIsIncludeTurnoverSelected());
        assertNull(turnoverPolicy.getTurnoverPolicyDetails());
    }

    @Test
    @DisplayName("Returned empty turnover policy object when turnover policy is null within accounting policies api")
    void shouldGetTurnoverPolicyWhenAccountingPoliciesApiIsSetAndTurnoverPolicyIsNull() {

        TurnoverPolicy turnoverPolicy = transformer.getTurnoverPolicy(new AccountingPoliciesApi());

        assertNotNull(turnoverPolicy);
        assertNull(turnoverPolicy.getIsIncludeTurnoverSelected());
        assertNull(turnoverPolicy.getTurnoverPolicyDetails());
    }

    @Test
    @DisplayName("Returned turnover policy contains values from turnover policy within accounting policies api")
    void shouldGetTurnoverPolicyWhenAccountingPolicyApiAndTurnoverPolicySet() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setTurnoverPolicy(TURNOVER_POLICY_DETAILS);

        TurnoverPolicy turnoverPolicy = transformer.getTurnoverPolicy(accountingPoliciesApi);

        assertNotNull(turnoverPolicy);
        assertTrue(turnoverPolicy.getIsIncludeTurnoverSelected());
        assertEquals(TURNOVER_POLICY_DETAILS, turnoverPolicy.getTurnoverPolicyDetails());
    }

    @Test
    @DisplayName("Turnover policy values are populated to the turnover policy within accounting policies api when turnover selected")
    void shouldSetTurnoverPolicyAPIWhenIncludeTurnoverSelected() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        transformer.setTurnoverPolicy(createTurnOverPolicy(true), accountingPoliciesApi);

        assertNotNull(accountingPoliciesApi);
        assertNotNull(accountingPoliciesApi.getTurnoverPolicy());
        assertEquals(TURNOVER_POLICY_DETAILS, accountingPoliciesApi.getTurnoverPolicy());
    }

    @Test
    @DisplayName("Turnover policy values are populated to the turnover policy within accounting policies api when turnover not selected")
    void shouldNotSetTurnoverPolicyAPIWhenIncludeTurnoverNotSelected() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        transformer.setTurnoverPolicy(createTurnOverPolicy(false), accountingPoliciesApi);

        assertNotNull(accountingPoliciesApi);
        assertNull(accountingPoliciesApi.getTurnoverPolicy());
    }

    @Test
    @DisplayName("Get intangible amortisation policy - no data in API model")
    void getIntangibleAmortisationPolicyNoDataInApiModel() {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy =
                transformer.getIntangibleAmortisationPolicy(new AccountingPoliciesApi());

        assertNotNull(intangibleAmortisationPolicy);
        assertNull(intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails());
        assertNull(intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy());
    }

    @Test
    @DisplayName("Get intangible amortisation policy - data present in API model")
    void getIntangibleAmortisationPolicyDataPresentInApiModel() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setIntangibleFixedAssetsAmortisationPolicy(INTANGIBLE_AMORTISATION_POLICY_DETAILS);

        IntangibleAmortisationPolicy intangibleAmortisationPolicy =
                transformer.getIntangibleAmortisationPolicy(accountingPoliciesApi);

        assertNotNull(intangibleAmortisationPolicy);
        assertEquals(INTANGIBLE_AMORTISATION_POLICY_DETAILS, intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails());
        assertTrue(intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy());
    }

    @Test
    @DisplayName("Set intangible amortisation policy - details not provided")
    void setIntangibleAmortisationPolicyDetailsNotProvided() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setIntangibleFixedAssetsAmortisationPolicy(INTANGIBLE_AMORTISATION_POLICY_DETAILS);

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = createIntangibleAmortisationPolicy(false);

        transformer.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy, accountingPoliciesApi);

        assertNull(accountingPoliciesApi.getIntangibleFixedAssetsAmortisationPolicy());
    }

    @Test
    @DisplayName("Set intangible amortisation policy - details provided")
    void setIntangibleAmortisationPolicyDetailsProvided() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = createIntangibleAmortisationPolicy(true);

        transformer.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy, accountingPoliciesApi);

        assertEquals(INTANGIBLE_AMORTISATION_POLICY_DETAILS, accountingPoliciesApi.getIntangibleFixedAssetsAmortisationPolicy());
    }

    private BasisOfPreparation createBasisOfPreparation(boolean isPreparedInAccordanceWithStandards) {

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        basisOfPreparation.setIsPreparedInAccordanceWithStandards(isPreparedInAccordanceWithStandards);
        if (!isPreparedInAccordanceWithStandards) {
            basisOfPreparation.setCustomStatement(BASIS_OF_PREPARATION_CUSTOM_STATEMENT);
        }
        return basisOfPreparation;
    }

    private TurnoverPolicy createTurnOverPolicy(boolean includePolicy) {

        TurnoverPolicy turnoverPolicy = new TurnoverPolicy();
        turnoverPolicy.setIsIncludeTurnoverSelected(includePolicy);
        if (includePolicy) {
            turnoverPolicy.setTurnoverPolicyDetails(TURNOVER_POLICY_DETAILS);
        }

        return turnoverPolicy;
    }

    private IntangibleAmortisationPolicy createIntangibleAmortisationPolicy(boolean includePolicy) {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(includePolicy);
        if (includePolicy) {
            intangibleAmortisationPolicy
                    .setIntangibleAmortisationPolicyDetails(INTANGIBLE_AMORTISATION_POLICY_DETAILS);
        }

        return intangibleAmortisationPolicy;
    }

}
