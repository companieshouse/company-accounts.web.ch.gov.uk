package uk.gov.companieshouse.web.accounts.transformer.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.CurrentPeriod;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.PreviousPeriod;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.AverageNumberOfEmployees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class EmployeesTransformerImpl implements NoteTransformer<Employees, EmployeesApi> {

    @Override
    public Employees toWeb(EmployeesApi employeesApi) {

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

    @Override
    public EmployeesApi toApi(Employees employees) {

        EmployeesApi employeesApi = new EmployeesApi();

        setCurrentPeriodEmployeesOnApiModel(employees, employeesApi);
        setPreviousPeriodEmployeesOnApiModel(employees, employeesApi);

        return employeesApi;
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_EMPLOYEES;
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

    private void setCurrentPeriodEmployeesOnApiModel(Employees employees, EmployeesApi employeesApi) {
        CurrentPeriod currentPeriod = new CurrentPeriod();

        if (StringUtils.isBlank(employees.getDetails())) {
            currentPeriod.setDetails(null);
        } else {
            currentPeriod.setDetails(employees.getDetails());
        }

        if (employees.getAverageNumberOfEmployees() != null && employees.getAverageNumberOfEmployees().getCurrentAverageNumberOfEmployees() != null) {
            currentPeriod.setAverageNumberOfEmployees(employees.getAverageNumberOfEmployees().getCurrentAverageNumberOfEmployees());
        }

        employeesApi.setCurrentPeriod(currentPeriod);
    }

    private void setPreviousPeriodEmployeesOnApiModel(Employees employees, EmployeesApi employeesApi) {

        if (hasPreviousPeriodEmployees(employees)) {
            PreviousPeriod previousPeriod = new PreviousPeriod();

            if (employees.getAverageNumberOfEmployees() != null && employees.getAverageNumberOfEmployees().getPreviousAverageNumberOfEmployees() != null) {
                previousPeriod.setAverageNumberOfEmployees(employees.getAverageNumberOfEmployees().getPreviousAverageNumberOfEmployees());
            }

            employeesApi.setPreviousPeriod(previousPeriod);
        }
    }

    private boolean hasPreviousPeriodEmployees(Employees employees) {

        return Stream.of(employees.getAverageNumberOfEmployees().getPreviousAverageNumberOfEmployees()
                ).anyMatch(Objects::nonNull);
    }
}
