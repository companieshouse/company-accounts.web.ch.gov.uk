package uk.gov.companieshouse.web.accounts.service.payment.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.payment.PaymentSessionApi;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.CompanyAccountsWebApplication;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private ApiClientService apiClientService;

    private String chsUrl;

    private String apiUrl;

    private static final String CHS_URL = "CHS_URL";

    private static final String API_URL = "API_URL";

    private static final String JOURNEY_LINK = "journey";

    private static final String PAYMENT_URL = "/payment";

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(CompanyAccountsWebApplication.APPLICATION_NAME_SPACE);

    @Autowired
    public PaymentServiceImpl(ApiClientService apiClientService, EnvironmentReader environmentReader) {

        this.apiClientService = apiClientService;
        this.chsUrl = environmentReader.getMandatoryString(CHS_URL);
        this.apiUrl = environmentReader.getMandatoryString(API_URL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createPaymentSessionForTransaction(String transactionId)
            throws ServiceException {

        PaymentSessionApi paymentSessionApi = new PaymentSessionApi();
        paymentSessionApi.setRedirectUri(chsUrl + "/transaction/" + transactionId + "/confirmation");
        paymentSessionApi.setResource(apiUrl + "/transactions/" + transactionId + PAYMENT_URL);
        paymentSessionApi.setReference("cic_report_and_accounts_" + transactionId);
        paymentSessionApi.setState(UUID.randomUUID().toString());

        try {
            return apiClientService.getApiClient()
                    .payment().create(PAYMENT_URL, paymentSessionApi)
                            .execute().getData().getLinks().get(JOURNEY_LINK);
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error creating payment session", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for payment resource", e);
        }
    }
}
