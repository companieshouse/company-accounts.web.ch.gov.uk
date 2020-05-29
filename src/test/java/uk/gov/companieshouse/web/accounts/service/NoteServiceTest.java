package uk.gov.companieshouse.web.accounts.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.Executor;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.common.ApiResource;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.enumeration.NoteType;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.Note;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandler;
import uk.gov.companieshouse.web.accounts.service.notehandler.NoteResourceHandlerFactory;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformer;
import uk.gov.companieshouse.web.accounts.transformer.NoteTransformerFactory;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NoteServiceTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private NoteResourceHandlerFactory<ApiResource> noteResourceHandlerFactory;

    @Mock
    private NoteResourceHandler<ApiResource> noteResourceHandler;

    @Mock
    private Executor<ApiResponse<ApiResource>> executorWithResponseBody;

    @Mock
    private ApiResponse<ApiResource> apiResponseWithBody;

    @Mock
    private ApiResource apiResource;

    @Mock
    private Executor<ApiResponse<Void>> executorWithoutResponseBody;

    @Mock
    private ApiResponse<Void> apiResponseWithoutBody;

    @Mock
    private NoteTransformerFactory<Note, ApiResource> noteTransformerFactory;

    @Mock
    private NoteTransformer<Note, ApiResource> noteTransformer;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private List<ApiError> apiErrors;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private Note note;

    @InjectMocks
    private NoteService<Note> noteService;

    private static final NoteType NOTE_TYPE = NoteType.SMALL_FULL_OFF_BALANCE_SHEET_ARRANGEMENTS;

    private static final String URI = "uri";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    @BeforeEach
    private void setup() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(noteResourceHandlerFactory.getNoteResourceHandler(NOTE_TYPE)).thenReturn(noteResourceHandler);
        when(noteResourceHandler.getUri(TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(URI);
    }

    @Test
    @DisplayName("Get - success")
    void getSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteResourceHandler.get(apiClient, URI)).thenReturn(executorWithResponseBody);
        when(executorWithResponseBody.execute()).thenReturn(apiResponseWithBody);
        when(apiResponseWithBody.getData()).thenReturn(apiResource);

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toWeb(apiResource)).thenReturn(note);

        Optional<Note> response = noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE);

        assertNotNull(response);
        assertTrue(response.isPresent());
        assertEquals(note, response.get());
    }

    @Test
    @DisplayName("Get - not found")
    void getNotFound() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteResourceHandler.get(apiClient, URI)).thenReturn(executorWithResponseBody);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(404,"Not found", new HttpHeaders()).build();

        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(executorWithResponseBody.execute()).thenThrow(apiErrorResponseException);

        Optional<Note> response = noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE);

        assertNotNull(response);
        assertFalse(response.isPresent());
    }

    @Test
    @DisplayName("Get - ApiErrorResponseException")
    void getApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {

        when(noteResourceHandler.get(apiClient, URI)).thenReturn(executorWithResponseBody);

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(500,"Internal server error", new HttpHeaders()).build();

        ApiErrorResponseException apiErrorResponseException =
                ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(executorWithResponseBody.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ServiceException.class, () -> noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE));
    }

    @Test
    @DisplayName("Get - URIValidationException")
    void getURIValidationException() throws ApiErrorResponseException, URIValidationException {

        when(noteResourceHandler.get(apiClient, URI)).thenReturn(executorWithResponseBody);

        when(executorWithResponseBody.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () -> noteService.get(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE));
    }

    @Test
    @DisplayName("Submit - create - success")
    void submitCreateSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(false);

        when(noteResourceHandler.create(apiClient, URI, apiResource)).thenReturn(executorWithResponseBody);

        when(executorWithResponseBody.execute()).thenReturn(apiResponseWithBody);

        when(apiResponseWithBody.hasErrors()).thenReturn(false);

        List<ValidationError> returnedValidationErrors = noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE);

        assertTrue(returnedValidationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit - create - has validation errors")
    void submitCreateHasValidationErrors() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(false);

        when(noteResourceHandler.create(apiClient, URI, apiResource)).thenReturn(executorWithResponseBody);

        when(executorWithResponseBody.execute()).thenReturn(apiResponseWithBody);

        when(apiResponseWithBody.hasErrors()).thenReturn(true);

        when(apiResponseWithBody.getErrors()).thenReturn(apiErrors);

        when(validationContext.getValidationErrors(apiErrors)).thenReturn(validationErrors);

        List<ValidationError> returnedValidationErrors = noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE);

        assertEquals(validationErrors, returnedValidationErrors);
    }

    @Test
    @DisplayName("Submit - create - ApiErrorResponseException")
    void submitCreateApiErrorResponseException() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(false);

        when(noteResourceHandler.create(apiClient, URI, apiResource)).thenReturn(executorWithResponseBody);

        when(executorWithResponseBody.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE));
    }

    @Test
    @DisplayName("Submit - create - URIValidationException")
    void submitCreateURIValidationException() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(false);

        when(noteResourceHandler.create(apiClient, URI, apiResource)).thenReturn(executorWithResponseBody);

        when(executorWithResponseBody.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE));
    }

    @Test
    @DisplayName("Submit - update - success")
    void submitUpdateSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(true);

        when(noteResourceHandler.update(apiClient, URI, apiResource)).thenReturn(executorWithoutResponseBody);

        when(executorWithoutResponseBody.execute()).thenReturn(apiResponseWithoutBody);

        when(apiResponseWithoutBody.hasErrors()).thenReturn(false);

        List<ValidationError> returnedValidationErrors = noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE);

        assertTrue(returnedValidationErrors.isEmpty());
    }

    @Test
    @DisplayName("Submit - update - has validation errors")
    void submitUpdateHasValidationErrors() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(true);

        when(noteResourceHandler.update(apiClient, URI, apiResource)).thenReturn(executorWithoutResponseBody);

        when(executorWithoutResponseBody.execute()).thenReturn(apiResponseWithoutBody);

        when(apiResponseWithoutBody.hasErrors()).thenReturn(true);

        when(apiResponseWithoutBody.getErrors()).thenReturn(apiErrors);

        when(validationContext.getValidationErrors(apiErrors)).thenReturn(validationErrors);

        List<ValidationError> returnedValidationErrors = noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE);

        assertEquals(validationErrors, returnedValidationErrors);
    }

    @Test
    @DisplayName("Submit - update - ApiErrorResponseException")
    void submitUpdateApiErrorResponseException() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(true);

        when(noteResourceHandler.update(apiClient, URI, apiResource)).thenReturn(executorWithoutResponseBody);

        when(executorWithoutResponseBody.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE));
    }

    @Test
    @DisplayName("Submit - update - URIValidationException")
    void submitUpdateURIValidationException() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toApi(note)).thenReturn(apiResource);

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(true);

        when(noteResourceHandler.update(apiClient, URI, apiResource)).thenReturn(executorWithoutResponseBody);

        when(executorWithoutResponseBody.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                noteService.submit(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, note, NOTE_TYPE));
    }

    @Test
    @DisplayName("Delete - success")
    void deleteSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(true);

        when(noteResourceHandler.delete(apiClient, URI)).thenReturn(executorWithoutResponseBody);

        assertAll(() -> noteService.delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE));

        verify(executorWithoutResponseBody).execute();
    }

    @Test
    @DisplayName("Delete - does not exist")
    void deleteDoesNotExist() throws ServiceException {

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(false);

        assertAll(() -> noteService.delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE));

        verify(noteResourceHandler, never()).delete(apiClient, URI);
    }

    @Test
    @DisplayName("Delete - ApiErrorResponseException")
    void deleteApiErrorResponseException() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(true);

        when(noteResourceHandler.delete(apiClient, URI)).thenReturn(executorWithoutResponseBody);

        when(executorWithoutResponseBody.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () -> noteService.delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE));
    }

    @Test
    @DisplayName("Delete - URIValidationException")
    void deleteURIValidationException() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(noteResourceHandler.parentResourceExists(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(true);

        when(noteResourceHandler.delete(apiClient, URI)).thenReturn(executorWithoutResponseBody);

        when(executorWithoutResponseBody.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () -> noteService.delete(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, NOTE_TYPE));
    }
}
