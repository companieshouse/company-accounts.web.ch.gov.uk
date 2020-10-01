package uk.gov.companieshouse.web.accounts.validation.helper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceExceptionHandlerTests {

    private final ServiceExceptionHandler serviceExceptionHandler = new ServiceExceptionHandlerImpl();

    private static final String RESOURCE_NAME = "resourceName";

    @Test
    @DisplayName("Handle submission exception")
    void handleSubmissionException() {

        HttpResponseException httpResponseException =
                new HttpResponseException.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), new HttpHeaders())
                        .build();

        ApiErrorResponseException e = ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        Throwable exception =
                assertThrows(ServiceException.class, () -> serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME));

        assertEquals("Error when submitting resource: " + RESOURCE_NAME, exception.getMessage());
        assertEquals(e, exception.getCause());
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
