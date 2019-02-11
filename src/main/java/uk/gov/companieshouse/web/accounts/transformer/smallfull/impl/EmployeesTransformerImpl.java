package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.AverageNumberOfEmployees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.EmployeesTransformer;

@Component
public class EmployeesTransformerImpl implements EmployeesTransformer {

    @Override
    public Employees getEmployees(EmployeesApi employeesApi) {

      Employees employees = new Employees();

      if (employeesApi == null) {
        return employees;
      }
      
      AverageNumberOfEmployees averageNumberOfEmployees = new AverageNumberOfEmployees();
      
      populateCurrentPeriodForWeb(employeesApi, employees, averageNumberOfEmployees);
      populatePreviousPeriodForWeb(employeesApi, averageNumberOfEmployees);

      employees.setAverageNumberOfEmployees(averageNumberOfEmployees);
      
      return employees;
    }
    
    private void populateCurrentPeriodForWeb(EmployeesApi employeesApi, Employees employees, AverageNumberOfEmployees averageNumberOfEmployees) {     
        CurrentPeriod currentPeriod = employeesApi.getCurrentPeriod();  
        if (currentPeriod != null) {
            employees.setDetails(currentPeriod.getDetails());
            averageNumberOfEmployees.setCurrentAverageNumberOfEmployees(currentPeriod.getAverageNumberOfEmployees());
        }
    }
    
    private void populatePreviousPeriodForWeb(EmployeesApi employeesApi, AverageNumberOfEmployees averageNumberOfEmployees) {     
        PreviousPeriod previousPeriod = employeesApi.getPreviousPeriod();  
        if (previousPeriod != null) {
            averageNumberOfEmployees.setPreviousAverageNumberOfEmployees(previousPeriod.getAverageNumberOfEmployees());
        }
    }    
}
