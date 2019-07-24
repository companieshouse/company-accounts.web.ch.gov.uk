package uk.gov.companieshouse.web.accounts.service.payment;

import uk.gov.companieshouse.web.accounts.exception.ServiceException;

public interface PaymentService {

    /**
     * Creates a payment session in order to pay to close a transaction
     *
     * @param transactionId The id of the CHS transaction
     * @param companyNumber The company number
     * @return A URL to to which to redirect to perform the payment
     * @throws ServiceException if there's an error creating the payment session
     */
    String createPaymentSessionForTransaction(String transactionId, String companyNumber)
            throws ServiceException;
}