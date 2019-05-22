package uk.gov.companieshouse.web.accounts.validation.helper;

import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface ServiceExceptionHandler {

    /**
     * Handles an {@link ApiErrorResponseException}, triggered by resource submission
     * @param e The exception which has occurred as a result of submitting an API resource
     * @param resourceName The name of the submitted resource (e.g. 'debtors'), used for the exception message
     * @throws ServiceException wrapping the {@link ApiErrorResponseException} with an appropriate message
     */
    void handleSubmissionException(ApiErrorResponseException e, String resourceName) throws ServiceException;

    /**
     * Handles an {@link ApiErrorResponseException} which occurs as a result of a delete request
     * @param e The exception which has occurred as a result of a delete request
     * @param resourceName The name of the submitted resource (e.g. 'debtors'), used for the exception message
     * @throws ServiceException wrapping the {@link ApiErrorResponseException} with an appropriate message
     */
    void handleDeletionException(ApiErrorResponseException e, String resourceName) throws ServiceException;

    /**
     * Handles an {@link ApiErrorResponseException} which occurs as a result of a get request
     * @param e The exception which has occurred as a result of a get request
     * @param resourceName The name of the submitted resource (e.g. 'debtors'), used for the exception message
     * @throws ServiceException if the exception is not thrown as the result of a 'Not Found' response
     */
    void handleRetrievalException(ApiErrorResponseException e, String resourceName) throws ServiceException;

    /**
     * Handles a {@link URIValidationException}
     * @param e The exception which has occurred
     * @param resourceName The name of the submitted resource (e.g. 'debtors'), used for the exception message
     * @throws ServiceException wrapping the {@link URIValidationException} with an appropriate message
     */
    void handleURIValidationException(URIValidationException e, String resourceName) throws ServiceException;
}
