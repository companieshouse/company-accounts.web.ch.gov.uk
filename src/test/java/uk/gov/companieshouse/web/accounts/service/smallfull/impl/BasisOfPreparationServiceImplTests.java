package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.BasisOfPreparation;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.BasisOfPreparationService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BasisOfPreparationServiceImplTests {

    @Mock
    private AccountingPoliciesService accountingPoliciesService;

    @Mock
    private AccountingPoliciesTransformer transformer;

    @InjectMocks
    private BasisOfPreparationService basisOfPreparationService = new BasisOfPreparationServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Test
    @DisplayName("Get basis of preparation")
    void getBasisOfPreparation() throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        when(transformer.getBasisOfPreparation(accountingPoliciesApi)).thenReturn(basisOfPreparation);

        assertNotNull(basisOfPreparationService.getBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
        assertEquals(basisOfPreparation,
                        basisOfPreparationService.getBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Submit basis of preparation - create resource")
    void submitBasisOfPreparationCreateResource() throws ServiceException {

        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(null);

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        doNothing().when(transformer).setBasisOfPreparation(eq(basisOfPreparation), any(AccountingPoliciesApi.class));

        when(accountingPoliciesService
                .createAccountingPoliciesApi(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AccountingPoliciesApi.class)))
                .thenReturn(new ArrayList<>());

        basisOfPreparationService.submitBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, basisOfPreparation);

        verify(accountingPoliciesService, times(1))
                .createAccountingPoliciesApi(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AccountingPoliciesApi.class));
        verify(accountingPoliciesService, never())
                .updateAccountingPoliciesApi(eq(TRANSACTION_ID), eq(COMPANY_ACCOUNTS_ID), any(AccountingPoliciesApi.class));
    }

    @Test
    @DisplayName("Submit basis of preparation - update resource")
    void submitBasisOfPreparationUpdateResource() throws ServiceException {

        AccountingPoliciesApi accountingPoliciesApi = new AccountingPoliciesApi();
        when(accountingPoliciesService.getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(accountingPoliciesApi);

        BasisOfPreparation basisOfPreparation = new BasisOfPreparation();
        doNothing().when(transformer).setBasisOfPreparation(basisOfPreparation, accountingPoliciesApi);

        when(accountingPoliciesService
                .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi))
                .thenReturn(new ArrayList<>());

        basisOfPreparationService.submitBasisOfPreparation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, basisOfPreparation);

        verify(accountingPoliciesService, times(1))
                .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);
        verify(accountingPoliciesService, never())
                .createAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, accountingPoliciesApi);
    }

}
