package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.statements.StatementsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.statements.request.StatementsCreate;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.statements.request.StatementsDelete;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.statements.request.StatementsGet;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.statements.request.StatementsUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.StatementsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportStatementsService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DirectorsReportStatementsServiceImplTests {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private DirectorsReportResourceHandler directorsReportResourceHandler;

    @Mock
    private StatementsResourceHandler statementsResourceHandler;

    @Mock
    private StatementsCreate statementsCreate;

    @Mock
    private StatementsUpdate statementsUpdate;

    @Mock
    private StatementsGet statementsGet;

    @Mock
    private StatementsDelete statementsDelete;

    @Mock
    private ApiResponse<StatementsApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private StatementsApi statementsApi;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private DirectorsReportStatementsService directorsReportStatementsService = new DirectorsReportStatementsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DIRECTORS_REPORT_STATEMENTS_URI =
            "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                    COMPANY_ACCOUNTS_ID + "/small-full/directors-report/statements";

    private static final String RESOURCE_NAME = "directors report statements";

    @BeforeEach
    void setUp() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.statements()).thenReturn(statementsResourceHandler);
    }

    @Test
    @DisplayName("Get directors report statements - success")
    void getDirectorsReportStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler.get(DIRECTORS_REPORT_STATEMENTS_URI)).thenReturn(
                statementsGet);

        when(statementsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(statementsApi);

        StatementsApi returnedDirectorsReportStatements =
                directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID);

        assertEquals(statementsApi, returnedDirectorsReportStatements);
    }

    @Test
    @DisplayName("Get directors report statements - throws ApiErrorResponseException")
    void getDirectorsReportStatementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler.get(DIRECTORS_REPORT_STATEMENTS_URI)).thenReturn(
                statementsGet);

        when(statementsGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get directors report statements - resource not found")
    void getDirectorsReportStatementsResourceNotFound()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler.get(DIRECTORS_REPORT_STATEMENTS_URI)).thenReturn(
                statementsGet);

        when(statementsGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertNull(directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get directors report statements - throws URIValidationException")
    void getDirectorsReportStatementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler.get(DIRECTORS_REPORT_STATEMENTS_URI)).thenReturn(
                statementsGet);

        when(statementsGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.getDirectorsReportStatements(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create directors report statements - success")
    void createDirectorsReportStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .create(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsCreate);

        when(statementsCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                directorsReportStatementsService
                        .createDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                statementsApi);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Create directors report statements - throws ApiErrorResponseException")
    void createDirectorsReportStatementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .create(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsCreate);

        when(statementsCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.createDirectorsReportStatements(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi));
    }

    @Test
    @DisplayName("Create directors report statements - validation errors")
    void createDirectorsReportStatementsValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .create(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsCreate);

        when(statementsCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                directorsReportStatementsService
                        .createDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                statementsApi);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Create directors report statements - throws URIValidationException")
    void createDirectorsReportStatementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .create(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsCreate);

        when(statementsCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.createDirectorsReportStatements(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi));
    }

    @Test
    @DisplayName("Update directors report statements - success")
    void updateDirectorsReportStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .update(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsUpdate);

        when(statementsUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                directorsReportStatementsService
                        .updateDirectorsReportStatements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                statementsApi);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Update directors report statements - throws ApiErrorResponseException")
    void updateDirectorsReportStatementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .update(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsUpdate);

        when(statementsUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.updateDirectorsReportStatements(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi));
    }

    @Test
    @DisplayName("Update directors report statements - validation errors")
    void updateDirectorsReportStatementsValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .update(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsUpdate);

        when(statementsUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                directorsReportStatementsService.updateDirectorsReportStatements(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Update directors report statements - throws URIValidationException")
    void updateDirectorsReportStatementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler
                .update(DIRECTORS_REPORT_STATEMENTS_URI, statementsApi))
                .thenReturn(statementsUpdate);

        when(statementsUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.updateDirectorsReportStatements(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, statementsApi));
    }

    @Test
    @DisplayName("Delete directors report statements - success")
    void deleteDirectorsReportStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException {

        when(statementsResourceHandler.delete(DIRECTORS_REPORT_STATEMENTS_URI))
                .thenReturn(statementsDelete);

        assertAll(() ->
                directorsReportStatementsService.deleteDirectorsReportStatements(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));

        verify(statementsDelete).execute();
    }

    @Test
    @DisplayName("Delete directors report statements - throws ApiErrorResponseException")
    void deleteDirectorsReportStatementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler.delete(DIRECTORS_REPORT_STATEMENTS_URI))
                .thenReturn(statementsDelete);

        when(statementsDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.deleteDirectorsReportStatements(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Delete directors report statements - throws URIValidationException")
    void deleteDirectorsReportStatementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(statementsResourceHandler.delete(DIRECTORS_REPORT_STATEMENTS_URI))
                .thenReturn(statementsDelete);

        when(statementsDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportStatementsService.deleteDirectorsReportStatements(TRANSACTION_ID,
                        COMPANY_ACCOUNTS_ID));
    }
}
