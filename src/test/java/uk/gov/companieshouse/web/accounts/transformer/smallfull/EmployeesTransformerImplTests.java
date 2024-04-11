package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.AverageNumberOfEmployees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.impl.EmployeesTransformerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeesTransformerImplTests {
    
    private static final Long AVERAGE_NUMBER_OF_EMPLOYEES_CURRENT = 1L;
    
    private static final Long AVERAGE_NUMBER_OF_EMPLOYEES_PREVIOUS = 10L;

    private static final String DETAILS = "DETAILS";
    
    private final NoteTransformer<Employees, EmployeesApi>  transformer = new EmployeesTransformerImpl();

    @Test
    @DisplayName("All Current period values added to employees web model")
    void getEmployeesForCurrentPeriod() {
        EmployeesApi employeesApi = new EmployeesApi();

        CurrentPeriod currentPeriod = new CurrentPeriod();
        currentPeriod.setAverageNumberOfEmployees(AVERAGE_NUMBER_OF_EMPLOYEES_CURRENT);
        currentPeriod.setDetails(DETAILS);

        employeesApi.setCurrentPeriod(currentPeriod);

        Employees employees = transformer.toWeb(employeesApi);

        assertEquals(AVERAGE_NUMBER_OF_EMPLOYEES_CURRENT, employees.getAverageNumberOfEmployees().getCurrentAverageNumberOfEmployees());
        assertEquals(DETAILS, employees.getDetails());
    }

    @Test
    @DisplayName("Only populated Current period values added to employees web model")
    void getEmployeesCurrentPeriodPopulatedValues() {
        EmployeesApi employeesApi = new EmployeesApi();

        CurrentPeriod currentPeriod = new CurrentPeriod();
        currentPeriod.setDetails(DETAILS);

        employeesApi.setCurrentPeriod(currentPeriod);

        Employees employees = transformer.toWeb(employeesApi);

        assertNotNull(employees.getAverageNumberOfEmployees());
        assertNull(employees.getAverageNumberOfEmployees().getCurrentAverageNumberOfEmployees());
        assertEquals(DETAILS, employees.getDetails());
    }

    @Test
    @DisplayName("Previous period values added to employees web model")
    void getEmployeesForPreviousPeriod() {
        EmployeesApi employeesApi = new EmployeesApi();

        PreviousPeriod previousPeriod = new PreviousPeriod();

        previousPeriod.setAverageNumberOfEmployees(AVERAGE_NUMBER_OF_EMPLOYEES_PREVIOUS);

        employeesApi.setPreviousPeriod(previousPeriod);

        Employees employees = transformer.toWeb(employeesApi);

        assertEquals(AVERAGE_NUMBER_OF_EMPLOYEES_PREVIOUS, employees.getAverageNumberOfEmployees().getPreviousAverageNumberOfEmployees());
    }
    
    @Test
    @DisplayName("When employees API model is null the returned web model is non null")
    void NonNullEmployeesWebModelWhenNullAPIModel() {
        Employees employees = transformer.toWeb(null);

        assertNotNull(employees);
    }

    @Test
    @DisplayName("Current period value added to employees API model when present")
    void currentPeriodValueAddedToEmployeesApiModel() {
        Employees employees = new Employees();

        AverageNumberOfEmployees averageNumberOfEmployees = new AverageNumberOfEmployees();
        averageNumberOfEmployees.setCurrentAverageNumberOfEmployees(AVERAGE_NUMBER_OF_EMPLOYEES_CURRENT);
        employees.setAverageNumberOfEmployees(averageNumberOfEmployees);

        EmployeesApi employeesApi = transformer.toApi(employees);

        assertNull(employeesApi.getCurrentPeriod().getDetails());
        uk.gov.companieshouse.api.model.accounts.smallfull.employees.CurrentPeriod currentPeriod = employeesApi.getCurrentPeriod();

        assertEquals(AVERAGE_NUMBER_OF_EMPLOYEES_CURRENT, currentPeriod.getAverageNumberOfEmployees());
    }

    @Test
    @DisplayName("Previous period value added to employees API model when present")
    void previousPeriodValueAddedToEmployeesApiModel() {
        Employees employees = new Employees();

        AverageNumberOfEmployees averageNumberOfEmployees = new AverageNumberOfEmployees();
        averageNumberOfEmployees.setPreviousAverageNumberOfEmployees(AVERAGE_NUMBER_OF_EMPLOYEES_PREVIOUS);
        employees.setAverageNumberOfEmployees(averageNumberOfEmployees);

        EmployeesApi employeesApi = transformer.toApi(employees);

        uk.gov.companieshouse.api.model.accounts.smallfull.employees.PreviousPeriod previousPeriod = employeesApi.getPreviousPeriod();

        assertEquals(AVERAGE_NUMBER_OF_EMPLOYEES_PREVIOUS, previousPeriod.getAverageNumberOfEmployees());
    }

    @Test
    @DisplayName("Current period details are null in employees API model if empty string passed in web model")
    void detailsNullWithEmployeesApiModel() {
        Employees employees = new Employees();
        employees.setDetails("");

        AverageNumberOfEmployees averageNumberOfEmployees = new AverageNumberOfEmployees();

        employees.setAverageNumberOfEmployees(averageNumberOfEmployees);

        EmployeesApi employeesApi = transformer.toApi(employees);

        assertNull(employeesApi.getCurrentPeriod().getDetails());
    }

    @Test
    @DisplayName("Test employees type returned")
    void testEmployeesReturned() {
        assertEquals(NoteType.SMALL_FULL_EMPLOYEES, transformer.getNoteType());
    }
}