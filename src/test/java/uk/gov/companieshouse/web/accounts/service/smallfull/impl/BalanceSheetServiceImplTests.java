package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.transaction.TransactionResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.CompanyAccountResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.transaction.companyaccount.smallfull.subresource.CurrentPeriodResourceHandler;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriod;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.BalanceSheetTransformer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BalanceSheetServiceImplTests {

    @Mock
    private BalanceSheetTransformer transformer;

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private TransactionResourceHandler transactionResourceHandler;

    @Mock
    private CompanyAccountResourceHandler companyAccountResourceHandler;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CurrentPeriodResourceHandler currentPeriodResourceHandler;

    @InjectMocks
    private BalanceSheetService balanceSheetService = new BalanceSheetServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @BeforeEach
    private void init() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.transaction(TRANSACTION_ID)).thenReturn(transactionResourceHandler);

        when(transactionResourceHandler.companyAccount(COMPANY_ACCOUNTS_ID))
                .thenReturn(companyAccountResourceHandler);

        when(companyAccountResourceHandler.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);
    }

    @Test
    @DisplayName("Get Balance Sheet - Success Path")
    void getBalanceSheetSuccess() throws ApiErrorResponseException {

        when(currentPeriodResourceHandler.get()).thenReturn(new CurrentPeriod());

        when(transformer.getBalanceSheet()).thenReturn(new BalanceSheet());

        BalanceSheet balanceSheet = balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(balanceSheet);
    }

    @Test
    @DisplayName("Get Balance Sheet - Throws ApiErrorResponseException")
    void getBalanceSheetThrowsApiErrorResponseException() throws ApiErrorResponseException {

        when(currentPeriodResourceHandler.get()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ApiErrorResponseException.class, () ->
                balanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
