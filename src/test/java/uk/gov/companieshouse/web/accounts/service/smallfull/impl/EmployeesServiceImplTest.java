package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

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
import uk.gov.companieshouse.api.handler.smallfull.employees.EmployeesResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesDelete;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesCreate;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesGet;
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.AverageNumberOfEmployees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.EmployeesTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeesServiceImplTest {

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    @Mock
    private EmployeesResourceHandler mockEmployeesResourceHandler;

    @Mock
    private EmployeesGet mockEmployeesGet;

    @Mock
    private EmployeesCreate mockEmployeesCreate;

    @Mock
    private EmployeesUpdate mockEmployeesUpdate;

    @Mock
    private EmployeesDelete mockEmployeesDelete;

    @Mock
    private EmployeesTransformer mockEmployeesTransformer;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext mockValidationContext;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private ApiResponse<EmployeesApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private EmployeesService employeesService = new EmployeesServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String BASE_SMALL_FULL_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full";

    private static final String EMPLOYEES_URI = BASE_SMALL_FULL_URI + "/notes/employees";

    private static final String DETAILS = "test";

    private static final String RESOURCE_NAME = "employees";

    @Test
    @DisplayName("GET - Employees successful path")
    void getEmployeesSuccess() throws Exception {

        EmployeesApi employeesApi = new EmployeesApi();
        getMockEmployeesApi(employeesApi);

        when(mockEmployeesTransformer.getEmployees(employeesApi)).thenReturn(createEmployees());

        Employees employees = employeesService.getEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(employees);
        assertNotNull(employees.getAverageNumberOfEmployees());
        assertNotNull(employees.getAverageNumberOfEmployees().getCurrentAverageNumberOfEmployees());
        assertNotNull(employees.getAverageNumberOfEmployees().getPreviousAverageNumberOfEmployees());
        assertEquals(DETAILS, employees.getDetails());
    }

    @Test
    @DisplayName("GET - Employees successful path when http status not found")
    void getEmployeesSuccessHttpStatusNotFound() throws Exception {

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        when(mockEmployeesTransformer.getEmployees(null)).thenReturn(new Employees());

        Employees employees = employeesService.getEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(employees);
        assertNull(employees.getAverageNumberOfEmployees());
        assertNull(employees.getDetails());
    }

    @Test
    @DisplayName("GET - Employees throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void getEmployeesApiResponseException() throws Exception {

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> employeesService.getEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Employees throws ServiceException due to URIValidationException")
    void getEmployeesURIValidationException() throws Exception {

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> employeesService.getEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @DisplayName("POST - Employees successful path")
    void postEmployeesSuccess() throws Exception {

        Employees employees = createEmployees();
        EmployeesApi employeesApi = createEmployeesApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        setLinksWithoutEmployees(smallFullApi);

        when(mockEmployeesTransformer.getEmployeesApi(employees)).thenReturn(employeesApi);

        employeesCreate(employeesApi);

        List<ValidationError> validationErrors = employeesService.submitEmployees(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, employees, COMPANY_NUMBER) ;

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("POST - Employees throws ServiceException due to ApiErrorResponseException - 404 Not Found")
    void postEmployeesApiErrorResponseExceptionNotFound() throws Exception {

        getMockEmployeesResourceHandler();

        Employees employees = createEmployees();
        EmployeesApi employeesApi = createEmployeesApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutEmployees(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockEmployeesTransformer.getEmployeesApi(employees)).thenReturn(employeesApi);
        when(mockEmployeesResourceHandler.create(EMPLOYEES_URI, employeesApi)).thenReturn(mockEmployeesCreate);

        when(mockEmployeesCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> employeesService.submitEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            employees,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Employees throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void postEmployeesApiErrorResponseExceptionBadRequest() throws Exception {

        getMockEmployeesResourceHandler();

        Employees employees = createEmployees();
        EmployeesApi employeesApi = createEmployeesApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutEmployees(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockEmployeesTransformer.getEmployeesApi(employees)).thenReturn(employeesApi);
        when(mockEmployeesResourceHandler.create(EMPLOYEES_URI, employeesApi)).thenReturn(mockEmployeesCreate);

        when(mockEmployeesCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> employeesService.submitEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            employees,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Employees throws ServiceException getting Smallfull data")
    void postEmployeesGetSmallFullDataApiResponseException() throws Exception {

        Employees employees = new Employees();

        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> employeesService.submitEmployees(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                employees,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("PUT - Employees successful path")
    void putEmployeesSuccess() throws Exception {

        Employees employees = createEmployees();
        EmployeesApi employeesApi = createEmployeesApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithEmployees(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockEmployeesTransformer.getEmployeesApi(employees)).thenReturn(employeesApi);
        employeesUpdate(employeesApi);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors = employeesService.submitEmployees(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, employees, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("PUT - Employees throws ServiceExcepiton due to URIValidationException")
    void putEmployeesURIValidationException() throws Exception {

        Employees employees = createEmployees();
        EmployeesApi employeesApi = createEmployeesApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithEmployees(smallFullApi);

        when(smallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);

        when(mockEmployeesTransformer.getEmployeesApi(employees)).thenReturn(employeesApi);

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.update(EMPLOYEES_URI, employeesApi)).thenReturn(mockEmployeesUpdate);

        when(mockEmployeesUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> employeesService.submitEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            employees,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("DELETE - Employees successful delete path")
    void deleteEmployees() throws Exception {

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.delete(EMPLOYEES_URI)).thenReturn(mockEmployeesDelete);

        employeesService.deleteEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(mockEmployeesDelete, times(1)).execute();
    }

    @Test
    @DisplayName("DELETE - Employees throws ServiceExcepiton due to URIValidationException")
    void deleteEmployeesUriValidationException() throws Exception {

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.delete(EMPLOYEES_URI)).thenReturn(mockEmployeesDelete);
        when(mockEmployeesDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> employeesService.deleteEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Employees throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void deleteEmployeesApiErrorResponseExceptionNotFound() throws Exception {

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.delete(EMPLOYEES_URI)).thenReturn(mockEmployeesDelete);

        when(mockEmployeesDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> employeesService.deleteEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private void getMockEmployeesResourceHandler() {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.employees()).thenReturn(mockEmployeesResourceHandler);
    }

    private void getMockEmployeesApi(EmployeesApi employeesApi) throws Exception {
        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(employeesApi);
    }

    private void employeesCreate(EmployeesApi employeesApi) throws Exception {
        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.create(EMPLOYEES_URI, employeesApi)).thenReturn(mockEmployeesCreate);
        when(mockEmployeesCreate.execute()).thenReturn(responseWithData);
    }

    private void employeesUpdate(EmployeesApi employeesApi) throws Exception {
        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.update(EMPLOYEES_URI, employeesApi)).thenReturn(mockEmployeesUpdate);
        when(mockEmployeesUpdate.execute()).thenReturn(responseNoData);
    }

    private Employees createEmployees() {
        Employees employees = new Employees();
        AverageNumberOfEmployees averageNumberOfEmployees = new AverageNumberOfEmployees();

        BalanceSheetHeadings balanceSheetHeadings = new BalanceSheetHeadings();

        averageNumberOfEmployees.setCurrentAverageNumberOfEmployees(1l);;
        averageNumberOfEmployees.setPreviousAverageNumberOfEmployees(2l);

        balanceSheetHeadings.setCurrentPeriodHeading("");
        balanceSheetHeadings.setPreviousPeriodHeading("");

        employees.setDetails(DETAILS);
        employees.setAverageNumberOfEmployees(averageNumberOfEmployees);
        employees.setBalanceSheetHeadings(balanceSheetHeadings);

        return employees;
    }

    private EmployeesApi createEmployeesApi() {
        EmployeesApi employeesApi = new EmployeesApi();
        CurrentPeriod currentPeriod = new CurrentPeriod();
        PreviousPeriod previousPeriod = new PreviousPeriod();

        currentPeriod.setAverageNumberOfEmployees(5L);
        currentPeriod.setDetails("DETAILS");

        previousPeriod.setAverageNumberOfEmployees(5L);

        employeesApi.setCurrentPeriod(currentPeriod);
        employeesApi.setPreviousPeriod(previousPeriod);

        return employeesApi;
    }

    private void setLinksWithEmployees(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setEmployeesNote("");

        smallFullApi.setLinks(links);
    }

    private void setLinksWithoutEmployees(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setDebtorsNote("");

        smallFullApi.setLinks(links);
    }
}
