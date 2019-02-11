package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.EmployeesTransformer;

@Service
public class EmployeesServiceImpl implements EmployeesService {

  @Autowired
  private ApiClientService apiClientService;

  @Autowired
  private EmployeesTransformer transformer;
  
  @Autowired
  private BalanceSheetService balanceSheetService;

  private static final UriTemplate EMPLOYEES_URI =
      new UriTemplate(
          "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/employees");

  private static final String INVALID_URI_MESSAGE =
      "Invalid URI for employees note resource";

  @Override
  public Employees getEmployees(String transactionId,
      String companyAccountsId, String companyNumber) throws ServiceException {
      
      EmployeesApi employeesApi = getEmployeesApi(transactionId, companyAccountsId);
      Employees employees = transformer.getEmployees(employeesApi);
      
      BalanceSheet balanceSheet =
              balanceSheetService.getBalanceSheet(transactionId, companyAccountsId, companyNumber);
          BalanceSheetHeadings balanceSheetHeadings = balanceSheet.getBalanceSheetHeadings();
          employees.setBalanceSheetHeadings(balanceSheetHeadings);

    return employees;
  }
  
  private EmployeesApi getEmployeesApi(String transactionId,
          String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = EMPLOYEES_URI.expand(transactionId, companyAccountsId).toString();

        try {
          return apiClient.smallFull().employees().get(uri).execute();

        } catch (ApiErrorResponseException e) {
          if (e.getStatusCode() == HttpStatus.NOT_FOUND.value()) {
            return null;
          }
          throw new ServiceException("Error when retrieving cemployees note", e);
        } catch (URIValidationException e) {
          throw new ServiceException(INVALID_URI_MESSAGE, e);
        }
      }
}
