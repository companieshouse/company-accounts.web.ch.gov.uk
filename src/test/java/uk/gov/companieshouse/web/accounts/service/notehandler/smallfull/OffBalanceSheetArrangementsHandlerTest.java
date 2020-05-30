package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.OffBalanceSheetResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetCreate;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetDelete;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetGet;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OffBalanceSheetArrangementsHandlerTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private OffBalanceSheetGet offBalanceSheetGet;

    @Mock
    private OffBalanceSheetUpdate offBalanceSheetUpdate;

    @Mock
    private OffBalanceSheetCreate offBalanceSheetCreate;

    @Mock
    private OffBalanceSheetDelete offBalanceSheetDelete;

    @Mock
    private OffBalanceSheetApi offBalanceSheetApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @InjectMocks
    private OffBalanceSheetArrangementsHandler offBalanceSheetArrangementsHandler;

    @Mock
    private OffBalanceSheetResourceHandler offBalanceSheetResourceHandler;


    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID + "/small-full/notes/off-balance-sheet-arrangements";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_NOTE = "offBalanceSheetArrangements";

    @Test
    @DisplayName("Get the resource URI")
    void getResourceURI() {

        assertEquals(URI, offBalanceSheetArrangementsHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get OffBalanceSheet Resource")
    void getOffBalanceSheetResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.offBalanceSheet()).thenReturn(offBalanceSheetResourceHandler);
        when(offBalanceSheetResourceHandler.get(URI)).thenReturn(offBalanceSheetGet);

        Executor<ApiResponse<OffBalanceSheetApi>> offBalanceSheetApi = offBalanceSheetArrangementsHandler.get(apiClient, URI);

        assertNotNull(offBalanceSheetApi);
        assertEquals(offBalanceSheetApi, offBalanceSheetGet);
        verify(offBalanceSheetResourceHandler).get(URI);
    }

    @Test
    @DisplayName("Update OffBalanceSheet Resource")
    void updateOffBalanceSheetResource() throws ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.offBalanceSheet()).thenReturn(offBalanceSheetResourceHandler);
        when(offBalanceSheetResourceHandler.update(URI, offBalanceSheetApi)).thenReturn(offBalanceSheetUpdate);

        Executor<ApiResponse<Void>> updatedOffBalanceSheetApi = offBalanceSheetArrangementsHandler.update(apiClient, URI, offBalanceSheetApi);

        assertNotNull(updatedOffBalanceSheetApi);
        assertEquals(updatedOffBalanceSheetApi, offBalanceSheetUpdate);
        verify(offBalanceSheetResourceHandler).update(URI, offBalanceSheetApi);
    }

    @Test
    @DisplayName("Create OffBalanceSheet Resource")
    void createOffBalanceSheetResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.offBalanceSheet()).thenReturn(offBalanceSheetResourceHandler);
        when(offBalanceSheetResourceHandler.create(URI, offBalanceSheetApi)).thenReturn(offBalanceSheetCreate);

        Executor<ApiResponse<OffBalanceSheetApi>> createOffBalanceSheetApi = offBalanceSheetArrangementsHandler.create(apiClient, URI, offBalanceSheetApi);

        assertNotNull(createOffBalanceSheetApi);
        assertEquals(createOffBalanceSheetApi, offBalanceSheetCreate);
        verify(offBalanceSheetResourceHandler).create(URI, offBalanceSheetApi);
    }

    @Test
    @DisplayName("Delete OffBalanceSheet Resource")
    void deleteOffBalanceSheetResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.offBalanceSheet()).thenReturn(offBalanceSheetResourceHandler);
        when(offBalanceSheetResourceHandler.delete(URI)).thenReturn(offBalanceSheetDelete);

        Executor<ApiResponse<Void>> deleteOffBalanceSheetApi = offBalanceSheetArrangementsHandler.delete(apiClient, URI);

        assertNotNull(deleteOffBalanceSheetApi);
        assertEquals(deleteOffBalanceSheetApi, offBalanceSheetDelete);
        verify(offBalanceSheetResourceHandler).delete(URI);
    }

    @Test
    @DisplayName("Test parent resource exist")
    void testParentResourceExist() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getOffBalanceSheetArrangementsNote()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_NOTE);

        assertTrue(offBalanceSheetArrangementsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

   @Test
    @DisplayName("Test parent resource throws service exception")
    void testParentResourceThrowsServiceException() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> offBalanceSheetArrangementsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test method returns OffBalanceSheetArrangements as NoteType")
    void testOffBalanceSheetArrangementsReturned()  {

        assertEquals(NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS, offBalanceSheetArrangementsHandler.getNoteType());
    }
}