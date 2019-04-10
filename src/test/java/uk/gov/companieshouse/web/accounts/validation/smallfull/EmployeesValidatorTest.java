package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.AverageNumberOfEmployees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeesValidatorTest {

    private EmployeesValidator employeesValidator;
    
    private static final String MESSAGE_KEY = "validation.missing.employees";

    @BeforeEach
    private void setup() {
        employeesValidator = new EmployeesValidator();
    }
    
    @Test
    @DisplayName("Validate employees successfully with details non-blank")
    void validateWithDetailsPresent() throws Exception {
        Employees employees = new Employees();
        employees.setDetails("test");
        
        Errors errors = new DataBinder(employees).getBindingResult();
        
        employeesValidator.validate(employees, errors);
        
        assertFalse(errors.hasErrors());
    }
    
    @Test
    @DisplayName("Validate employees successfully with currentAverageNumberOfEmployees value")
    void validateWithCurrentAverageNumberOfEmployeesPresent() throws Exception {
        Employees employees = new Employees();
        AverageNumberOfEmployees averageNumberOfEmployees = new AverageNumberOfEmployees();
        averageNumberOfEmployees.setCurrentAverageNumberOfEmployees(0L);
        employees.setAverageNumberOfEmployees(averageNumberOfEmployees);
        
        Errors errors = new DataBinder(employees).getBindingResult();
        
        employeesValidator.validate(employees, errors);
        
        assertFalse(errors.hasErrors());
    }
    
    @Test
    @DisplayName("Validate employees successfully with previousAverageNumberOfEmployees value")
    void validateWithPreviousAverageNumberOfEmployeesPresent() throws Exception {
        Employees employees = new Employees();
        AverageNumberOfEmployees averageNumberOfEmployees = new AverageNumberOfEmployees();
        averageNumberOfEmployees.setPreviousAverageNumberOfEmployees(0L);
        employees.setAverageNumberOfEmployees(averageNumberOfEmployees);
        
        Errors errors = new DataBinder(employees).getBindingResult();
        
        employeesValidator.validate(employees, errors);
        
        assertFalse(errors.hasErrors());
    }
    
    @Test
    @DisplayName("Fail employees validation with null employees")
    void failValidationWithNullEmployees() throws Exception {
        Errors errors = new DataBinder(null).getBindingResult();
        
        employeesValidator.validate(null, errors);
        
        assertTrue(errors.hasErrors());
        assertEquals(errors.getErrorCount(), 1);
        assertEquals(errors.getAllErrors().get(0).getCode(), MESSAGE_KEY);
    }
    
    @Test
    @DisplayName("Fail employees validation with null employees values")
    void failValidationWithNullEmployeesValues() throws Exception {
        Employees employees = new Employees();
        
        Errors errors = new DataBinder(employees).getBindingResult();
        
        employeesValidator.validate(employees, errors);
        
        assertTrue(errors.hasErrors());
        assertEquals(errors.getErrorCount(), 1);
        assertEquals(errors.getAllErrors().get(0).getCode(), MESSAGE_KEY);
    }

    @Test
    @DisplayName("Fail employees validation with empty details")
    void failValidationWithEmptyDetails() throws Exception {
        Employees employees = new Employees();
        employees.setDetails("");
        
        Errors errors = new DataBinder(employees).getBindingResult();
        
        employeesValidator.validate(employees, errors);
        
        assertTrue(errors.hasErrors());
        assertEquals(errors.getErrorCount(), 1);
        assertEquals(errors.getAllErrors().get(0).getCode(), MESSAGE_KEY);
    }
    
    @Test
    @DisplayName("Check validator supports Employees class")
    void supportsEmployeesClass() throws Exception {
        assertTrue(employeesValidator.supports(Employees.class));
    }
   
}
