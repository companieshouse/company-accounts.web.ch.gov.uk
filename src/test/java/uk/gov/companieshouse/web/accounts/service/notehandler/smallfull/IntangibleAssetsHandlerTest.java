package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import uk.gov.companieshouse.api.handler.smallfull.intangible.IntangibleResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleCreate;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleDelete;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleGet;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IntangibleAssetsHandlerTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private IntangibleGet intangibleGet;

    @Mock
    private IntangibleUpdate intangibleUpdate;

    @Mock
    private IntangibleDelete intangibleDelete;

    @Mock
    private IntangibleCreate intangibleCreate;

    @Mock
    private IntangibleApi intangibleApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private IntangibleResourceHandler intangibleResourceHandler;

    @InjectMocks
    private IntangibleAssetsHandler intangibleHandler;

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/intangible-assets";

    private static final String INTANGIBLE_NOTE = "intangibleNote";

    @Test
    @DisplayName("Get Intangible Assets resource URI")
    void getIntangibleAssetsURI() {
        assertEquals(URI, intangibleHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Intangible Resource")
    void getIntangibleResource() {

        setupIntangibleHandler();

        when(intangibleHandler.get(apiClient, URI)).thenReturn(intangibleGet);

        Executor<ApiResponse<IntangibleApi>> intangibleApiGet = intangibleHandler.get(apiClient,
                URI);

        assertNotNull(intangibleApiGet);
        assertEquals(intangibleApiGet, intangibleGet);
    }

    @Test
    @DisplayName("Update Intangible Resource")
    void updateIntangibleResource() {

        setupIntangibleHandler();

        when(intangibleHandler.update(apiClient, URI, intangibleApi)).thenReturn(intangibleUpdate);

        Executor<ApiResponse<Void>> updatedIntangible = intangibleHandler.update(apiClient, URI,
                intangibleApi);

        assertNotNull(updatedIntangible);
        assertEquals(updatedIntangible, intangibleUpdate);
    }


    @Test
    @DisplayName("Create Intangible Resource")
    void createIntangibleResource() {

        setupIntangibleHandler();

        when(intangibleHandler.create(apiClient, URI, intangibleApi)).thenReturn(intangibleCreate);

        Executor<ApiResponse<IntangibleApi>> createIntangibleApi = intangibleHandler.create(
                apiClient, URI, intangibleApi);

        assertNotNull(createIntangibleApi);
        assertEquals(createIntangibleApi, intangibleCreate);
    }

    @Test
    @DisplayName("Delete Intangible Resource")
    void deleteIntangibleResource() {

        setupIntangibleHandler();

        when(intangibleHandler.delete(apiClient, URI)).thenReturn(intangibleDelete);

        Executor<ApiResponse<Void>> deleteIntangibleApi = intangibleHandler.delete(apiClient, URI);

        assertNotNull(deleteIntangibleApi);
        assertEquals(deleteIntangibleApi, intangibleDelete);
    }

    @Test
    @DisplayName("Test parent resource exist")
    void testParentResourceExist() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getIntangibleAssetsNote()).thenReturn(INTANGIBLE_NOTE);

        assertTrue(intangibleHandler.parentResourceExists(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test parent resource throws service exception")
    void testParentResourceThrowsServiceException() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class,
                () -> intangibleHandler.parentResourceExists(apiClient, TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test method returns Intangible as NoteType")
    void testIntangibleReturned() {

        assertEquals(NoteType.SMALL_FULL_INTANGIBLE_ASSETS, intangibleHandler.getNoteType());
    }

    private void setupIntangibleHandler() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.intangible()).thenReturn(intangibleResourceHandler);
    }
}
