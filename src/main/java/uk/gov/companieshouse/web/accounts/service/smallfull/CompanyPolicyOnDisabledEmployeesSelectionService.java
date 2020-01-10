package uk.gov.companieshouse.web.accounts.service.smallfull;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.CompanyPolicyOnDisabledEmployeesSelection;

public interface CompanyPolicyOnDisabledEmployeesSelectionService {

    CompanyPolicyOnDisabledEmployeesSelection getCompanyPolicyOnDisabledEmployeesSelection(String transactionId,
            String companyAccountsId) throws ServiceException;

    void submitCompanyPolicyOnDisabledEmployees(String transactionId, String companyAccountsId,
            CompanyPolicyOnDisabledEmployeesSelection companyPolicyOnDisabledEmployeesSelection) throws ServiceException;
}
