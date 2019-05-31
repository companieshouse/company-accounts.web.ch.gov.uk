package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.balancesheetstatements.StatementsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.balancesheetstatements.request.StatementsCreate;
import uk.gov.companieshouse.api.handler.smallfull.balancesheetstatements.request.StatementsGet;
import uk.gov.companieshouse.api.handler.smallfull.balancesheetstatements.request.StatementsUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.BalanceSheetStatementsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.Statements;
import uk.gov.companieshouse.web.accounts.service.smallfull.StatementsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.StatementsTransformer;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StatementsServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private StatementsTransformer transformer;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private StatementsResourceHandler statementsResourceHandler;

    @Mock
    private StatementsCreate statementsCreate;

    @Mock
    private StatementsUpdate statementsUpdate;

    @Mock
    private StatementsGet statementsGet;

    @Mock
    private ApiResponse<BalanceSheetStatementsApi> responseWithData;

    @InjectMocks
    private StatementsService statementsService = new StatementsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String STATEMENTS_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                    COMPANY_ACCOUNTS_ID + "/small-full/statements";

    @BeforeEach
    void setUp() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.balanceSheetStatements()).thenReturn(statementsResourceHandler);
    }

    @Test
    @DisplayName("Get balance sheet statements - success")
    void getBalanceSheetStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler.get(STATEMENTS_URI)).thenReturn(statementsGet);

        BalanceSheetStatementsApi statementsApi = new BalanceSheetStatementsApi();
        when(statementsGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(statementsApi);
        when(transformer.getBalanceSheetStatements(statementsApi)).thenReturn(new Statements());

        Statements statements = statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(statements);
    }

    @Test
    @DisplayName("Get balance sheet statements - throws ApiErrorResponseException")
    void getBalanceSheetStatementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.get(STATEMENTS_URI)).thenReturn(statementsGet);

        when(statementsGet.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get balance sheet statements - throws URIValidationException")
    void getBalanceSheetStatementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.get(STATEMENTS_URI)).thenReturn(statementsGet);

        when(statementsGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                statementsService.getBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create balance sheet statements - success")
    void createBalanceSheetStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.create(anyString(), any(BalanceSheetStatementsApi.class)))
                .thenReturn(statementsCreate);

        assertAll(() -> statementsService.createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(statementsCreate).execute();
    }

    @Test
    @DisplayName("Create balance sheet statements - throws ApiErrorResponseException")
    void createBalanceSheetStatementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.create(anyString(), any(BalanceSheetStatementsApi.class)))
                .thenReturn(statementsCreate);
        when(statementsCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                statementsService.createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create balance sheet statements - throws URIValidationException")
    void createBalanceSheetStatementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.create(anyString(), any(BalanceSheetStatementsApi.class)))
                .thenReturn(statementsCreate);
        when(statementsCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                statementsService.createBalanceSheetStatementsResource(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Update balance sheet statements - success")
    void updateBalanceSheetStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.update(anyString(), any(BalanceSheetStatementsApi.class)))
                .thenReturn(statementsUpdate);

        assertAll(() -> statementsService.acceptBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(statementsUpdate).execute();
    }

    @Test
    @DisplayName("Update balance sheet statements - throws ApiErrorResponseException")
    void updateBalanceSheetStatementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.update(anyString(), any(BalanceSheetStatementsApi.class)))
                .thenReturn(statementsUpdate);
        doThrow(ApiErrorResponseException.class).when(statementsUpdate).execute();

        assertThrows(ServiceException.class, () ->
                statementsService.acceptBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Update balance sheet statements - throws URIValidationException")
    void updateBalanceSheetStatementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.update(anyString(), any(BalanceSheetStatementsApi.class)))
                .thenReturn(statementsUpdate);
        doThrow(URIValidationException.class).when(statementsUpdate).execute();

        assertThrows(ServiceException.class, () ->
                statementsService.acceptBalanceSheetStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

}
