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
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.request.CurrentAssetsInvestmentsCreate;
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.request.CurrentAssetsInvestmentsDelete;
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.request.CurrentAssetsInvestmentsGet;
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.request.CurrentAssetsInvestmentsUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrentAssetsInvestmentsHandlerTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CurrentAssetsInvestmentsResourceHandler currentAssetsInvestmentsResourceHandler;

    @Mock
    private CurrentAssetsInvestmentsGet currentAssetsInvestmentsGet;

    @Mock
    private CurrentAssetsInvestmentsUpdate currentAssetsInvestmentsUpdate;

    @Mock
    private CurrentAssetsInvestmentsCreate currentAssetsInvestmentsCreate;

    @Mock
    private CurrentAssetsInvestmentsDelete currentAssetsInvestmentsDelete;

    @Mock
    private CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @InjectMocks
    private CurrentAssetsInvestmentsHandler currentAssetsInvestmentsHandler;

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID + "/small-full/notes/current-assets-investments";

    private static final String CURRENT_ASSETS_INVESTMENTS = "currentAssetsInvestments";

    @Test
    @DisplayName("Get the resource URI")
    void getResourceURI() {

        assertEquals(URI, currentAssetsInvestmentsHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get CurrentAssetsInvestments Resource")
    void getCurrentAssetsInvestmentsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.currentAssetsInvestments()).thenReturn(currentAssetsInvestmentsResourceHandler);
        when(currentAssetsInvestmentsResourceHandler.get(URI)).thenReturn(currentAssetsInvestmentsGet);

        Executor<ApiResponse<CurrentAssetsInvestmentsApi>> getCurrentAssetsInvestmentsApi = currentAssetsInvestmentsHandler.get(apiClient, URI);

        assertNotNull(getCurrentAssetsInvestmentsApi);
        assertEquals(currentAssetsInvestmentsGet, getCurrentAssetsInvestmentsApi);
    }

    @Test
    @DisplayName("Update CurrentAssetsInvestments Resource")
    void updateCurrentAssetsInvestmentsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.currentAssetsInvestments()).thenReturn(currentAssetsInvestmentsResourceHandler);
        when(currentAssetsInvestmentsResourceHandler.update(URI, currentAssetsInvestmentsApi)).thenReturn(currentAssetsInvestmentsUpdate);

        Executor<ApiResponse<Void>> updatedCurrentAssetsInvestmentsApi = currentAssetsInvestmentsHandler.update(apiClient, URI, currentAssetsInvestmentsApi);

        assertNotNull(updatedCurrentAssetsInvestmentsApi);
        assertEquals(currentAssetsInvestmentsUpdate, updatedCurrentAssetsInvestmentsApi);
    }

    @Test
    @DisplayName("Create CurrentAssetsInvestments Resource")
    void createCurrentAssetsInvestmentsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.currentAssetsInvestments()).thenReturn(currentAssetsInvestmentsResourceHandler);
        when(currentAssetsInvestmentsResourceHandler.create(URI, currentAssetsInvestmentsApi)).thenReturn(currentAssetsInvestmentsCreate);

        Executor<ApiResponse<CurrentAssetsInvestmentsApi>> createCurrentAssetsInvestmentsApi = currentAssetsInvestmentsHandler.create(apiClient, URI, currentAssetsInvestmentsApi);

        assertNotNull(createCurrentAssetsInvestmentsApi);
        assertEquals(currentAssetsInvestmentsCreate, createCurrentAssetsInvestmentsApi);
    }

    @Test
    @DisplayName("Delete CurrentAssetsInvestments Resource")
    void deleteCurrentAssetsInvestmentsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.currentAssetsInvestments()).thenReturn(currentAssetsInvestmentsResourceHandler);
        when(currentAssetsInvestmentsResourceHandler.delete(URI)).thenReturn(currentAssetsInvestmentsDelete);

        Executor<ApiResponse<Void>> deleteCurrentAssetsInvestmentsApi = currentAssetsInvestmentsHandler.delete(apiClient, URI);

        assertNotNull(deleteCurrentAssetsInvestmentsApi);
        assertEquals(currentAssetsInvestmentsDelete, deleteCurrentAssetsInvestmentsApi);
    }

    @Test
    @DisplayName("Parent resource exists")
    void parentResourceExists() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentAssetsInvestmentsNote()).thenReturn(CURRENT_ASSETS_INVESTMENTS);

        assertTrue(currentAssetsInvestmentsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Parent resource does not exist")
    void parentResourceDoesNotExist() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentAssetsInvestmentsNote()).thenReturn(null);

        assertFalse(currentAssetsInvestmentsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get NoteType")
    void getNoteType()  {

        assertEquals(NoteType.SMALL_FULL_CURRENT_ASSETS_INVESTMENTS, currentAssetsInvestmentsHandler.getNoteType());
    }
}