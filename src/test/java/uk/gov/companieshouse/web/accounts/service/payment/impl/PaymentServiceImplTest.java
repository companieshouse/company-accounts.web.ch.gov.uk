package uk.gov.companieshouse.web.accounts.service.payment.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.payment.PaymentResourceHandler;
import uk.gov.companieshouse.api.handler.payment.request.PaymentCreate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.payment.PaymentApi;
import uk.gov.companieshouse.api.model.payment.PaymentSessionApi;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.payment.PaymentService;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentServiceImplTest {

    @Mock
    private ApiClient apiClient;

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private EnvironmentReader environmentReader;

    @Mock
    private PaymentResourceHandler paymentResourceHandler;

    @Mock
    private PaymentCreate paymentCreate;

    @Mock
    private ApiResponse<PaymentApi> apiResponse;

    @Mock
    private PaymentApi paymentApi;

    @Mock
    private Map<String, String> links;

    private PaymentService paymentService;

    private static final String PAYMENT_ENDPOINT = "/payment";

    private static final String TRANSACTION_ID = "transactionId";

    private static final String JOURNEY_LINK = "journey";

    private static final String JOURNEY_URL = "journeyUrl";

    @BeforeEach
    private void setUp() {

        paymentService = new PaymentServiceImpl(apiClientService, environmentReader);
    }

    @Test
    @DisplayName("Create payment session - success")
    void createPaymentSessionSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.payment()).thenReturn(paymentResourceHandler);

        when(paymentResourceHandler.create(eq(PAYMENT_ENDPOINT), any(PaymentSessionApi.class)))
                .thenReturn(paymentCreate);

        when(paymentCreate.execute()).thenReturn(apiResponse);

        when(apiResponse.getData()).thenReturn(paymentApi);

        when(paymentApi.getLinks()).thenReturn(links);

        when(links.get(JOURNEY_LINK)).thenReturn(JOURNEY_URL);

        String journeyUrl = paymentService.createPaymentSessionForTransaction(TRANSACTION_ID);

        assertEquals(JOURNEY_URL, journeyUrl);
    }

    @Test
    @DisplayName("Create payment session - throws ApiErrorResponseException")
    void createPaymentSessionThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.payment()).thenReturn(paymentResourceHandler);

        when(paymentResourceHandler.create(eq(PAYMENT_ENDPOINT), any(PaymentSessionApi.class)))
                .thenReturn(paymentCreate);

        when(paymentCreate.execute()).thenThrow(ApiErrorResponseException.class);

        assertThrows(ServiceException.class, () ->
                paymentService.createPaymentSessionForTransaction(TRANSACTION_ID));
    }

    @Test
    @DisplayName("Create payment session - throws URIValidationException")
    void createPaymentSessionThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(apiClient.payment()).thenReturn(paymentResourceHandler);

        when(paymentResourceHandler.create(eq(PAYMENT_ENDPOINT), any(PaymentSessionApi.class)))
                .thenReturn(paymentCreate);

        when(paymentCreate.execute()).thenThrow(URIValidationException.class);

        assertThrows(ServiceException.class, () ->
                paymentService.createPaymentSessionForTransaction(TRANSACTION_ID));
    }
}
