package uk.gov.companieshouse.web.accounts.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
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
    private ApiResource responseBody;

    @Mock
    private NoteTransformerFactory<Note, ApiResource> noteTransformerFactory;

    @Mock
    private NoteTransformer<Note, ApiResource> noteTransformer;

    @Mock
    private ValidationContext validationContext;

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
        when(apiResponseWithBody.getData()).thenReturn(responseBody);

        when(noteTransformerFactory.getNoteTransformer(NOTE_TYPE)).thenReturn(noteTransformer);
        when(noteTransformer.toWeb(responseBody)).thenReturn(note);

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
}
