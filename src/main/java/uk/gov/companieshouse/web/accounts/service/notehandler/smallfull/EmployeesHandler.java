package uk.gov.companieshouse.web.accounts.service.notehandler.smallfull;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.employees.EmployeesApi;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;

@Component
public class EmployeesHandler implements NoteResourceHandler<EmployeesApi> {

    @Autowired
    private SmallFullService smallFullService;

    private static final UriTemplate EMPLOYEES_URI = new UriTemplate(
            "/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/employees"
    );

    @Override
    public String getUri(String transactionId, String companyAccountsId) {
        return EMPLOYEES_URI.expand(transactionId, companyAccountsId).toString();
    }

    @Override
    public Executor<ApiResponse<EmployeesApi>> get(ApiClient apiClient, String uri) {
        return apiClient.smallFull().employees().get(uri);
    }

    @Override
    public Executor<ApiResponse<Void>> update(ApiClient apiClient, String uri, EmployeesApi employeesApi) {
        return apiClient.smallFull().employees().update(uri, employeesApi);
    }

    @Override
    public Executor<ApiResponse<EmployeesApi>> create(ApiClient apiClient, String uri, EmployeesApi employeesApi) {
        return apiClient.smallFull().employees().create(uri, employeesApi);
    }

    @Override
    public Executor<ApiResponse<Void>> delete(ApiClient apiClient, String uri) {
        return apiClient.smallFull().employees().delete(uri);
    }

    @Override
    public boolean parentResourceExists(ApiClient apiClient, String transactionId, String companyAccountsId) throws ServiceException {
        return StringUtils.isNotBlank(
                smallFullService.getSmallFullAccounts(apiClient, transactionId, companyAccountsId)
                .getLinks().getEmployeesNote());
    }

    @Override
    public NoteType getNoteType() {
        return NoteType.SMALL_FULL_EMPLOYEES;
    }
}
