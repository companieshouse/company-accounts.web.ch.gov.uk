package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPoliciesApi;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.accountingpolicies.TurnoverPolicy;
import uk.gov.companieshouse.web.accounts.service.smallfull.AccountingPoliciesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TurnoverPolicyService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.AccountingPoliciesTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TurnoverPolicyServiceImplTest {

    private static final String TURNOVER_POLICY_DETAILS = "Turnover policy details";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @Mock
    private AccountingPoliciesTransformer accountingPoliciesTransformerMock;
    @Mock
    private AccountingPoliciesService accountingPoliciesServiceMock;
    @Mock
    private AccountingPoliciesApi accountingPoliciesApiMock;

    private TurnoverPolicyService turnoverPolicyService;

    @BeforeEach
    void setUpBeforeEach() {
        turnoverPolicyService = new TurnoverPolicyServiceImpl(accountingPoliciesServiceMock,
            accountingPoliciesTransformerMock);
    }

    @Test
    @DisplayName("Get the turnoverPolicy")
    void shouldGetTurnoverPolicy() throws ServiceException {

        when(accountingPoliciesServiceMock
            .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApiMock);

        TurnoverPolicy turnoverPolicyFromTransformer = createTurnOverPolicy();
        when(accountingPoliciesTransformerMock.getTurnoverPolicy(accountingPoliciesApiMock))
            .thenReturn(turnoverPolicyFromTransformer);

        TurnoverPolicy turnOverPolicy =
            turnoverPolicyService.getTurnOverPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(turnOverPolicy);
        assertEquals(turnoverPolicyFromTransformer, turnOverPolicy);
    }

    @Test
    @DisplayName("Post a turnoverPolicy")
    void shouldPostTurnoverPolicy() throws ServiceException {

        when(accountingPoliciesServiceMock
            .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(accountingPoliciesApiMock);

        TurnoverPolicy turnoverPolicy = createTurnOverPolicy();

        doNothing()
            .when(accountingPoliciesTransformerMock)
            .setTurnoverPolicy(turnoverPolicy, accountingPoliciesApiMock);

        turnoverPolicyService
            .postTurnoverPolicy(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, turnoverPolicy);

        verify(accountingPoliciesTransformerMock, times(1))
            .setTurnoverPolicy(turnoverPolicy, accountingPoliciesApiMock);

        verify(accountingPoliciesServiceMock, times(1))
            .getAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(accountingPoliciesServiceMock, times(1))
            .updateAccountingPoliciesApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                accountingPoliciesApiMock);

    }

    private TurnoverPolicy createTurnOverPolicy() {
        TurnoverPolicy turnoverPolicy = new TurnoverPolicy();

        turnoverPolicy.setIsIncludeTurnoverSelected(true);
        turnoverPolicy.setTurnoverPolicyDetails(TURNOVER_POLICY_DETAILS);

        return turnoverPolicy;
    }
}
