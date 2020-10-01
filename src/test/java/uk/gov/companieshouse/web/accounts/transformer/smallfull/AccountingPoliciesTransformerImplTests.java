package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.enumeration.AccountingRegulatoryStandard;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.AccountingPolicies;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.AccountingPoliciesTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountingPoliciesTransformerImplTests {

    private static final String BASIS_OF_PREPARATION_CUSTOM_STATEMENT = "customStatement";
    private static final String TURNOVER_POLICY_DETAILS = "turnoverPolicyDetails";
    private static final String INTANGIBLE_AMORTISATION_POLICY_DETAILS = "intangibleAmortisationPolicyDetails";
    private static final String VALUATION_INFORMATION_POLICY_DETAILS = "valuationInformationPolicyDetails";
    private static final String TANGIBLE_DEPRECIATION_POLICY_DETAILS = "tangibleDepreciationPolicyDetails";

    private AccountingPoliciesTransformerImpl transformer = new AccountingPoliciesTransformerImpl();

    @Test
    @DisplayName("Get Basis of Preparation - Null AccountingPoliciesApi")
    void getBasisOfPreparationNullAccountingPoliciesApi() {

        BasisOfPreparation basisOfPreparation = transformer.toWeb(null).getBasisOfPreparation();

        assertNotNull(basisOfPreparation);
        assertNull(basisOfPreparation.getAccountingRegulatoryStandard());
    }

    @Test
    @DisplayName("Get Basis of Preparation - Selected prepared statement")
    void getBasisOfPreparationSelectedPreparedStatement() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setBasisOfMeasurementAndPreparation(AccountingRegulatoryStandard.FRS101.toString());

        BasisOfPreparation basisOfPreparation = transformer.toWeb(accountingPoliciesApi).getBasisOfPreparation();

        assertNotNull(basisOfPreparation);
        assertEquals(AccountingRegulatoryStandard.FRS101, basisOfPreparation.getAccountingRegulatoryStandard());
    }

    @Test
    @DisplayName("Get Basis of Preparation - Custom statement")
    void getBasisOfPreparationCustomStatement() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setBasisOfMeasurementAndPreparation(BASIS_OF_PREPARATION_CUSTOM_STATEMENT);

        BasisOfPreparation basisOfPreparation = transformer.toWeb(accountingPoliciesApi).getBasisOfPreparation();

        assertNotNull(basisOfPreparation);
        assertNotEquals(AccountingRegulatoryStandard.FRS102, basisOfPreparation.getAccountingRegulatoryStandard());
    }

    @Test
    @DisplayName("Set Basis of Preparation - Selected prepared statement")
    void setBasisOfPreparationSelectedPreparedStatement() {

        BasisOfPreparation basisOfPreparation = createBasisOfPreparation(AccountingRegulatoryStandard.FRS101);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setBasisOfPreparation(basisOfPreparation);

        AccountingPoliciesApi accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertEquals(AccountingRegulatoryStandard.FRS101.toString(), accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
    }

    @Test
    @DisplayName("Set Basis of Preparation - Custom statement")
    void setBasisOfPreparationCustomStatement() {

        BasisOfPreparation basisOfPreparation = createBasisOfPreparation(AccountingRegulatoryStandard.FRS101);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setBasisOfPreparation(basisOfPreparation);

        AccountingPoliciesApi accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertEquals(AccountingRegulatoryStandard.FRS101.toString(), accountingPoliciesApi.getBasisOfMeasurementAndPreparation());
    }

    @Test
    @DisplayName("Returned empty turnover policy object when accounting policies api is null")
    void shouldCreateTurnoverPolicyObjectWhenAccountingPoliciesApiIsNull() {

        TurnoverPolicy turnoverPolicy = transformer.toWeb(null).getTurnoverPolicy();

        assertNotNull(turnoverPolicy);
        assertNull(turnoverPolicy.getIsIncludeTurnoverSelected());
        assertNull(turnoverPolicy.getTurnoverPolicyDetails());
    }

    @Test
    @DisplayName("Returned empty turnover policy object when turnover policy is null within accounting policies api")
    void shouldGetTurnoverPolicyWhenAccountingPoliciesApiIsSetAndTurnoverPolicyIsNull() {

        AccountingPoliciesApi accountingPoliciesApi =  new AccountingPoliciesApi();
        TurnoverPolicy turnoverPolicy = transformer.toWeb(accountingPoliciesApi).getTurnoverPolicy();

        assertNotNull(turnoverPolicy);
        assertNull(turnoverPolicy.getIsIncludeTurnoverSelected());
        assertNull(turnoverPolicy.getTurnoverPolicyDetails());
    }

    @Test
    @DisplayName("Returned turnover policy contains values from turnover policy within accounting policies api")
    void shouldGetTurnoverPolicyWhenAccountingPolicyApiAndTurnoverPolicySet() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setTurnoverPolicy(TURNOVER_POLICY_DETAILS);

        TurnoverPolicy turnoverPolicy = transformer.toWeb(accountingPoliciesApi).getTurnoverPolicy();

        assertNotNull(turnoverPolicy);
        assertTrue(turnoverPolicy.getIsIncludeTurnoverSelected());
        assertEquals(TURNOVER_POLICY_DETAILS, turnoverPolicy.getTurnoverPolicyDetails());
    }

    @Test
    @DisplayName("Turnover policy values are populated to the turnover policy within accounting policies api when turnover selected")
    void shouldSetTurnoverPolicyAPIWhenIncludeTurnoverSelected() {

        TurnoverPolicy turnoverPolicy = createTurnOverPolicy(true);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setTurnoverPolicy(turnoverPolicy);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS101));

        AccountingPoliciesApi accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertNotNull(accountingPoliciesApi);
        assertNotNull(accountingPoliciesApi.getTurnoverPolicy());
        assertEquals(TURNOVER_POLICY_DETAILS, accountingPoliciesApi.getTurnoverPolicy());
    }

    @Test
    @DisplayName("Turnover policy values are populated to the turnover policy within accounting policies api when turnover not selected")
    void shouldNotSetTurnoverPolicyAPIWhenIncludeTurnoverNotSelected() {

        TurnoverPolicy turnoverPolicy = createTurnOverPolicy(false);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setTurnoverPolicy(turnoverPolicy);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS101));

        AccountingPoliciesApi accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertNotNull(accountingPoliciesApi);
        assertNull(accountingPoliciesApi.getTurnoverPolicy());
    }

    @Test
    @DisplayName("Get intangible amortisation policy - no data in API model")
    void getIntangibleAmortisationPolicyNoDataInApiModel() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();

        IntangibleAmortisationPolicy intangibleAmortisationPolicy =
                transformer.toWeb(accountingPoliciesApi).getIntangibleAmortisationPolicy();

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
                transformer.toWeb(accountingPoliciesApi).getIntangibleAmortisationPolicy();

        assertNotNull(intangibleAmortisationPolicy);
        assertEquals(INTANGIBLE_AMORTISATION_POLICY_DETAILS, intangibleAmortisationPolicy.getIntangibleAmortisationPolicyDetails());
        assertTrue(intangibleAmortisationPolicy.getIncludeIntangibleAmortisationPolicy());
    }

    @Test
    @DisplayName("Set intangible amortisation policy - details not provided")
    void setIntangibleAmortisationPolicyDetailsNotProvided() {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = createIntangibleAmortisationPolicy(false);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setIntangibleFixedAssetsAmortisationPolicy(INTANGIBLE_AMORTISATION_POLICY_DETAILS);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS101));

        accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertNull(accountingPoliciesApi.getIntangibleFixedAssetsAmortisationPolicy());
    }

    @Test
    @DisplayName("Set intangible amortisation policy - details provided")
    void setIntangibleAmortisationPolicyDetailsProvided() {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = createIntangibleAmortisationPolicy(true);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setIntangibleAmortisationPolicy(intangibleAmortisationPolicy);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS102));

        AccountingPoliciesApi accountingPoliciesApi = transformer.toApi(accountingPolicies);


        assertEquals(INTANGIBLE_AMORTISATION_POLICY_DETAILS, accountingPoliciesApi.getIntangibleFixedAssetsAmortisationPolicy());
    }

    @Test
    @DisplayName("Set tangible depreciation policy - details not provided")
    void setTangibleDepreciationPolicyDetailsNotProvided() {

        TangibleDepreciationPolicy tangibleDepreciationPolicy = createTangibleDepreciationPolicy(false);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setTangibleDepreciationPolicy(tangibleDepreciationPolicy);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setTangibleFixedAssetsDepreciationPolicy(TANGIBLE_DEPRECIATION_POLICY_DETAILS);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS101));

        accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertNull(accountingPoliciesApi.getTangibleFixedAssetsDepreciationPolicy());
    }

    @Test
    @DisplayName("Set tangible depreciation policy - details provided")
    void setTangibleDepreciationPolicyDetailsProvided() {

        TangibleDepreciationPolicy tangibleDepreciationPolicy = createTangibleDepreciationPolicy(true);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setTangibleDepreciationPolicy(tangibleDepreciationPolicy);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS102));

        AccountingPoliciesApi accountingPoliciesApi = transformer.toApi(accountingPolicies);


        assertEquals(TANGIBLE_DEPRECIATION_POLICY_DETAILS, accountingPoliciesApi.getTangibleFixedAssetsDepreciationPolicy());
    }

    @Test
    @DisplayName("Get valuation information policy - no data in API model")
    void getValuationInformationPolicyNoDataInApiModel() {

        AccountingPoliciesApi accountingPoliciesApi =  new AccountingPoliciesApi();

        ValuationInformationPolicy valuationInformationPolicy =
                transformer.toWeb(accountingPoliciesApi).getValuationInformationPolicy();

        assertNotNull(valuationInformationPolicy);
        assertNull(valuationInformationPolicy.getIncludeValuationInformationPolicy());
        assertNull(valuationInformationPolicy.getValuationInformationPolicyDetails());
    }

    @Test
    @DisplayName("Get valuation information policy - data present in API model")
    void getValuationInformationPolicyDataPresentInApiModel() {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setValuationInformationAndPolicy(VALUATION_INFORMATION_POLICY_DETAILS);

        ValuationInformationPolicy valuationInformationPolicy =
                transformer.toWeb(accountingPoliciesApi).getValuationInformationPolicy();

        assertNotNull(valuationInformationPolicy);
        assertEquals(VALUATION_INFORMATION_POLICY_DETAILS, valuationInformationPolicy.getValuationInformationPolicyDetails());
        assertTrue(valuationInformationPolicy.getIncludeValuationInformationPolicy());
    }

    @Test
    @DisplayName("Set valuation information policy - details not provided")
    void setValuationInformationPolicyDetailsNotProvided() {

        ValuationInformationPolicy valuationInformationPolicy = createValuationInformationPolicy(false);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setValuationInformationPolicy(valuationInformationPolicy);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        accountingPoliciesApi.setValuationInformationAndPolicy(VALUATION_INFORMATION_POLICY_DETAILS);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS101));

        accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertNull(accountingPoliciesApi.getValuationInformationAndPolicy());
    }

    @Test
    @DisplayName("Set valuation information policy - details provided")
    void setValuationInformationPolicyDetailsProvided() {

        ValuationInformationPolicy valuationInformationPolicy = createValuationInformationPolicy(true);

        AccountingPolicies accountingPolicies = new AccountingPolicies();
        accountingPolicies.setValuationInformationPolicy(valuationInformationPolicy);
        accountingPolicies.setBasisOfPreparation(createBasisOfPreparation(AccountingRegulatoryStandard.FRS101));

        AccountingPoliciesApi accountingPoliciesApi = transformer.toApi(accountingPolicies);

        assertEquals(VALUATION_INFORMATION_POLICY_DETAILS, accountingPoliciesApi.getValuationInformationAndPolicy());
    }

    @Test
    @DisplayName("Note returned type is AccountingPolicies")
    void setAccountingPoliciesNoteReturned() {

        assertEquals(NoteType.SMALL_FULL_ACCOUNTING_POLICIES, transformer.getNoteType());
    }

    private BasisOfPreparation createBasisOfPreparation(AccountingRegulatoryStandard isPreparedInAccordanceWithStandards) {

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        basisOfPreparation.setAccountingRegulatoryStandard(isPreparedInAccordanceWithStandards);
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

    private TangibleDepreciationPolicy createTangibleDepreciationPolicy(boolean includePolicy) {

        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(includePolicy);
        if (includePolicy) {
            tangibleDepreciationPolicy
                    .setTangibleDepreciationPolicyDetails(TANGIBLE_DEPRECIATION_POLICY_DETAILS);
        }

        return tangibleDepreciationPolicy;
    }

    private ValuationInformationPolicy createValuationInformationPolicy(boolean includePolicy) {

        ValuationInformationPolicy valuationInformationPolicy = new ValuationInformationPolicy();
        valuationInformationPolicy.setIncludeValuationInformationPolicy(includePolicy);
        if (includePolicy) {
            valuationInformationPolicy
                    .setValuationInformationPolicyDetails(VALUATION_INFORMATION_POLICY_DETAILS);
        }

        return valuationInformationPolicy;
    }
}
