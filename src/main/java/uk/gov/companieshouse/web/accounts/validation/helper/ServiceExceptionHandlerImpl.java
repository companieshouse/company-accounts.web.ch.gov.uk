package uk.gov.companieshouse.web.accounts.validation.helper;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@Component
public class ServiceExceptionHandlerImpl implements ServiceExceptionHandler {

    @Autowired
    private ValidationContext validationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ValidationError> handleSubmissionException(ApiErrorResponseException e, String resourceName)
            throws ServiceException {

        if (e.getStatusCode() == HttpStatus.BAD_REQUEST.value()) {
            List<ValidationError> validationErrors = validationContext.getValidationErrors(e);
            if (validationErrors.isEmpty()) {
                throw new ServiceException("Bad request when submitting resource: " + resourceName, e);
            }
            return validationErrors;
        } else {
            throw new ServiceException("Error when submitting resource: " + resourceName, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleDeletionException(ApiErrorResponseException e, String resourceName)
            throws ServiceException{

        throw new ServiceException("Error deleting resource: " + resourceName, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleRetrievalException(ApiErrorResponseException e, String resourceName)
            throws ServiceException {

        if (e.getStatusCode() != HttpStatus.NOT_FOUND.value()) {
            throw new ServiceException("Error retrieving resource: " + resourceName, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleURIValidationException(URIValidationException e, String resourceName)
            throws ServiceException {

        throw new ServiceException("Invalid URI for resource: " + resourceName, e);
    }
}
