package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
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

@Service
public class EmployeesServiceImpl implements EmployeesService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private EmployeesTransformer transformer;

    @Autowired
    private BalanceSheetService balanceSheetService;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate EMPLOYEES_URI =
            new UriTemplate(
                    "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small" +
                            "-full/notes/employees");

    private static final String INVALID_URI_MESSAGE =
            "Invalid URI for employees note resource";

    private static final String EMPLOYEES_DETAILS = "details";
    private static final String INVALID_STRING_SIZE_ERROR_MESSAGE = "validation.length.minInvalid" +
            ".accounting_policies.turnover_policy";

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
            Employees employees, String companyNumber, Model model) throws ServiceException {

        validateEmployees(employees, model);

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = EMPLOYEES_URI.expand(transactionId, companyAccountsId).toString();

        SmallFullApi smallFullApi = smallFullService.getSmallFullAccounts(apiClient,
                transactionId, companyAccountsId);

        EmployeesApi employeesApi = transformer.getEmployeesApi(employees);

        boolean employeesResourceExists = hasEmployees(smallFullApi.getLinks());

        try {
            if (! employeesResourceExists) {
                apiClient.smallFull().employees().create(uri, employeesApi).execute();
            } else {
                apiClient.smallFull().employees().update(uri, employeesApi).execute();
            }
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
                List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
                if (validationErrors.isEmpty()) {
                    throw new ServiceException("Bad request when creating employees resource", e);
                }
                return validationErrors;
            }
            throw new ServiceException("Error creating employees resource", e);
        }

        return new ArrayList<>();
    }

    private void validateEmployees(Employees employees, Model model) {

        if ((employees == null) || (employees != null &&
                employees.getAverageNumberOfEmployees()!=null &&
                employees.getAverageNumberOfEmployees().getCurrentAverageNumberOfEmployees() == null
                && employees.getAverageNumberOfEmployees().getPreviousAverageNumberOfEmployees() == null
                && StringUtils.isEmpty(employees.getDetails()))) {
            model.addAttribute("noteEmpty", "noteEmpty");
        }
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
            throw new ServiceException("Error when retrieving employees note", e);
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        }
    }

    @Override
    public void deleteEmployees(String transactionId, String companyAccountsId) throws ServiceException {
        ApiClient apiClient = apiClientService.getApiClient();

        String uri = EMPLOYEES_URI.expand(transactionId, companyAccountsId).toString();

        try {
            apiClient.smallFull().employees().delete(uri).execute();
        } catch (URIValidationException e) {
            throw new ServiceException(INVALID_URI_MESSAGE, e);
        } catch (ApiErrorResponseException e) {
            throw new ServiceException("Error deleting employees resource", e);
        }
    }

    private boolean hasEmployees(SmallFullLinks smallFullLinks) {
        return smallFullLinks.getEmployeesNote() != null;
    }
}
