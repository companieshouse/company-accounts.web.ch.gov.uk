package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

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
import uk.gov.companieshouse.api.handler.smallfull.debtors.DebtorsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsCreate;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsDelete;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsGet;
import uk.gov.companieshouse.api.handler.smallfull.debtors.request.DebtorsUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.Debtors.DebtorsApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DebtorsHandlerTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private DebtorsResourceHandler debtorsResourceHandler;

    @Mock
    private DebtorsGet debtorsGet;

    @Mock
    private DebtorsUpdate debtorsUpdate;

    @Mock
    private DebtorsCreate debtorsCreate;

    @Mock
    private DebtorsDelete debtorsDelete;

    @Mock
    private DebtorsApi debtorsApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @InjectMocks
    private DebtorsHandler debtorsHandler;

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" + COMPANY_ACCOUNTS_ID + "/small-full/notes/debtors";

    private static final String DEBTORS_NOTE = "debtorsNote";

    @Test
    @DisplayName("Get the resource URI")
    void getResourceURI() {

        assertEquals(URI, debtorsHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Debtors Resource")
    void getDebtorsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.debtors()).thenReturn(debtorsResourceHandler);
        when(debtorsResourceHandler.get(URI)).thenReturn(debtorsGet);

        Executor<ApiResponse<DebtorsApi>> debtorsApi = debtorsHandler.get(apiClient, URI);

        assertNotNull(debtorsApi);
        assertEquals(debtorsApi, debtorsGet);
    }

    @Test
    @DisplayName("Update Debtors Resource")
    void updateDebtorsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.debtors()).thenReturn(debtorsResourceHandler);
        when(debtorsResourceHandler.update(URI, debtorsApi)).thenReturn(debtorsUpdate);

        Executor<ApiResponse<Void>> updatedDebtorsApi = debtorsHandler.update(apiClient, URI, debtorsApi);

        assertNotNull(updatedDebtorsApi);
        assertEquals(updatedDebtorsApi, debtorsUpdate);
    }

    @Test
    @DisplayName("Create Debtors Resource")
    void createDebtorsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.debtors()).thenReturn(debtorsResourceHandler);
        when(debtorsResourceHandler.create(URI, debtorsApi)).thenReturn(debtorsCreate);

        Executor<ApiResponse<DebtorsApi>> createDebtorsApi = debtorsHandler.create(apiClient, URI, debtorsApi);

        assertNotNull(createDebtorsApi);
        assertEquals(createDebtorsApi, debtorsCreate);
    }

    @Test
    @DisplayName("Delete Debtors Resource")
    void deleteDebtorsResource() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.debtors()).thenReturn(debtorsResourceHandler);
        when(debtorsResourceHandler.delete(URI)).thenReturn(debtorsDelete);

        Executor<ApiResponse<Void>> deleteDebtorsApi = debtorsHandler.delete(apiClient, URI);

        assertNotNull(deleteDebtorsApi);
        assertEquals(deleteDebtorsApi, debtorsDelete);
    }

    @Test
    @DisplayName("Parent resource exists")
    void parentResourceExists() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getDebtorsNote()).thenReturn(DEBTORS_NOTE);

        assertTrue(debtorsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Parent resource does not exist")
    void parentResourceDoesNotExist() throws ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getDebtorsNote()).thenReturn(null);

        assertFalse(debtorsHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get NoteType")
    void getNoteType()  {

        assertEquals(NoteType.SMALL_FULL_DEBTORS, debtorsHandler.getNoteType());
    }
}