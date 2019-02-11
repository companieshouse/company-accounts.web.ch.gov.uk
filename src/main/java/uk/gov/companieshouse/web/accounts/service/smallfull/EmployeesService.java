package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;

public interface EmployeesService {

    /**
     * Fetch employees note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param companyNumber The company identifier
     * @return employees note
     * @throws ServiceException if there's an error when retrieving the debtors note
     */
    Employees getEmployees(String transactionId, String companyAccountsId, String companyNumber)
        throws ServiceException;

}
