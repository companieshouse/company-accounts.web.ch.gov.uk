package uk.gov.companieshouse.web.accounts.controller;

/**
 * The {@code ConditionalController} interface defines a single method that
 * should be implemented in controller classes whose template is rendered
 * conditionally (i.e. the controller may be skipped during the web journey).
 * This method should return boolean true to indicate that the template should
 * be rendered, otherwise false.
 **/
public interface ConditionalController {

    /**
     * Returns a boolean value indicating whether the template associated with
     * a controller should be rendered or not.
     *
     * @param companyNumber     the company number
     * @param transactionId     the transaction identifier
     * @param companyAccountsId the company accounts identifier
     *
     * @return true if the template for a controller should be rendered
     */
    boolean willRender(String companyNumber, String transactionId, String companyAccountsId);
}
