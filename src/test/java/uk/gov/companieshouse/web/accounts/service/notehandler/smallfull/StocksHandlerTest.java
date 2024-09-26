package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.stocks.StocksResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksCreate;
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksDelete;
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksGet;
import uk.gov.companieshouse.api.handler.smallfull.stocks.request.StocksUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.stocks.StocksApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StocksHandlerTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private StocksResourceHandler stocksResourceHandler;

    @Mock
    private StocksGet stocksGet;

    @Mock
    private StocksUpdate stocksUpdate;

    @Mock
    private StocksCreate stocksCreate;

    @Mock
    private StocksDelete stocksDelete;

    @Mock
    private StocksApi stocksApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @InjectMocks
    private StocksHandler stocksHandler;

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI =
            "/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID
                    + "/small-full/notes/stocks";

    private static final String STOCKS_NOTE = "stocksNote";

    @Test
    @DisplayName("Get the resource URI")
    void getResourceURI() {

        assertEquals(URI, stocksHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Stocks Resource")
    void getStocksResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.stocks()).thenReturn(stocksResourceHandler);
        when(stocksResourceHandler.get(URI)).thenReturn(stocksGet);

        Executor<ApiResponse<StocksApi>> stocksApi = stocksHandler.get(apiClient, URI);

        assertNotNull(stocksApi);
        assertEquals(stocksApi, stocksGet);
    }

    @Test
    @DisplayName("Update Stocks Resource")
    void updateStocksResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.stocks()).thenReturn(stocksResourceHandler);
        when(stocksResourceHandler.update(URI, stocksApi)).thenReturn(stocksUpdate);

        Executor<ApiResponse<Void>> updatedStocksApi = stocksHandler.update(apiClient, URI,
                stocksApi);

        assertNotNull(updatedStocksApi);
        assertEquals(updatedStocksApi, stocksUpdate);
    }

    @Test
    @DisplayName("Create Stocks Resource")
    void createStocksResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.stocks()).thenReturn(stocksResourceHandler);
        when(stocksResourceHandler.create(URI, stocksApi)).thenReturn(stocksCreate);

        Executor<ApiResponse<StocksApi>> createStocksApi = stocksHandler.create(apiClient, URI,
                stocksApi);

        assertNotNull(createStocksApi);
        assertEquals(createStocksApi, stocksCreate);
    }

    @Test
    @DisplayName("Delete Stocks Resource")
    void deleteStocksResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.stocks()).thenReturn(stocksResourceHandler);
        when(stocksResourceHandler.delete(URI)).thenReturn(stocksDelete);

        Executor<ApiResponse<Void>> deleteStocksApi = stocksHandler.delete(apiClient, URI);

        assertNotNull(deleteStocksApi);
        assertEquals(deleteStocksApi, stocksDelete);
    }

    @Test
    @DisplayName("Parent resource exists")
    void parentResourceExists() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getStocksNote()).thenReturn(STOCKS_NOTE);

        assertTrue(
                stocksHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Parent resource does not exist")
    void parentResourceDoesNotExist() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getStocksNote()).thenReturn(null);

        assertFalse(
                stocksHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get NoteType")
    void getNoteType() {

        assertEquals(NoteType.SMALL_FULL_STOCKS, stocksHandler.getNoteType());
    }
}