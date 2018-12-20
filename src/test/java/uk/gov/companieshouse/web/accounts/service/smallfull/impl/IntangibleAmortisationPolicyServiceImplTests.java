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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.IntangibleAmortisationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAmortisationPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntangibleAmortisationPolicyServiceImplTests {

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH =
            "intangibleAmortisationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.intangible_fixed_assets_amortisation_policy";

    @Mock
    private List<ValidationError> apiErrors;

    @Mock
    private AccountingPoliciesService accountingPoliciesService;

    @Mock
    private AccountingPoliciesTransformer accountingPoliciesTransformer;

    @InjectMocks
    private IntangibleAmortisationPolicyService service = new IntangibleAmortisationPolicyServiceImpl();

    @Test
    @DisplayName("Get intangible amortisation policy")
    void getIntangibleAmortisationPolicy() throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);
        when(accountingPoliciesTransformer.getIntangibleAmortisationPolicy(accountingPoliciesApi))
                .thenReturn(new IntangibleAmortisationPolicy());

        IntangibleAmortisationPolicy intangibleAmortisationPolicy =
                service.getIntangibleAmortisationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(intangibleAmortisationPolicy);
    }

    @Test
    @DisplayName("Submit invalid intangible amortisation policy")
    void submitInvalidIntangibleAmortisationPolicy() throws ServiceException {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = createInvalidIntangibleAmortisationPolicy();

        List<ValidationError> validationErrors =
                service.submitIntangibleAmortisationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAmortisationPolicy);

        assertEquals(1, validationErrors.size());
        assertEquals(INVALID_STRING_SIZE_ERROR_MESSAGE, validationErrors.get(0).getMessageKey());
        assertEquals(INTANGIBLE_AMORTISATION_POLICY_FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Submit intangible amortisation policy not provided")
    void submitIntangibleAmortisationPolicyNotProvided() throws ServiceException {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(false);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
                .setIntangibleAmortisationPolicy(intangibleAmortisationPolicy, accountingPoliciesApi);
        when(accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi))
                .thenReturn(apiErrors);

        List<ValidationError> validationErrors =
                service.submitIntangibleAmortisationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAmortisationPolicy);

        assertEquals(apiErrors, validationErrors);

        verify(accountingPoliciesService, times(1))
                .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(accountingPoliciesTransformer, times(1))
                .setIntangibleAmortisationPolicy(intangibleAmortisationPolicy, accountingPoliciesApi);

        verify(accountingPoliciesService, times(1))
                .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);
    }

    @Test
    @DisplayName("Submit provided intangible amortisation policy")
    void submitProvidedIntangibleAmortisationPolicy() throws ServiceException {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(true);
        intangibleAmortisationPolicy.setIntangibleAmortisationPolicyDetails("intangibleAmortisationPolicy");

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
                .setIntangibleAmortisationPolicy(intangibleAmortisationPolicy, accountingPoliciesApi);
        when(accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi))
                .thenReturn(apiErrors);

        List<ValidationError> validationErrors =
                service.submitIntangibleAmortisationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAmortisationPolicy);

        assertEquals(apiErrors, validationErrors);

        verify(accountingPoliciesService, times(1))
                .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(accountingPoliciesTransformer, times(1))
                .setIntangibleAmortisationPolicy(intangibleAmortisationPolicy, accountingPoliciesApi);

        verify(accountingPoliciesService, times(1))
                .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);
    }

    /**
     * Creates an intangible amortisation policy with 'include policy' set to true, but without policy details
     * @return
     */
    private IntangibleAmortisationPolicy createInvalidIntangibleAmortisationPolicy() {

        IntangibleAmortisationPolicy intangibleAmortisationPolicy = new IntangibleAmortisationPolicy();
        intangibleAmortisationPolicy.setIncludeIntangibleAmortisationPolicy(true);
        return intangibleAmortisationPolicy;
    }
}
