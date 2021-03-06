package uk.gov.companieshouse.web.accounts.service.payment.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.api.model.payment.PaymentSessionApi;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;
import uk.gov.companieshouse.web.accounts.session.SessionService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private ApiClientService apiClientService;

    private SessionService sessionService;

    private String chsUrl;

    private String apiUrl;

    private static final String CHS_URL = "CHS_URL";

    private static final String API_URL = "API_URL";

    private static final String JOURNEY_LINK = "journey";

    private static final String PAYMENT_URL = "/payments";

    private static final String PAYMENT_STATE = "payment_state";

    @Autowired
    public PaymentServiceImpl(ApiClientService apiClientService, SessionService sessionService, EnvironmentReader environmentReader) {

        this.apiClientService = apiClientService;
        this.sessionService = sessionService;
        this.chsUrl = environmentReader.getMandatoryString(CHS_URL);
        this.apiUrl = environmentReader.getMandatoryString(API_URL);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createPaymentSessionForTransaction(String transactionId)
            throws ServiceException {

        String paymentState = UUID.randomUUID().toString();

        PaymentSessionApi paymentSessionApi = new PaymentSessionApi();
        paymentSessionApi.setRedirectUri(chsUrl + "/transaction/" + transactionId + "/confirmation");
        paymentSessionApi.setResource(apiUrl + "/transactions/" + transactionId + "/payment");
        paymentSessionApi.setReference("cic_report_and_accounts_" + transactionId);
        paymentSessionApi.setState(paymentState);

        try {
            ApiResponse<PaymentApi> apiResponse = apiClientService.getApiClient()
                .payment().create(PAYMENT_URL, paymentSessionApi)
                    .execute();

            setPaymentStateOnSession(paymentState);

            return apiResponse.getData().getLinks().get(JOURNEY_LINK);
        } catch (ApiErrorResponseException e) {

            throw new ServiceException("Error creating payment session", e);
        } catch (URIValidationException e) {

            throw new ServiceException("Invalid URI for payment resource", e);
        }
    }

    private void setPaymentStateOnSession(String paymentState) {

        sessionService.getSessionDataFromContext().put(PAYMENT_STATE, paymentState);
    }
}
