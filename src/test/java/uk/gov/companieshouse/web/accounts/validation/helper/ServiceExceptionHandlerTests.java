package uk.gov.companieshouse.web.accounts.validation.helper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServiceExceptionHandlerTests {

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private ValidationContext validationContext;

    @InjectMocks
    private ServiceExceptionHandler serviceExceptionHandler = new ServiceExceptionHandlerImpl();

    private static final String RESOURCE_NAME = "resourceName";

    @Test
    @DisplayName("Handle submission exception - bad request with validation errors")
    void handleSubmissionExceptionBadRequestWithValidationErrors() throws ServiceException {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders())
                        .build();

        ApiErrorResponseException e = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(validationContext.getValidationErrors(e)).thenReturn(validationErrors);
        when(validationErrors.isEmpty()).thenReturn(false);

        List<ValidationError> returnedErrors = serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);

        assertEquals(validationErrors, returnedErrors);
    }

    @Test
    @DisplayName("Handle submission exception - bad request without validation errors")
    void handleSubmissionExceptionBadRequestWithoutValidationErrors() {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), new HttpHeaders())
                        .build();

        ApiErrorResponseException e = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        when(validationContext.getValidationErrors(e)).thenReturn(validationErrors);
        when(validationErrors.isEmpty()).thenReturn(true);

        Throwable exception =
                assertThrows(ServiceException.class, () -> serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME));

        assertEquals("Bad request when submitting resource: " + RESOURCE_NAME, exception.getMessage());
        assertEquals(e, exception.getCause());
    }

    @Test
    @DisplayName("Handle submission exception - internal server error")
    void handleSubmissionExceptionInternalServerError() {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new HttpHeaders())
                        .build();

        ApiErrorResponseException e = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        Throwable exception =
                assertThrows(ServiceException.class, () -> serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME));

        assertEquals("Error when submitting resource: " + RESOURCE_NAME, exception.getMessage());
        assertEquals(e, exception.getCause());
        verify(validationContext, never()).getValidationErrors(e);
    }

    @Test
    @DisplayName("Handle deletion exception")
    void handleDeletionException() {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new HttpHeaders())
                        .build();

        ApiErrorResponseException e = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        Throwable exception =
                assertThrows(ServiceException.class, () -> serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME));

        assertEquals("Error deleting resource: " + RESOURCE_NAME, exception.getMessage());
        assertEquals(e, exception.getCause());
    }

    @Test
    @DisplayName("Handle retrieval exception - internal server error")
    void handleRetrievalExceptionInternalServerError() {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new HttpHeaders())
                        .build();

        ApiErrorResponseException e = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        Throwable exception =
                assertThrows(ServiceException.class, () -> serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME));

        assertEquals("Error retrieving resource: " + RESOURCE_NAME, exception.getMessage());
        assertEquals(e, exception.getCause());
    }

    @Test
    @DisplayName("Handle retrieval exception - not found")
    void handleRetrievalExceptionNotFound() {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), new HttpHeaders())
                        .build();

        ApiErrorResponseException e = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        assertAll(() -> serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME));
    }

    @Test
    @DisplayName("Handle URI validation exception")
    void handleURIValidationException() {

        URIValidationException e = new URIValidationException("Invalid URI");

        Throwable exception =
                assertThrows(ServiceException.class, () -> serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME));

        assertEquals("Invalid URI for resource: " + RESOURCE_NAME, exception.getMessage());
        assertEquals(e, exception.getCause());
    }
}
