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
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.request.FixedAssetsInvestmentsCreate;
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.request.FixedAssetsInvestmentsDelete;
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.request.FixedAssetsInvestmentsGet;
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.request.FixedAssetsInvestmentsUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FixedAssetsInvestmentsHandlerTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private FixedAssetsInvestmentsGet fixedAssetsInvestmentsGet;

    @Mock
    private FixedAssetsInvestmentsUpdate fixedAssetsInvestmentsUpdate;

    @Mock
    private FixedAssetsInvestmentsDelete fixedAssetsInvestmentsDelete;

    @Mock
    private FixedAssetsInvestmentsCreate fixedAssetsInvestmentsCreate;

    @Mock
    private FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private FixedAssetsInvestmentsResourceHandler fixedAssetsInvestmentsResourceHandler;

    @InjectMocks
    private FixedAssetsInvestmentsHandler fixedAssetsInvestmentsHandler;

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/fixed-assets-investments";

    private static final String FIXED_ASSETS_INVESTMENTS_NOTE = "fixedAssetsInvestment";

    @Test
    @DisplayName("Get FixedAssetsInvestments resource URI")
    void getFixedAssetsInvestmentsURI() {
        assertEquals(URI,
                fixedAssetsInvestmentsHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get FixedAssetsInvestments Resource")
    void getFixedAssetsInvestmentsResource() {

        setupFixedAssetsInvestmentsHandler();

        when(fixedAssetsInvestmentsHandler.get(apiClient, URI)).thenReturn(
                fixedAssetsInvestmentsGet);

        Executor<ApiResponse<FixedAssetsInvestmentsApi>> getFixedAssetsInvestmentsApi = fixedAssetsInvestmentsHandler.get(
                apiClient, URI);

        assertNotNull(getFixedAssetsInvestmentsApi);
        assertEquals(getFixedAssetsInvestmentsApi, fixedAssetsInvestmentsGet);
    }

    @Test
    @DisplayName("Update FixedAssetsInvestments Resource")
    void updateFixedAssetsInvestmentsResource() {

        setupFixedAssetsInvestmentsHandler();

        when(fixedAssetsInvestmentsHandler.update(apiClient, URI,
                fixedAssetsInvestmentsApi)).thenReturn(fixedAssetsInvestmentsUpdate);

        Executor<ApiResponse<Void>> updatedFixedAssetsInvestments = fixedAssetsInvestmentsHandler.update(
                apiClient, URI, fixedAssetsInvestmentsApi);

        assertNotNull(updatedFixedAssetsInvestments);
        assertEquals(updatedFixedAssetsInvestments, fixedAssetsInvestmentsUpdate);
    }

    @Test
    @DisplayName("Create FixedAssetsInvestments Resource")
    void createFixedAssetsInvestmentsResource() {

        setupFixedAssetsInvestmentsHandler();

        when(fixedAssetsInvestmentsHandler.create(apiClient, URI,
                fixedAssetsInvestmentsApi)).thenReturn(fixedAssetsInvestmentsCreate);

        Executor<ApiResponse<FixedAssetsInvestmentsApi>> createFixedAssetsInvestments = fixedAssetsInvestmentsHandler.create(
                apiClient, URI, fixedAssetsInvestmentsApi);

        assertNotNull(createFixedAssetsInvestments);
        assertEquals(createFixedAssetsInvestments, fixedAssetsInvestmentsCreate);
    }

    @Test
    @DisplayName("Delete FixedAssetsInvestments Resource")
    void deleteFixedAssetsInvestmentsResource() {

        setupFixedAssetsInvestmentsHandler();

        when(fixedAssetsInvestmentsHandler.delete(apiClient, URI)).thenReturn(
                fixedAssetsInvestmentsDelete);

        Executor<ApiResponse<Void>> deleteFixedAssetsInvestmentsApi = fixedAssetsInvestmentsHandler.delete(
                apiClient, URI);

        assertNotNull(deleteFixedAssetsInvestmentsApi);
        assertEquals(deleteFixedAssetsInvestmentsApi, fixedAssetsInvestmentsDelete);
    }

    @Test
    @DisplayName("Test parent resource exist")
    void testParentResourceExist() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getFixedAssetsInvestmentsNote()).thenReturn(
                FIXED_ASSETS_INVESTMENTS_NOTE);

        assertTrue(fixedAssetsInvestmentsHandler.parentResourceExists(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test parent resource throws service exception")
    void testParentResourceThrowsServiceException() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class,
                () -> fixedAssetsInvestmentsHandler.parentResourceExists(apiClient, TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test method returns FixedAssetsInvestments as NoteType")
    void testFixedAssetsInvestmentsReturned() {

        assertEquals(NoteType.SMALL_FULL_FIXED_ASSETS_INVESTMENT,
                fixedAssetsInvestmentsHandler.getNoteType());
    }

    private void setupFixedAssetsInvestmentsHandler() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.fixedAssetsInvestments()).thenReturn(
                fixedAssetsInvestmentsResourceHandler);
    }
}
