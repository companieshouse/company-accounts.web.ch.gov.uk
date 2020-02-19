package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.request.DirectorsReportCreate;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.request.DirectorsReportDelete;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.request.DirectorsReportGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DirectorsReportServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private DirectorsReportApi directorsReportApi;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private DirectorsReportResourceHandler directorsReportResourceHandler;

    @Mock
    private DirectorsReportGet directorsReportGet;

    @Mock
    private DirectorsReportCreate directorsReportCreate;

    @Mock
    private DirectorsReportDelete directorsReportDelete;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ApiResponse<DirectorsReportApi> responseWithData;
    
    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private DirectorsReportService directorsReportService = new DirectorsReportServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String DIRECTORS_REPORT_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                        COMPANY_ACCOUNTS_ID + "/small-full/directors-report";

    private static final String SMALL_FULL_DIRECTORS_LINK = "smallFullDirectorsLink";

    private static final String RESOURCE_NAME = "directors report";

    @Test
    @DisplayName("GET - directors report - success")
    void getDirectorsReportSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(directorsReportApi);

        DirectorsReportApi response = directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(directorsReportApi, response);
    }

    @Test
    @DisplayName("GET - directors report - not found")
    void getDirectorsReportNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        DirectorsReportApi response = directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNull(response);
    }

    @Test
    @DisplayName("GET - directors report - ApiErrorResponseException")
    void getDirectorsReportApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("GET - directors report - URIValidationException")
    void getDirectorsReportURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - directors report - success")
    void createDirectorsReportSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);
        when(directorsReportResourceHandler.create(eq(DIRECTORS_REPORT_URI), any(DirectorsReportApi.class))).thenReturn(directorsReportCreate);

        assertAll(() -> directorsReportService.createDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(directorsReportCreate).execute();
    }

    @Test
    @DisplayName("POST - directors report - already exists")
    void createDirectorsReportAlreadyExists()
            throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(directorsReportApi);

        assertAll(() -> directorsReportService.createDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(directorsReportResourceHandler, never()).create(eq(DIRECTORS_REPORT_URI), any(DirectorsReportApi.class));
    }

    @Test
    @DisplayName("POST - directors report - ApiErrorResponseException")
    void createDirectorsReportApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);
        when(directorsReportResourceHandler.create(eq(DIRECTORS_REPORT_URI), any(DirectorsReportApi.class))).thenReturn(directorsReportCreate);
        when(directorsReportCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorsReportService.createDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - directors report - URIValidationException")
    void createDirectorsReportURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.get(DIRECTORS_REPORT_URI)).thenReturn(directorsReportGet);
        when(directorsReportGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);
        when(directorsReportResourceHandler.create(eq(DIRECTORS_REPORT_URI), any(DirectorsReportApi.class))).thenReturn(directorsReportCreate);
        when(directorsReportCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorsReportService.createDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - directors report - success")
    void deleteDirectorsReportSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getDirectorsReport()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.delete(DIRECTORS_REPORT_URI)).thenReturn(directorsReportDelete);

        assertAll(() -> directorsReportService.deleteDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(directorsReportDelete).execute();
    }

    @Test
    @DisplayName("DELETE - directors report - no small full link")
    void deleteDirectorsReportNoSmallFullLink() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getDirectorsReport()).thenReturn(null);

        assertAll(() -> directorsReportService.deleteDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(apiClient, never()).smallFull();
    }

    @Test
    @DisplayName("DELETE - directors report - ApiErrorResponseException")
    void deleteDirectorsReportApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getDirectorsReport()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.delete(DIRECTORS_REPORT_URI)).thenReturn(directorsReportDelete);
        when(directorsReportDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorsReportService.deleteDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - directors report - URIValidationException")
    void deleteDirectorsReportURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getDirectorsReport()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.delete(DIRECTORS_REPORT_URI)).thenReturn(directorsReportDelete);
        when(directorsReportDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> directorsReportService.deleteDirectorsReport(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
