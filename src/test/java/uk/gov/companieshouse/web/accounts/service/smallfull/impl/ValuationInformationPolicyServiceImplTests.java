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
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.ValuationInformationPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.ValuationInformationPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValuationInformationPolicyServiceImplTests {

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String VALUATION_INFORMATION_POLICY_FIELD_PATH =
            "valuationInformationPolicyDetails";

    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE =
            "validation.length.minInvalid.accounting_policies.valuation_information_and_policy";

    @Mock
    private List<ValidationError> apiErrors;

    @Mock
    private AccountingPoliciesService accountingPoliciesService;

    @Mock
    private AccountingPoliciesTransformer accountingPoliciesTransformer;

    @InjectMocks
    private ValuationInformationPolicyService service = new ValuationInformationPolicyServiceImpl();

    @Test
    @DisplayName("Get valuation information policy")
    void getValuationInformationPolicy() throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);
        when(accountingPoliciesTransformer.getValuationInformationPolicy(accountingPoliciesApi))
                .thenReturn(new ValuationInformationPolicy());

        ValuationInformationPolicy valuationInformationPolicy =
                service.getValuationInformationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(valuationInformationPolicy);
    }

    @Test
    @DisplayName("Submit invalid valuation information policy")
    void submitInvalidValuationInformationPolicy() throws ServiceException {

        ValuationInformationPolicy valuationInformationPolicy = createInvalidValuationInformationPolicy();

        List<ValidationError> validationErrors =
                service.submitValuationInformationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, valuationInformationPolicy);

        assertEquals(1, validationErrors.size());
        assertEquals(INVALID_STRING_SIZE_ERROR_MESSAGE, validationErrors.get(0).getMessageKey());
        assertEquals(VALUATION_INFORMATION_POLICY_FIELD_PATH, validationErrors.get(0).getFieldPath());
    }

    @Test
    @DisplayName("Submit valuation information policy not provided")
    void submitValuationInformationPolicyNotProvided() throws ServiceException {

        ValuationInformationPolicy valuationInformationPolicy = new ValuationInformationPolicy();
        valuationInformationPolicy.setIncludeValuationInformationPolicy(false);

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
                .setValuationInformationPolicy(valuationInformationPolicy, accountingPoliciesApi);
        when(accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi))
                .thenReturn(apiErrors);

        List<ValidationError> validationErrors =
                service.submitValuationInformationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, valuationInformationPolicy);

        assertEquals(apiErrors, validationErrors);

        verify(accountingPoliciesService, times(1))
                .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(accountingPoliciesTransformer, times(1))
                .setValuationInformationPolicy(valuationInformationPolicy, accountingPoliciesApi);

        verify(accountingPoliciesService, times(1))
                .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);
    }

    @Test
    @DisplayName("Submit provided valuation information policy")
    void submitProvidedValuationInformationPolicy() throws ServiceException {

        ValuationInformationPolicy valuationInformationPolicy = new ValuationInformationPolicy();
        valuationInformationPolicy.setIncludeValuationInformationPolicy(true);
        valuationInformationPolicy.setValuationInformationPolicyDetails("valuationInformationPolicy");

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);
        doNothing().when(accountingPoliciesTransformer)
                .setValuationInformationPolicy(valuationInformationPolicy, accountingPoliciesApi);
        when(accountingPoliciesService.updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi))
                .thenReturn(apiErrors);

        List<ValidationError> validationErrors =
                service.submitValuationInformationPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, valuationInformationPolicy);

        assertEquals(apiErrors, validationErrors);

        verify(accountingPoliciesService, times(1))
                .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(accountingPoliciesTransformer, times(1))
                .setValuationInformationPolicy(valuationInformationPolicy, accountingPoliciesApi);

        verify(accountingPoliciesService, times(1))
                .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);
    }

    private ValuationInformationPolicy createInvalidValuationInformationPolicy() {

        ValuationInformationPolicy valuationInformationPolicy = new ValuationInformationPolicy();
        valuationInformationPolicy.setIncludeValuationInformationPolicy(true);
        return valuationInformationPolicy;
    }
}
