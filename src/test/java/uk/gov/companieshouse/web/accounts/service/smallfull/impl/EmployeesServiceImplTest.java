package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;

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
import uk.gov.companieshouse.api.handler.smallfull.employees.request.EmployeesGet;
import uk.gov.companieshouse.api.handler.smallfull.request.SmallFullGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.AverageNumberOfEmployees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.EmployeesTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private SmallFullGet mockSmallFullGet;

    @Mock
    private EmployeesTransformer mockEmployeesTransformer;

    @Mock
    private ValidationContext mockValidationContext;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private BalanceSheetService mockBalanceSheetService;

    @Mock
    private BalanceSheet mockBalanceSheet;

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

    @Test
    @DisplayName("GET - Employees successful path")
    void getEmployeesSuccess() throws Exception {

        EmployeesApi employeesApi = new EmployeesApi();
        getMockEmployeesApi(employeesApi);

        when(mockEmployeesTransformer.getEmployees(employeesApi)).thenReturn(createEmployees());
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheet);
        when(mockBalanceSheet.getBalanceSheetHeadings()).thenReturn(new BalanceSheetHeadings());

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

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenThrow(apiErrorResponseException);
        when(mockBalanceSheetService.getBalanceSheet(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER)).thenReturn(mockBalanceSheet);

        when(mockEmployeesTransformer.getEmployees(null)).thenReturn(new Employees());

        Employees employees = employeesService.getEmployees(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(employees);
        assertNull(employees.getAverageNumberOfEmployees());
        assertNull(employees.getDetails());
    }

    @Test
    @DisplayName("GET - Employees throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void getEmployeesApiResponseException() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockEmployeesGet.execute());
        assertThrows(ServiceException.class, () -> employeesService.getEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Employees throws ServiceExcepiton due to URIValidationException")
    void getEmployeesURIValidationException() throws Exception {

        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockEmployeesGet.execute());
        assertThrows(ServiceException.class, () -> employeesService.getEmployees(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private void getMockEmployeesResourceHandler() throws Exception {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.employees()).thenReturn(mockEmployeesResourceHandler);
    }

    private void getMockEmployeesApi(EmployeesApi employeesApi) throws Exception {
        getMockEmployeesResourceHandler();
        when(mockEmployeesResourceHandler.get(EMPLOYEES_URI)).thenReturn(mockEmployeesGet);
        when(mockEmployeesGet.execute()).thenReturn(employeesApi);
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
}
