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
import uk.gov.companieshouse.api.handler.smallfull.tangible.TangibleResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleCreate;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleDelete;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleGet;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TangibleAssetsHandlerTest {
    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private TangibleResourceHandler tangibleAssetsResourceHandler;

    @Mock
    private TangibleGet tangibleAssetsGet;

    @Mock
    private TangibleUpdate tangibleAssetsUpdate;

    @Mock
    private TangibleCreate tangibleAssetsCreate;

    @Mock
    private TangibleDelete tangibleAssetsDelete;

    @Mock
    private TangibleApi tangibleAssetsApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @InjectMocks
    private TangibleAssetsHandler tangibleAssetsHandler;

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID + "/small-full/notes/tangible-assets";

    private static final String TANGIBLE_ASSETS = "tangibleAssets";

    @Test
    @DisplayName("Get the resource URI")
    void getResourceURI() {
        assertEquals(URI, tangibleAssetsHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get TangibleAssets Resource")
    void getTangibleAssetsResource() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.tangible()).thenReturn(tangibleAssetsResourceHandler);
        when(tangibleAssetsResourceHandler.get(URI)).thenReturn(tangibleAssetsGet);

        Executor<ApiResponse<TangibleApi>> getTangibleApi = tangibleAssetsHandler.get(apiClient, URI);

        assertNotNull(getTangibleApi);
        assertEquals(tangibleAssetsGet, getTangibleApi);
    }

    @Test
    @DisplayName("Update TangibleAssets Resource")
    void updateTangibleAssetsResource() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.tangible()).thenReturn(tangibleAssetsResourceHandler);
        when(tangibleAssetsResourceHandler.update(URI, tangibleAssetsApi)).thenReturn(tangibleAssetsUpdate);

        Executor<ApiResponse<Void>> updatedTangibleApi = tangibleAssetsHandler.update(apiClient, URI, tangibleAssetsApi);

        assertNotNull(updatedTangibleApi);
        assertEquals(tangibleAssetsUpdate, updatedTangibleApi);
    }

    @Test
    @DisplayName("Create TangibleAssets Resource")
    void createTangibleAssetsResource() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.tangible()).thenReturn(tangibleAssetsResourceHandler);
        when(tangibleAssetsResourceHandler.create(URI, tangibleAssetsApi)).thenReturn(tangibleAssetsCreate);

        Executor<ApiResponse<TangibleApi>> createTangibleApi = tangibleAssetsHandler.create(apiClient, URI, tangibleAssetsApi);

        assertNotNull(createTangibleApi);
        assertEquals(tangibleAssetsCreate, createTangibleApi);
    }

    @Test
    @DisplayName("Delete TangibleAssets Resource")
    void deleteTangibleAssetsResource() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.tangible()).thenReturn(tangibleAssetsResourceHandler);
        when(tangibleAssetsResourceHandler.delete(URI)).thenReturn(tangibleAssetsDelete);

        Executor<ApiResponse<Void>> deleteTangibleApi = tangibleAssetsHandler.delete(apiClient, URI);

        assertNotNull(deleteTangibleApi);
        assertEquals(tangibleAssetsDelete, deleteTangibleApi);
    }

    @Test
    @DisplayName("Parent resource exists")
    void parentResourceExists() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn(TANGIBLE_ASSETS);

        assertTrue(tangibleAssetsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Parent resource does not exist")
    void parentResourceDoesNotExist() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn(null);

        assertFalse(tangibleAssetsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get NoteType")
    void getNoteType()  {
        assertEquals(NoteType.SMALL_FULL_TANGIBLE_ASSETS, tangibleAssetsHandler.getNoteType());
    }
}