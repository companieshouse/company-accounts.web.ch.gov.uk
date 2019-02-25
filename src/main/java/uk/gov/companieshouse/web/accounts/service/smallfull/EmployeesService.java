package uk.gov.companieshouse.web.accounts.service.smallfull;

import org.springframework.ui.Model;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

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

    /**
     * Submit the employees note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @param employees employees note to submit
     * @param companyNumber The company number
     * @return A list of validation errors, or an empty array list if none are present
     * @throws ServiceException if there's an error on submission
     */
    List<ValidationError> submitEmployees(String transactionId, String companyAccountsId, Employees employees, String companyNumber, Model model)
        throws ServiceException;

    /**
     * Delete the employees note
     *
     * @param transactionId The id of the CHS transaction
     * @param companyAccountsId The company accounts identifier
     * @throws ServiceException if there's an error on deletion
     */
    void deleteEmployees(String transactionId, String companyAccountsId) throws ServiceException;
}
