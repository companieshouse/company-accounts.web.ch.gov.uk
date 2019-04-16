package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.AverageNumberOfEmployees;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;

public class EmployeesValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
      return Employees.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employees employees = (Employees) target;

      if (!isEmployeesValid(employees) && !errors.hasFieldErrors()) {

          errors.reject("validation.missing.employees", new Object[]{}, null);
      }
    }

    private boolean isEmployeesValid(Employees employees) {

        return !(employees == null || (isAverageNumberOfEmployeesNull(employees) &&
            StringUtils.isEmpty(employees.getDetails())));
    }
    
    private boolean isAverageNumberOfEmployeesNull(Employees employees) {
        AverageNumberOfEmployees average = employees.getAverageNumberOfEmployees();

        return  average == null || (average.getCurrentAverageNumberOfEmployees() == null &&
            average.getPreviousAverageNumberOfEmployees() == null);
    }
}