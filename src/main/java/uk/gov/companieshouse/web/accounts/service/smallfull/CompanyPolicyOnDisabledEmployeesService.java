package uk.gov.companieshouse.web.accounts.service.smallfull;

import java.util.List;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployees;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

public interface CompanyPolicyOnDisabledEmployeesService {
    CompanyPolicyOnDisabledEmployees getCompanyPolicyOnDisabledEmployees(String transactionId, String companyAccountsId)
            throws ServiceException;

    List<ValidationError> submitCompanyPolicyOnDisabledEmployees(String transactionId, String companyAccountsId,
            CompanyPolicyOnDisabledEmployees companyPolicyOnDisabledEmployees) throws ServiceException;
}
