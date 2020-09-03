package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.DirectorsReportResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.director.DirectorsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.director.request.DirectorCreate;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.director.request.DirectorDelete;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.director.request.DirectorGetAll;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.model.directorsreport.Director;
import uk.gov.companieshouse.web.accounts.model.directorsreport.DirectorToAdd;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.DirectorTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.DateValidator;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;
import uk.gov.companieshouse.web.accounts.validation.smallfull.DirectorValidator;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectorServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private DirectorToAdd directorToAdd;

    @Mock
    private AddOrRemoveDirectors addOrRemoveDirectors;

    @Mock
    private DirectorApi directorApi;

    @Mock
    private DirectorTransformer directorTransformer;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private DirectorsReportResourceHandler directorsReportResourceHandler;

    @Mock
    private DirectorsResourceHandler directorResourceHandler;

    @Mock
    private DirectorGetAll directorGetAll;

    @Mock
    private DirectorCreate directorCreate;

    @Mock
    private DirectorDelete directorDelete;
    
    @Mock
    private ValidationContext validationContext;
    
    @Mock
    private DateValidator dateValidator;

    @Mock
    private DirectorValidator directorValidator;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ApiResponse<DirectorApi> responseWithSingleDirector;

    @Mock
    private ApiResponse<DirectorApi[]> responseWithMultipleDirectors;
    
    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private DirectorServiceImpl directorService;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DIRECTOR_ID = "directorId";

    private static final String DIRECTORS_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                        COMPANY_ACCOUNTS_ID + "/small-full/directors-report/directors";

    private static final String DIRECTORS_URI_WITH_ID = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                        COMPANY_ACCOUNTS_ID + "/small-full/directors-report/directors/" + DIRECTOR_ID;

    private static final String RESOURCE_NAME = "directors";

    private static final LocalDate APPOINTMENT_DATE = LocalDate.of(2019, 1, 1);
    private static final LocalDate RESIGNATION_DATE = LocalDate.of(2019, 2, 1);

    @Test
    @DisplayName("GET - all directors - success")
    void getAllDirectorsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.getAll(DIRECTORS_URI)).thenReturn(directorGetAll);
        when(directorGetAll.execute()).thenReturn(responseWithMultipleDirectors);
        DirectorApi[] directors = new DirectorApi[1];
        when(responseWithMultipleDirectors.getData()).thenReturn(directors);
        Director[] allDirectors = new Director[1];
        when(directorTransformer.getAllDirectors(directors)).thenReturn(allDirectors);

        Director[] response = directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false);

        assertEquals(allDirectors, response);
    }

    @Test
    @DisplayName("GET - all directors - success with isActive flag true")
    void getAllDirectorsSuccessIsActiveTrue()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.getAll(DIRECTORS_URI)).thenReturn(directorGetAll);
        when(directorGetAll.execute()).thenReturn(responseWithMultipleDirectors);
        DirectorApi[] allDirectors = createArrayOfDirectorsApi();
        when(responseWithMultipleDirectors.getData()).thenReturn(allDirectors);
        Director[] responseDirectors = createArrayOfDirectors();
        when(directorTransformer.getAllDirectors(any())).thenReturn(responseDirectors);

        Director[] response = directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, true);

        assertEquals(responseDirectors, response);
    }

    @Test
    @DisplayName("GET - all directors - not found")
    void getAllDirectorsNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.getAll(DIRECTORS_URI)).thenReturn(directorGetAll);
        when(directorGetAll.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        Director[] response = directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false);

        assertNotNull(response);
        assertEquals(0, response.length);
    }

    @Test
    @DisplayName("GET - all directors - ApiErrorResponseException")
    void getAllDirectorsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.getAll(DIRECTORS_URI)).thenReturn(directorGetAll);
        when(directorGetAll.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false));
    }

    @Test
    @DisplayName("GET - all directors - URIValidationException")
    void getAllDirectorsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.getAll(DIRECTORS_URI)).thenReturn(directorGetAll);
        when(directorGetAll.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorService.getAllDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, false));
    }

    @Test
    @DisplayName("POST - director - success")
    void createDirectorSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(directorValidator.validateDirectorToAdd(directorToAdd)).thenReturn(new ArrayList<>());

        when(directorToAdd.getWasDirectorAppointedDuringPeriod()).thenReturn(true);
        when(dateValidator.validateDate(directorToAdd.getAppointmentDate(), "directorToAdd.appointmentDate", ".director.appointment_date"))
                        .thenReturn(new ArrayList<>());

        when(directorToAdd.getDidDirectorResignDuringPeriod()).thenReturn(false);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(directorTransformer.getDirectorApi(directorToAdd)).thenReturn(directorApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.create(DIRECTORS_URI, directorApi)).thenReturn(directorCreate);
        when(directorCreate.execute()).thenReturn(responseWithSingleDirector);
        when(responseWithSingleDirector.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors = directorService.createDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorToAdd);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - director - invalid date")
    void createDirectorInvalidDate() throws ServiceException {

        when(directorValidator.validateDirectorToAdd(directorToAdd)).thenReturn(new ArrayList<>());

        when(directorToAdd.getWasDirectorAppointedDuringPeriod()).thenReturn(false);
        when(directorToAdd.getDidDirectorResignDuringPeriod()).thenReturn(true);

        ValidationError validationError = new ValidationError();
        List<ValidationError> dateValidationErrors = new ArrayList<>();
        dateValidationErrors.add(validationError);
        when(dateValidator.validateDate(directorToAdd.getAppointmentDate(), "directorToAdd.resignationDate", ".director.resignation_date"))
                .thenReturn(dateValidationErrors);

        List<ValidationError> validationErrors = directorService.createDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorToAdd);

        assertEquals(dateValidationErrors, validationErrors);

        verify(apiClientService, never()).getApiClient();
    }

    @Test
    @DisplayName("POST - director - validation errors")
    void createDirectorValidation()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(directorValidator.validateDirectorToAdd(directorToAdd)).thenReturn(new ArrayList<>());

        when(directorToAdd.getWasDirectorAppointedDuringPeriod()).thenReturn(false);

        when(directorToAdd.getDidDirectorResignDuringPeriod()).thenReturn(false);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(directorTransformer.getDirectorApi(directorToAdd)).thenReturn(directorApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.create(DIRECTORS_URI, directorApi)).thenReturn(directorCreate);
        when(directorCreate.execute()).thenReturn(responseWithSingleDirector);
        when(responseWithSingleDirector.hasErrors()).thenReturn(true);

        ValidationError validationError = new ValidationError();
        List<ValidationError> apiValidationErrors = new ArrayList<>();
        apiValidationErrors.add(validationError);
        when(validationContext.getValidationErrors(responseWithSingleDirector.getErrors())).thenReturn(apiValidationErrors);

        List<ValidationError> validationErrors = directorService.createDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorToAdd);

        assertEquals(apiValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("POST - director - ApiErrorResponseException")
    void createDirectorApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(directorValidator.validateDirectorToAdd(directorToAdd)).thenReturn(new ArrayList<>());

        when(directorToAdd.getWasDirectorAppointedDuringPeriod()).thenReturn(false);
        when(directorToAdd.getDidDirectorResignDuringPeriod()).thenReturn(false);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(directorTransformer.getDirectorApi(directorToAdd)).thenReturn(directorApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.create(DIRECTORS_URI, directorApi)).thenReturn(directorCreate);
        when(directorCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorService.createDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorToAdd));
    }

    @Test
    @DisplayName("POST - director - URIValidationException")
    void createDirectorURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(directorValidator.validateDirectorToAdd(directorToAdd)).thenReturn(new ArrayList<>());

        when(directorToAdd.getWasDirectorAppointedDuringPeriod()).thenReturn(false);
        when(directorToAdd.getDidDirectorResignDuringPeriod()).thenReturn(false);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(directorTransformer.getDirectorApi(directorToAdd)).thenReturn(directorApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.create(DIRECTORS_URI, directorApi)).thenReturn(directorCreate);
        when(directorCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorService.createDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, directorToAdd));
    }

    @Test
    @DisplayName("DELETE - director - success")
    void deleteDirectorSuccess()
            throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.delete(DIRECTORS_URI_WITH_ID)).thenReturn(directorDelete);

        assertAll(() -> directorService.deleteDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, DIRECTOR_ID));

        verify(directorDelete).execute();
    }

    @Test
    @DisplayName("DELETE - director - ApiErrorResponseException")
    void deleteDirectorApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.delete(DIRECTORS_URI_WITH_ID)).thenReturn(directorDelete);
        when(directorDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorService.deleteDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, DIRECTOR_ID));
    }

    @Test
    @DisplayName("DELETE - director - URIValidationException")
    void deleteDirectorURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.directors()).thenReturn(directorResourceHandler);
        when(directorResourceHandler.delete(DIRECTORS_URI_WITH_ID)).thenReturn(directorDelete);
        when(directorDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorService.deleteDirector(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, DIRECTOR_ID));
    }

    @Test
    @DisplayName("Submit add or remove directors")
    void submitAddOrRemoveDirectors() throws ServiceException {

        List<ValidationError> validationErrors = new ArrayList<>();
        when(directorValidator.validateSubmitAddOrRemoveDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors)).thenReturn(validationErrors);

        assertEquals(validationErrors, directorService.submitAddOrRemoveDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors));
    }

    private DirectorApi[] createArrayOfDirectorsApi() {

        DirectorApi[] allDirectorsApi = new DirectorApi[4];

        DirectorApi activeDirector = new DirectorApi();
        activeDirector.setName("active");
        activeDirector.setAppointmentDate(null);
        activeDirector.setResignationDate(null);

        DirectorApi activeDirector2 = new DirectorApi();
        activeDirector2.setName("active2");
        activeDirector2.setAppointmentDate(APPOINTMENT_DATE);
        activeDirector2.setResignationDate(null);

        DirectorApi activeDirector3 = new DirectorApi();
        activeDirector3.setName("active3");
        activeDirector3.setAppointmentDate(RESIGNATION_DATE.plusDays(1));
        activeDirector3.setResignationDate(RESIGNATION_DATE);

        DirectorApi inactiveDirector = new DirectorApi();
        inactiveDirector.setName("inactive");
        inactiveDirector.setAppointmentDate(APPOINTMENT_DATE);
        inactiveDirector.setResignationDate(RESIGNATION_DATE);

        allDirectorsApi[0] = activeDirector;
        allDirectorsApi[1] = activeDirector2;
        allDirectorsApi[2] = activeDirector3;
        allDirectorsApi[3] = inactiveDirector;

        return allDirectorsApi;
    }

    private Director[] createArrayOfDirectors() {

        Director[] allDirectors = new Director[3];

        Director activeDirector = new Director();
        activeDirector.setName("active");
        activeDirector.setAppointmentDate(null);
        activeDirector.setResignationDate(null);

        Director activeDirector2 = new Director();
        activeDirector2.setName("active2");
        activeDirector2.setAppointmentDate(APPOINTMENT_DATE);
        activeDirector2.setResignationDate(null);

        Director activeDirector3 = new Director();
        activeDirector3.setName("active3");
        activeDirector3.setAppointmentDate(RESIGNATION_DATE.plusDays(1));
        activeDirector3.setResignationDate(RESIGNATION_DATE);

        allDirectors[0] = activeDirector;
        allDirectors[1] = activeDirector2;
        allDirectors[2] = activeDirector3;

        return allDirectors;
    }
}
