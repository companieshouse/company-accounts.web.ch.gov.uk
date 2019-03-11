package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.request.CurrentAssetsInvestmentsCreate;
import uk.gov.companieshouse.api.handler.smallfull.currentassetsinvestments.request.CurrentAssetsInvestmentsGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.currentassetsinvestments.CurrentAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.currentassetsinvestments.CurrentAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.CurrentAssetsInvestmentsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CurrentAssetsInvestmentsServiceImplTests {

    @Mock
    private CurrentAssetsInvestmentsTransformer mockTransformer;

    @Mock
    private CurrentAssetsInvestmentsResourceHandler mockCurrentAssetsInvestmentsResourceHandler;

    @Mock
    private CurrentAssetsInvestmentsGet mockCurrentAssetsInvestmentsGet;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullService mockSmallFullService;

    @Mock
    private CurrentAssetsInvestmentsCreate mockCurrentAssetsInvestmentsCreate;

    @Mock
    private ValidationContext mockValidationContext;

    @InjectMocks
    private CurrentAssetsInvestmentsService service = new CurrentAssetsInvestmentsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String TEST_DETAILS = "details text";

    private static final String CURRENTS_ASSETS_INVESTMENTS_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/notes/current-assets-investments";

    @Test
    @DisplayName("GET - currentAssetsInvestments successful path")
    void getCurrentAssetsInvestmentsSuccess() throws Exception {

        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = new CurrentAssetsInvestmentsApi();
        getMockCurrentAssetsInvestmentsApi(currentAssetsInvestmentsApi);

        when(mockTransformer.getCurrentAssetsInvestments(currentAssetsInvestmentsApi))
            .thenReturn(createCurrentAssetsInvestments());

        CurrentAssetsInvestments currentAssetsInvestments =
            service.getCurrentAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(currentAssetsInvestments);
        assertEquals(TEST_DETAILS, currentAssetsInvestments.getDetails());
    }

    @Test
    @DisplayName("GET - currentAssetsInvestments successful path when http status not found")
    void getCurrentAssetsInvestmentsSuccessHttpSTatusNotFound() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException
            .Builder(404, "Not Found", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockCurrentAssetsInvestmentsResourceHandler();
        when(mockCurrentAssetsInvestmentsResourceHandler.get(CURRENTS_ASSETS_INVESTMENTS_URI))
            .thenReturn(mockCurrentAssetsInvestmentsGet);
        when(mockCurrentAssetsInvestmentsGet.execute()).thenThrow(apiErrorResponseException);
        when(mockTransformer.getCurrentAssetsInvestments(null))
            .thenReturn(createCurrentAssetsInvestments());

        CurrentAssetsInvestments currentAssetsInvestments =
            service.getCurrentAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(currentAssetsInvestments);
        assertEquals(TEST_DETAILS, currentAssetsInvestments.getDetails());
    }

    @Test
    @DisplayName("GET - currentAssetsInvestments throws ServiceException due to 400 Bad Request")
    void getCurrentAssetsInvestmentsApiResponseException() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException
            .Builder(400, "Bad Request", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockCurrentAssetsInvestmentsResourceHandler();
        when(mockCurrentAssetsInvestmentsResourceHandler.get(CURRENTS_ASSETS_INVESTMENTS_URI))
            .thenReturn(mockCurrentAssetsInvestmentsGet);
        when(mockCurrentAssetsInvestmentsGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockCurrentAssetsInvestmentsGet.execute());
        assertThrows(ServiceException.class, () -> service.getCurrentAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - currentAssetsInvestments throws ServiceException due to URIValidationException")
    void getCurrentAssetsInvestmentsURIValidationException() throws Exception {

        getMockCurrentAssetsInvestmentsResourceHandler();
        when(mockCurrentAssetsInvestmentsResourceHandler.get(CURRENTS_ASSETS_INVESTMENTS_URI))
            .thenReturn(mockCurrentAssetsInvestmentsGet);
        when(mockCurrentAssetsInvestmentsGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockCurrentAssetsInvestmentsGet.execute());
        assertThrows(ServiceException.class, () -> service.getCurrentAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - currentAssetsInvestments successful path")
    void postCurrentAssetsInvestmentsSuccess() throws Exception {

        CurrentAssetsInvestments currentAssetsInvestments= createCurrentAssetsInvestments();
        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = createCurrentAssetsInvestmentsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        when(mockSmallFullService.getSmallFullAccounts(
            mockApiClient, TRANSACTION_ID,  COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        setLinksWithoutCurrentAssetsInvestments(smallFullApi);

        when(mockTransformer.getCurrentAssetsInvestmentsApi(currentAssetsInvestments))
            .thenReturn(currentAssetsInvestmentsApi);

        currentAssetsInvestmentsCreate(currentAssetsInvestmentsApi);

        List<ValidationError> validationErrors = service.submitCurrentAssetsInvestments(
            TRANSACTION_ID, COMPANY_ACCOUNTS_ID, currentAssetsInvestments, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("POST - currentAssetsInvestments throws ServiceException due to ApiErrorResponseException - 404 Not Found")
    void postCurrentAssetsInvestmentsApiErrorResponseExceptionNotFound() throws Exception {

        getMockCurrentAssetsInvestmentsResourceHandler();

        CurrentAssetsInvestments currentAssetsInvestments = createCurrentAssetsInvestments();
        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = createCurrentAssetsInvestmentsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutCurrentAssetsInvestments(smallFullApi);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(mockTransformer.getCurrentAssetsInvestmentsApi(currentAssetsInvestments))
            .thenReturn(currentAssetsInvestmentsApi);
        when(mockCurrentAssetsInvestmentsResourceHandler
            .create(CURRENTS_ASSETS_INVESTMENTS_URI, currentAssetsInvestmentsApi))
            .thenReturn(mockCurrentAssetsInvestmentsCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(
            404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockCurrentAssetsInvestmentsCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockCurrentAssetsInvestmentsCreate.execute());
        assertThrows(ServiceException.class, () -> service.submitCurrentAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            currentAssetsInvestments,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - currentAssetsInvestments throws ServiceException due to ApiErrorResponseException - 400 Bad Request")
    void postCurrentAssetsInvestmentsApiErrorResponseExceptionBadRequest() throws Exception {

        getMockCurrentAssetsInvestmentsResourceHandler();

        CurrentAssetsInvestments currentAssetsInvestments = createCurrentAssetsInvestments();
        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = createCurrentAssetsInvestmentsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutCurrentAssetsInvestments(smallFullApi);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(mockTransformer.getCurrentAssetsInvestmentsApi(currentAssetsInvestments))
            .thenReturn(currentAssetsInvestmentsApi);
        when(mockCurrentAssetsInvestmentsResourceHandler
            .create(CURRENTS_ASSETS_INVESTMENTS_URI, currentAssetsInvestmentsApi))
            .thenReturn(mockCurrentAssetsInvestmentsCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(
            400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockCurrentAssetsInvestmentsCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockCurrentAssetsInvestmentsCreate.execute());
        assertThrows(ServiceException.class, () -> service.submitCurrentAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            currentAssetsInvestments,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - currentAssetsInvestments throws ServiceException getting Smallfull data")
    void postCurrentAssetsInvestmentsGetSmallFullDataApiResponseException() throws Exception {

        CurrentAssetsInvestments currentAssetsInvestments = createCurrentAssetsInvestments();
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> service.submitCurrentAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            currentAssetsInvestments,
            COMPANY_NUMBER));
    }

    private void currentAssetsInvestmentsCreate(CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi) {
        getMockCurrentAssetsInvestmentsResourceHandler();
        when(mockCurrentAssetsInvestmentsResourceHandler
            .create(CURRENTS_ASSETS_INVESTMENTS_URI, currentAssetsInvestmentsApi))
            .thenReturn(mockCurrentAssetsInvestmentsCreate);
    }

    private void setLinksWithoutCurrentAssetsInvestments(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setCurrentAssetsInvestmentsNote(null);
        smallFullApi.setLinks(links);
    }

    private void getMockCurrentAssetsInvestmentsApi(CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi) throws Exception {
        getMockCurrentAssetsInvestmentsResourceHandler();
        when(mockCurrentAssetsInvestmentsResourceHandler.get(CURRENTS_ASSETS_INVESTMENTS_URI))
            .thenReturn(mockCurrentAssetsInvestmentsGet);
        when(mockCurrentAssetsInvestmentsGet.execute()).thenReturn(currentAssetsInvestmentsApi);
    }

    private void getMockCurrentAssetsInvestmentsResourceHandler() {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.currentAssetsInvestments())
            .thenReturn(mockCurrentAssetsInvestmentsResourceHandler);
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private CurrentAssetsInvestments createCurrentAssetsInvestments() {
        CurrentAssetsInvestments currentAssetsInvestments = new CurrentAssetsInvestments();
        currentAssetsInvestments.setDetails(TEST_DETAILS);
        return currentAssetsInvestments;
    }

    private CurrentAssetsInvestmentsApi createCurrentAssetsInvestmentsApi() {
        CurrentAssetsInvestmentsApi currentAssetsInvestmentsApi = new CurrentAssetsInvestmentsApi();
        currentAssetsInvestmentsApi.setDetails(TEST_DETAILS);
        return currentAssetsInvestmentsApi;
    }
}
