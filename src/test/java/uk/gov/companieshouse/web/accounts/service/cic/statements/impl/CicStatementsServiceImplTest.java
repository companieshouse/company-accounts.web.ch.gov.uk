package uk.gov.companieshouse.web.accounts.service.cic.statements.impl;

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
import uk.gov.companieshouse.api.handler.cic.CicReportResourceHandler;
import uk.gov.companieshouse.api.handler.cic.statements.CicStatementsResourceHandler;
import uk.gov.companieshouse.api.handler.cic.statements.request.CicStatementsCreate;
import uk.gov.companieshouse.api.handler.cic.statements.request.CicStatementsGet;
import uk.gov.companieshouse.api.handler.cic.statements.request.CicStatementsUpdate;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.cic.statements.CicStatementsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.cic.statements.CicStatementsService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CicStatementsServiceImplTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private CicReportResourceHandler cicReportResourceHandler;

    @Mock
    private CicStatementsResourceHandler cicStatementsResourceHandler;

    @Mock
    private CicStatementsCreate cicStatementsCreate;

    @Mock
    private CicStatementsUpdate cicStatementsUpdate;

    @Mock
    private CicStatementsGet cicStatementsGet;

    @Mock
    private ApiResponse<CicStatementsApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private CicStatementsApi cicStatementsApi;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private CicStatementsService statementsService = new CicStatementsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String CIC_STATEMENTS_URI = "/transactions/" + TRANSACTION_ID +
            "/company-accounts/" + COMPANY_ACCOUNTS_ID +
            "/cic-report/cic-statements";

    private static final String RESOURCE_NAME = "CIC statements";

    @BeforeEach
    private void setUp() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.cicReport()).thenReturn(cicReportResourceHandler);
        when(cicReportResourceHandler.statements()).thenReturn(cicStatementsResourceHandler);
    }

    @Test
    @DisplayName("Get CIC statements - success")
    void getCicStatementsSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(cicStatementsResourceHandler.get(CIC_STATEMENTS_URI)).thenReturn(cicStatementsGet);

        when(cicStatementsGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(cicStatementsApi);

        CicStatementsApi returnedCicStatements =
                statementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(cicStatementsApi, returnedCicStatements);
    }

    @Test
    @DisplayName("Get CIC statements - not found")
    void getCicStatementsNotFound()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(cicStatementsResourceHandler.get(CIC_STATEMENTS_URI)).thenReturn(cicStatementsGet);

        when(cicStatementsGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertNull(statementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get CIC statements - URIValidationException")
    void getCicStatementsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(cicStatementsResourceHandler.get(CIC_STATEMENTS_URI)).thenReturn(cicStatementsGet);

        when(cicStatementsGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> statementsService.getCicStatementsApi(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create CIC statements - success")
    void createCicStatementsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.create(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsCreate);

        when(cicStatementsCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> returnedValidationErrors =
                statementsService.createCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);

        assertTrue(returnedValidationErrors.isEmpty());

        verify(cicStatementsCreate).execute();
    }

    @Test
    @DisplayName("Create CIC statements - validation errors")
    void createCicStatementsValidationErrors()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.create(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsCreate);

        when(cicStatementsCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(validationErrors);

        List<ValidationError> returnedValidationErrors =
                statementsService.createCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);

        assertEquals(validationErrors, returnedValidationErrors);
    }

    @Test
    @DisplayName("Create CIC statements - URIValidationException")
    void createCicStatementsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.create(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsCreate);

        when(cicStatementsCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                statementsService.createCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi));
    }

    @Test
    @DisplayName("Create CIC statements - ApiErrorResponseException")
    void createCicStatementsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.create(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsCreate);

        when(cicStatementsCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                statementsService.createCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi));
    }

    @Test
    @DisplayName("Update CIC statements - success")
    void updateCicStatementsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.update(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsUpdate);

        when(cicStatementsUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> returnedValidationErrors =
                statementsService.updateCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);

        assertTrue(returnedValidationErrors.isEmpty());
    }

    @Test
    @DisplayName("Update CIC statements - validation errors")
    void updateCicStatementsValidationErrors()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.update(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsUpdate);

        when(cicStatementsUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(validationErrors);

        List<ValidationError> returnedValidationErrors =
                statementsService.updateCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi);

        assertEquals(validationErrors, returnedValidationErrors);
    }

    @Test
    @DisplayName("Update CIC statements - URIValidationException")
    void updateCicStatementsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.update(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsUpdate);

        when(cicStatementsUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                statementsService.updateCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi));
    }

    @Test
    @DisplayName("Update CIC statements - ApiErrorResponseException")
    void updateCicStatementsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(cicStatementsResourceHandler.update(CIC_STATEMENTS_URI, cicStatementsApi))
                .thenReturn(cicStatementsUpdate);

        when(cicStatementsUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                statementsService.updateCicStatementsApi(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, cicStatementsApi));
    }
}
