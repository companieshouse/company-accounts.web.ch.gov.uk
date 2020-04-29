package uk.gov.companieshouse.web.accounts.transformer.smallfull;

import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.service.smallfull.impl.NoteTransformer;

public interface EmployeesTransformer {

    Employees getEmployees(EmployeesApi employeesApi);

    EmployeesApi getEmployeesApi(Employees employees);
}
