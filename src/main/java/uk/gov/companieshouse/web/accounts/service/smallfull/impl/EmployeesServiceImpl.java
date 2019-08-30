package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheet;
import uk.gov.companieshouse.web.accounts.model.smallfull.BalanceSheetHeadings;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.employees.Employees;
import uk.gov.companieshouse.web.accounts.service.smallfull.BalanceSheetService;
import uk.gov.companieshouse.web.accounts.service.smallfull.EmployeesService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.EmployeesTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private EmployeesTransformer transformer;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate EMPLOYEES_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small" +
                            "-full/notes/employees");

    private static final String RESOURCE_NAME = "employees";

    @Override
    public Employees getEmployees(String transactionId, String companyAccountsId,
            String companyNumber) throws ServiceException {

        EmployeesApi employeesApi = getEmployeesApi(transactionId, companyAccountsId);
        Employees employees = transformer.getEmployees(employeesApi);

        BalanceSheet balanceSheet =
                balanceSheetService.getBalanceSheet(transactionId, companyAccountsId,
                        companyNumber);
        BalanceSheetHeadings balanceSheetHeadings = balanceSheet.getBalanceSheetHeadings();
        employees.setBalanceSheetHeadings(balanceSheetHeadings);

        return employees;
    }

    @Override
    public List<ValidationError> submitEmployees(String transactionId, String companyAccountsId,
            Employees employees, String companyNumber) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = EMPLOYEES_URI.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient,
                transactionId, companyAccountsId);

        EmployeesApi employeesApi = transformer.getEmployeesApi(employees);

        boolean employeesResourceExists = hasEmployees(smallFullApi.getLinks());

        try {
            ApiResponse apiResponse;
            if (! employeesResourceExists) {
                apiResponse = apiClient.smallFull().employees().create(uri, employeesApi).execute();
            } else {
                apiResponse = apiClient.smallFull().employees().update(uri, employeesApi).execute();
            }

            if (apiResponse.hasErrors()) {
                return validationContext.getValidationErrors(apiResponse.getErrors());
            }
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        }

        return new ArrayList<>();
    }

    private EmployeesApi getEmployeesApi(String transactionId,
            String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = EMPLOYEES_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().employees().get(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return null;
    }

    @Override
    public void deleteEmployees(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = EMPLOYEES_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().employees().delete(uri).execute();
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        }
    }

    private boolean hasEmployees(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getEmployeesNote() != null;
    }
}
