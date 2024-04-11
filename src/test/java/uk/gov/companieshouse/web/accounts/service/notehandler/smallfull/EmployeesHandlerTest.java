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
import uk.gov.companieshouse.api.handler.smallfull.employees.EmployeesResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesCreate;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesDelete;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesGet;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeesHandlerTest {
    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private EmployeesGet employeesGet;

    @Mock
    private EmployeesUpdate employeesUpdate;

    @Mock
    private EmployeesDelete employeesDelete;

    @Mock
    private EmployeesCreate employeesCreate;

    @Mock
    private EmployeesApi employeesApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private EmployeesResourceHandler employeesResourceHandler;

    @InjectMocks
    private EmployeesHandler employeesHandler;
    
    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";
    private static final String TRANSACTION_ID = "transactionId";

    private static final String URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/employees";

    private static final String EMPLOYEES_NOTE = "employeesNote";

    @Test
    @DisplayName("Get Employees resource URI")
    void getEmployeesURI() {
        assertEquals(URI, employeesHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get Employees Resource")
    void getEmployeesResource() {
        setupEmployeesHandler();

        when(employeesHandler.get(apiClient, URI)).thenReturn(employeesGet);

        Executor<ApiResponse<EmployeesApi>> employeesApiGet = employeesHandler.get(apiClient, URI);

        assertNotNull(employeesApiGet);
        assertEquals(employeesApiGet, employeesGet);
    }

    @Test
    @DisplayName("Update Employees Resource")
    void updateEmployeesResource() {
        setupEmployeesHandler();

        when(employeesHandler.update(apiClient, URI, employeesApi)).thenReturn(employeesUpdate);

        Executor<ApiResponse<Void>> updatedEmployees = employeesHandler.update(apiClient, URI, employeesApi);

        assertNotNull(updatedEmployees);
        assertEquals(updatedEmployees, employeesUpdate);
    }

    @Test
    @DisplayName("Create Employees Resource")
    void createEmployeesResource() {
        setupEmployeesHandler();

        when(employeesHandler.create(apiClient, URI, employeesApi)).thenReturn(employeesCreate);

        Executor<ApiResponse<EmployeesApi>> createEmployeesApi = employeesHandler.create(apiClient, URI, employeesApi);

        assertNotNull(createEmployeesApi);
        assertEquals(createEmployeesApi, employeesCreate);
    }

    @Test
    @DisplayName("Delete Employees Resource")
    void deleteEmployeesResource() {
        setupEmployeesHandler();

        when(employeesHandler.delete(apiClient, URI)).thenReturn(employeesDelete);

        Executor<ApiResponse<Void>> deleteEmployeesApi = employeesHandler.delete(apiClient, URI);

        assertNotNull(deleteEmployeesApi);
        assertEquals(deleteEmployeesApi, employeesDelete);
    }

    @Test
    @DisplayName("Test parent resource exist")
    void testParentResourceExist() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getEmployeesNote()).thenReturn(EMPLOYEES_NOTE);

        assertTrue(employeesHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test parent resource throws service exception")
    void testParentResourceThrowsServiceException() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> employeesHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Test method returns Employees as NoteType")
    void testEmployeesReturned()  {
        assertEquals(NoteType.SMALL_FULL_EMPLOYEES, employeesHandler.getNoteType());
    }

    private void setupEmployeesHandler() {
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.employees()).thenReturn(employeesResourceHandler);
    }
}