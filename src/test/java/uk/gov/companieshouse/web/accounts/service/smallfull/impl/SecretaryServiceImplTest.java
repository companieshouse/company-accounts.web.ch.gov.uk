package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.SecretaryResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.request.SecretaryCreate;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.request.SecretaryDelete;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.request.SecretaryGet;
import uk.gov.companieshouse.api.handler.smallfull.directorsreport.secretary.request.SecretaryUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportApi;
import uk.gov.companieshouse.api.model.accounts.directorsreport.DirectorsReportLinks;
import uk.gov.companieshouse.api.model.accounts.directorsreport.SecretaryApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.directorsreport.AddOrRemoveDirectors;
import uk.gov.companieshouse.web.accounts.service.smallfull.DirectorsReportService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.directorsreport.SecretaryTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecretaryServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SecretaryApi secretaryApi;

    @Mock
    private AddOrRemoveDirectors addOrRemoveDirectors;

    @Mock
    private SecretaryTransformer secretaryTransformer;

    @Mock
    private SecretaryGet secretaryGet;

    @Mock
    private SecretaryDelete secretaryDelete;

    @Mock
    private SecretaryUpdate secretaryUpdate;

    @Mock
    private SecretaryCreate secretaryCreate;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ApiResponse responseWithData;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private DirectorsReportResourceHandler directorsReportResourceHandler;

    @Mock
    private SecretaryResourceHandler secretaryResourceHandler;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private ValidationContext validationContext;

    @InjectMocks
    private SecretaryServiceImpl secretaryService;

    @Mock
    private DirectorsReportService directorsReportService;

    @Mock
    private DirectorsReportApi directorsReportApi;

    @Mock
    private DirectorsReportLinks directorsReportLinks;

    private static final String SECRETARY_LINK = "secretaryLink";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String SECRETARY_NAME = "name";

    private static final String RESOURCE_NAME = "secretaries";

    private static final String SECRETARY_URI =
            "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                    COMPANY_ACCOUNTS_ID + "/small-full/directors-report/secretary";


    @Test
    @DisplayName("Get Secretary success")
    void getSecretarySuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(secretaryResourceHandler.get(SECRETARY_URI)).thenReturn(secretaryGet);
        when(secretaryGet.execute()).thenReturn(responseWithData);

        SecretaryApi secretary = new SecretaryApi();
        secretary.setName(SECRETARY_NAME);
        when(responseWithData.getData()).thenReturn(secretary);

        String result = secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);
        assertEquals(SECRETARY_NAME, result);

    }

    @Test
    @DisplayName("Get secretary ApiErrorResponseException")
    void getSecretaryApiException() throws
            ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(secretaryResourceHandler.get(SECRETARY_URI)).thenReturn(secretaryGet);
        when(secretaryGet.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException
                (apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

    }

    @Test
    @DisplayName("Get secretary URIValidationException is thrown")
    void getSecretaryUriValidationException() throws
            ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(secretaryResourceHandler.get(SECRETARY_URI)).thenReturn(secretaryGet);
        when(secretaryGet.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException
                (uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> secretaryService.getSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

    }

    @Test
    @DisplayName("Submit secretary success")
    void submitSecretarySuccess() throws
            ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(getDirectorsReport());

        when(secretaryResourceHandler.create(SECRETARY_URI, secretaryApi)).thenReturn(
                secretaryCreate);
        when(secretaryCreate.execute()).thenReturn(responseWithData);
        when(responseWithData.hasErrors()).thenReturn(false);

        when(secretaryTransformer.getSecretaryApi(addOrRemoveDirectors)).thenReturn(secretaryApi);
        List<ValidationError> validationErrors = secretaryService.
                submitSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertTrue(validationErrors.isEmpty());

    }

    @Test
    @DisplayName("Update secretary success")
    void updateSecretarySuccess() throws
            ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);
        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);
        when(directorsReportLinks.getSecretary()).thenReturn(SECRETARY_LINK);

        when(secretaryResourceHandler.update(SECRETARY_URI, secretaryApi)).thenReturn(
                secretaryUpdate);
        when(secretaryUpdate.execute()).thenReturn(responseWithData);
        when(responseWithData.hasErrors()).thenReturn(false);

        when(secretaryTransformer.getSecretaryApi(addOrRemoveDirectors)).thenReturn(secretaryApi);
        List<ValidationError> validationErrors = secretaryService.
                submitSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertTrue(validationErrors.isEmpty());

    }

    @Test
    @DisplayName("Submit secretary with validation errors")
    void submitSecretaryValidationErrors()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(getDirectorsReport());

        when(secretaryResourceHandler.create(SECRETARY_URI, secretaryApi)).thenReturn(
                secretaryCreate);
        when(secretaryCreate.execute()).thenReturn(responseWithData);
        when(responseWithData.hasErrors()).thenReturn(true);

        ValidationError validationError = new ValidationError();
        List<ValidationError> apiValidationErrors = new ArrayList<>();
        apiValidationErrors.add(validationError);
        when(validationContext.getValidationErrors(responseWithData.getErrors())).thenReturn(
                apiValidationErrors);

        when(secretaryTransformer.getSecretaryApi(addOrRemoveDirectors)).thenReturn(secretaryApi);
        List<ValidationError> validationErrors = secretaryService.
                submitSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, addOrRemoveDirectors);

        assertEquals(apiValidationErrors, validationErrors);

    }

    @Test
    @DisplayName("Submit secretary with ApiErrorResponseException")
    void submitSecretaryApiException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(getDirectorsReport());

        when(secretaryResourceHandler.create(SECRETARY_URI, null)).thenReturn(secretaryCreate);
        when(secretaryCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> secretaryService.submitSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                        addOrRemoveDirectors));

    }

    @Test
    @DisplayName("Submit secretary with URIValidationException")
    void submitSecretaryUriException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(getDirectorsReport());

        when(secretaryResourceHandler.create(SECRETARY_URI, null)).thenReturn(secretaryCreate);
        when(secretaryCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> secretaryService.submitSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                        addOrRemoveDirectors));


    }

    @Test
    @DisplayName("Delete secretary success")
    void deleteSecretarySuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);
        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);
        when(directorsReportLinks.getSecretary()).thenReturn(SECRETARY_LINK);

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(secretaryResourceHandler.delete(SECRETARY_URI)).thenReturn(secretaryDelete);

        assertAll(() -> secretaryService.deleteSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
        verify(secretaryDelete).execute();

    }

    @Test
    @DisplayName("Delete secretary not found")
    void deleteSecretaryNotFound() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);
        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);
        when(directorsReportLinks.getSecretary()).thenReturn(null);

        assertAll(() -> secretaryService.deleteSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(apiClient, never()).smallFull();
    }

    @Test
    @DisplayName("Delete secretary ApiErrorResponseException")
    void deleteSecretaryApiException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);
        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);
        when(directorsReportLinks.getSecretary()).thenReturn(SECRETARY_LINK);

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(secretaryResourceHandler.delete(SECRETARY_URI)).thenReturn(secretaryDelete);
        when(secretaryDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).
                handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> secretaryService.deleteSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Delete secretary URIValidationException")
    void deleteSecretaryUriException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(directorsReportService.getDirectorsReport(apiClient, TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID)).thenReturn(directorsReportApi);
        when(directorsReportApi.getLinks()).thenReturn(directorsReportLinks);
        when(directorsReportLinks.getSecretary()).thenReturn(SECRETARY_LINK);

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);

        when(smallFullResourceHandler.directorsReport()).thenReturn(directorsReportResourceHandler);
        when(directorsReportResourceHandler.secretary()).thenReturn(secretaryResourceHandler);

        when(secretaryResourceHandler.delete(SECRETARY_URI)).thenReturn(secretaryDelete);
        when(secretaryDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).
                handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> secretaryService.deleteSecretary(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }


    private DirectorsReportApi getDirectorsReport() {
        DirectorsReportApi directorsReportApi = new DirectorsReportApi();
        DirectorsReportLinks directorsReportLinks = new DirectorsReportLinks();
        directorsReportLinks.setSecretary(null);
        directorsReportApi.setLinks(directorsReportLinks);
        return directorsReportApi;
    }

}
