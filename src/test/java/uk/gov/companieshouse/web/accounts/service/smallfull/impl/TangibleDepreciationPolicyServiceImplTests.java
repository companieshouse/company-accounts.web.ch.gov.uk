package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TangibleDepreciationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleDepreciationPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TangibleDepreciationPolicyServiceImplTests {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TANGIBLE_DEPRECIATION_POLICY_FIELD_PATH =
        "tangibleDepreciationPolicyDetails";
    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
        "validation.length.minInvalid.accounting_policies.tangible_fixed_assets_depreciation_policy";
    @Mock
    private List<ValidationError> apiErrors;
    @Mock
    private AccountingPoliciesService accountingPoliciesService;
    @Mock
    private AccountingPoliciesTransformer accountingPoliciesTransformer;
    @InjectMocks
    private TangibleDepreciationPolicyService service = new TangibleDepreciationPolicyServiceImpl();

    @Test
    @DisplayName("Get tangible depreciation policy")
    void getTangibleDepreciationPolicy() throws ServiceException {
        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(
            accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApi);
        when(accountingPoliciesTransformer.getTangibleDepreciationPolicy(accountingPoliciesApi))
            .thenReturn(new TangibleDepreciationPolicy());
        TangibleDepreciationPolicy tangibleDepreciationPolicy =
            service.getTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        assertNotNull(tangibleDepreciationPolicy);
    }

    @Test
    @DisplayName("Submit invalid tangible depreciation policy")
    void submitInvalidTangibleDepreciationPolicy() throws ServiceException {
        TangibleDepreciationPolicy tangibleDepreciationPolicy = createInvalidTangibleDepreciationPolicy();
        List<ValidationError> validationErrors =
            service.submitTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                tangibleDepreciationPolicy);
        assertEquals(1, validationErrors.size());
        assertEquals(INVALID_STRING_SIZE_ERROR_MESSAGE, validationErrors.get(0).getMessageKey());
        assertEquals(TANGIBLE_DEPRECIATION_POLICY_FIELD_PATH,
            validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Submit tangible depreciation policy not provided")
    void submitTangibleDepreciationPolicyNotProvided() throws ServiceException {
        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(false);
        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(
            accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
            .setTangibleDepreciationPolicy(tangibleDepreciationPolicy, accountingPoliciesApi);
        when(accountingPoliciesService
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi))
            .thenReturn(apiErrors);
        List<ValidationError> validationErrors =
            service.submitTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                tangibleDepreciationPolicy);
        assertEquals(apiErrors, validationErrors);
        verify(accountingPoliciesService, times(1))
            .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(accountingPoliciesTransformer, times(1))
            .setTangibleDepreciationPolicy(tangibleDepreciationPolicy, accountingPoliciesApi);
        verify(accountingPoliciesService, times(1))
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi);
    }

    @Test
    @DisplayName("Submit provided tangible depreciation policy")
    void submitProvidedTangibleDepreciationPolicy() throws ServiceException {
        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
        tangibleDepreciationPolicy
            .setTangibleDepreciationPolicyDetails("tangibleDepreciationPolicy");
        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(
            accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
            .setTangibleDepreciationPolicy(tangibleDepreciationPolicy, accountingPoliciesApi);
        when(accountingPoliciesService
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi))
            .thenReturn(apiErrors);
        List<ValidationError> validationErrors =
            service.submitTangibleDepreciationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                tangibleDepreciationPolicy);
        assertEquals(apiErrors, validationErrors);
        verify(accountingPoliciesService, times(1))
            .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(accountingPoliciesTransformer, times(1))
            .setTangibleDepreciationPolicy(tangibleDepreciationPolicy, accountingPoliciesApi);
        verify(accountingPoliciesService, times(1))
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi);
    }

    /**
     * Creates an tangible depreciation policy with 'include policy' set to true, but without
     * policy details
     */
    private TangibleDepreciationPolicy createInvalidTangibleDepreciationPolicy() {
        TangibleDepreciationPolicy tangibleDepreciationPolicy = new TangibleDepreciationPolicy();
        tangibleDepreciationPolicy.setHasTangibleDepreciationPolicySelected(true);
        return tangibleDepreciationPolicy;
    }
}
