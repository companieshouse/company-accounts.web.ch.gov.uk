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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.OtherAccountingPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.OtherAccountingPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OtherAccountingPolicyServiceImplTest {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String OTHER_ACCOUNTING_POLICY_FIELD_PATH =
        "otherAccountingPolicyDetails";
    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
        "validation.length.minInvalid.accounting_policies.other_accounting_policy";
    @Mock
    private List<ValidationError> apiErrors;
    @Mock
    private AccountingPoliciesService accountingPoliciesService;
    @Mock
    private AccountingPoliciesTransformer accountingPoliciesTransformer;
    @InjectMocks
    private OtherAccountingPolicyService service = new OtherAccountingPolicyServiceImpl();

    @Test
    @DisplayName("Get other accounting policy")
    void getOtherAccountingPolicy() throws ServiceException {
        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(
            accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApi);
        when(accountingPoliciesTransformer.getOtherAccountingPolicy(accountingPoliciesApi))
            .thenReturn(new OtherAccountingPolicy());
        OtherAccountingPolicy otherAccountingPolicy =
            service.getOtherAccountingPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        assertNotNull(otherAccountingPolicy);
    }

    @Test
    @DisplayName("Submit invalid other acounting policy")
    void submitInvalidOtherAccountingPolicy() throws ServiceException {
        OtherAccountingPolicy otherAccountingPolicy = createInvalidOtherAccountingPolicy();
        List<ValidationError> validationErrors =
            service.submitOtherAccountingPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                otherAccountingPolicy);
        assertEquals(1, validationErrors.size());
        assertEquals(INVALID_STRING_SIZE_ERROR_MESSAGE, validationErrors.get(0).getMessageKey());
        assertEquals(OTHER_ACCOUNTING_POLICY_FIELD_PATH,
            validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Submit other accounting policy not provided")
    void submitOtherAccountingPolicyNotProvided() throws ServiceException {
        OtherAccountingPolicy otherAccountingPolicy = new OtherAccountingPolicy();
        otherAccountingPolicy.setHasOtherAccountingPolicySelected(false);
        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(
            accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
            .setOtherAccountingPolicy(otherAccountingPolicy, accountingPoliciesApi);
        when(accountingPoliciesService
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi))
            .thenReturn(apiErrors);
        List<ValidationError> validationErrors =
            service.submitOtherAccountingPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                otherAccountingPolicy);
        assertEquals(apiErrors, validationErrors);
        verify(accountingPoliciesService, times(1))
            .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(accountingPoliciesTransformer, times(1))
            .setOtherAccountingPolicy(otherAccountingPolicy, accountingPoliciesApi);
        verify(accountingPoliciesService, times(1))
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi);
    }

    @Test
    @DisplayName("Submit provided other accounting policy")
    void submitProvidedOtherAccountingPolicy() throws ServiceException {
        OtherAccountingPolicy otherAccountingPolicy = new OtherAccountingPolicy();
        otherAccountingPolicy.setHasOtherAccountingPolicySelected(true);
        otherAccountingPolicy
            .setOtherAccountingPolicyDetails("otherAccountingPolicy");
        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(
            accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
            .setOtherAccountingPolicy(otherAccountingPolicy, accountingPoliciesApi);
        when(accountingPoliciesService
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi))
            .thenReturn(apiErrors);
        List<ValidationError> validationErrors =
            service.submitOtherAccountingPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                otherAccountingPolicy);
        assertEquals(apiErrors, validationErrors);
        verify(accountingPoliciesService, times(1))
            .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        verify(accountingPoliciesTransformer, times(1))
            .setOtherAccountingPolicy(otherAccountingPolicy, accountingPoliciesApi);
        verify(accountingPoliciesService, times(1))
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApi);
    }

    /**
     * Creates an other accounting policy with 'include policy' set to true, but without policy
     * details
     */
    private OtherAccountingPolicy createInvalidOtherAccountingPolicy() {
        OtherAccountingPolicy otherAccountingPolicy = new OtherAccountingPolicy();
        otherAccountingPolicy.setHasOtherAccountingPolicySelected(true);
        return otherAccountingPolicy;
    }
}