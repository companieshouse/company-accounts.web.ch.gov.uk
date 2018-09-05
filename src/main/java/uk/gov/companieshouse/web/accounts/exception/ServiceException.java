package uk.gov.companieshouse.web.accounts.exception;

/**
 * The class {@code ServiceException} is a form of {@code Exception}
 * that should be used at the service layer to abstract lower level
 * exceptions from being propagated up the call stack.
 */
public class ServiceException extends Exception {

    /**
     * Constructs a new {@code ServiceException} with the specified
     * cause.
     *
     * @param cause the cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
