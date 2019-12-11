package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.DateValidator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private DirectorTransformer directorTransformer;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private DateValidator dateValidator;

    private static final UriTemplate DIRECTORS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/directors");

    private static final UriTemplate DIRECTORS_URI_WITH_ID =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/directors-report/directors/{directorId}");

    private static final String RESOURCE_NAME = "directors";

    @Override
    public DirectorApi[] getAllDirectors(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DIRECTORS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            return apiClient.smallFull().directorsReport().director().getAll(uri).execute().getData();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return new DirectorApi[0];
    }

    @Override
    public List<ValidationError> createDirector(String transactionId, String companyAccountsId, DirectorToAdd directorToAdd) throws ServiceException {

        List<ValidationError> validationErrors = new ArrayList<>();

        if (directorToAdd.getWasDirectorAppointedDuringPeriod()) {
            validationErrors.addAll(
                    dateValidator.validateDate(
                            directorToAdd.getAppointmentDate(),
                            "appointmentDate",
                            ".director.appointment_date"));
        }

        if (directorToAdd.getDidDirectorResignDuringPeriod()) {
            validationErrors.addAll(
                    dateValidator.validateDate(
                            directorToAdd.getResignationDate(),
                            "resignationDate",
                            ".director.resignation_date"));
        }

        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DIRECTORS_URI.expand(transactionId, companyAccountsId).toString();

        DirectorApi directorApi = directorTransformer.getDirectorApi(directorToAdd);

        try {
            ApiResponse<DirectorApi> apiResponse = apiClient.smallFull().directorsReport().director().create(uri, directorApi).execute();

            if (apiResponse.hasErrors()) {
                validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return validationErrors;
    }

    @Override
    public void deleteDirector(String transactionId, String companyAccountsId, String directorId)
            throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = DIRECTORS_URI_WITH_ID.expand(transactionId, companyAccountsId, directorId).toString();

        try {
            apiClient.smallFull().directorsReport().director().delete(uri).execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }
    }
}
