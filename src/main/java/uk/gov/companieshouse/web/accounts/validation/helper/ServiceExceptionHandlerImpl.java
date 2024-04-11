package uk.gov.companieshouse.web.accounts.validation.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;

@Component
public class ServiceExceptionHandlerImpl implements ServiceExceptionHandler {
    @Autowired
    private ValidationContext validationContext;

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleSubmissionException(ApiErrorResponseException e, String resourceName)
            throws ServiceException {
        throw new ServiceException("Error when submitting resource: " + resourceName, e);
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
