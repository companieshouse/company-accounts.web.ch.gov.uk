package uk.gov.companieshouse.web.accounts.validation.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;

public class EmployeesValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
      return Employees.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Employees employees = (Employees) target;

      if(!isEmployeesValid(employees)) {
          errors.reject("employeesNote.error.averageEmployeesText.missing", new Object[]{}, null);
      }
    }

    private boolean isEmployeesValid(Employees employees) {

        if ((employees == null) || (employees.getAverageNumberOfEmployees() != null &&
                employees.getAverageNumberOfEmployees().getCurrentAverageNumberOfEmployees() == null
                && employees.getAverageNumberOfEmployees().getPreviousAverageNumberOfEmployees() == null
                && StringUtils.isEmpty(employees.getDetails()))) {
            return false;
        } else {
            return true;
        }
    }
}