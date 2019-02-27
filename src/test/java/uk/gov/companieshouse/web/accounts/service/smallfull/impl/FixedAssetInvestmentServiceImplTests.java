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
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.request.FixedAssetsInvestmentsCreate;
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.request.FixedAssetsInvestmentsDelete;
import uk.gov.companieshouse.api.handler.smallfull.fixedassetsinvestments.request.FixedAssetsInvestmentsGet;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.FixedAssetsInvestmentsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FixedAssetInvestmentServiceImplTests {

    @Mock
    private ApiClientService mockApiClientService;

    @Mock
    private ApiClient mockApiClient;

    @Mock
    private SmallFullResourceHandler mockSmallFullResourceHandler;

    @Mock
    private FixedAssetsInvestmentsResourceHandler mockFixedAssetsInvestmentsResourceHandler;

    @Mock
    private FixedAssetsInvestmentsGet mockFixedAssetsInvestmentsGet;
    
    @Mock
    private FixedAssetsInvestmentsDelete mockFixedAssetsInvestmentsDelete;

    @Mock
    private FixedAssetsInvestmentsCreate mockFixedAssetsInvestmentsCreate;

    @Mock
    private FixedAssetsInvestmentsTransformer mockFixedAssetsInvestmentsTransformer;

    @Mock
    private SmallFullService mockSmallFullService;

    @Mock
    private ValidationContext mockValidationContext;

    @InjectMocks
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService = new FixedAssetsInvestmentsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String FIXED_ASSETS_INVESTMENTS_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/notes/fixed-assets-investments";
    
    private static final String TEST_DETAILS = "details text";

    @Test
    @DisplayName("GET - fixedAssetsInvestments successful path")
    void getFixedAssetsInvestmentsSuccess() throws Exception {

        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = new FixedAssetsInvestmentsApi();
        getMockFixedAssetsInvestmentsApi(fixedAssetsInvestmentsApi);

        when(mockFixedAssetsInvestmentsTransformer.getFixedAssetsInvestments(fixedAssetsInvestmentsApi)).thenReturn(createFixedAssetsInvestments());

        FixedAssetsInvestments fixedAssetsInvestments = fixedAssetsInvestmentsService.getFixedAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(fixedAssetsInvestments);
        assertEquals(TEST_DETAILS, fixedAssetsInvestments.getDetails());
    }

    @Test
    @DisplayName("GET - fixedAssetsInvestments successful path when http status not found")
    void getFixedAssetsInvestmentsSuccessHttpStatusNotFound() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(404, "Not" +
            " Found", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.get(FIXED_ASSETS_INVESTMENTS_URI)).
            thenReturn(mockFixedAssetsInvestmentsGet);
        when(mockFixedAssetsInvestmentsGet.execute()).thenThrow(apiErrorResponseException);
        when(mockFixedAssetsInvestmentsTransformer.getFixedAssetsInvestments(null))
            .thenReturn(createFixedAssetsInvestments());

        FixedAssetsInvestments fixedAssetsInvestments =
            fixedAssetsInvestmentsService.getFixedAssetsInvestments(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(fixedAssetsInvestments);
        assertEquals(TEST_DETAILS, fixedAssetsInvestments.getDetails());
    }

    @Test
    @DisplayName("GET - fixedAssetsInvestments throws ServiceException due to " +
        "ApiErrorResponseException - 400 Bad Request")
    void getFixedAssetsInvestmentsApiResponseException() throws Exception {

        HttpResponseException httpResponseException = new HttpResponseException.Builder(400, "Bad" +
            " Request", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.get(FIXED_ASSETS_INVESTMENTS_URI))
            .thenReturn(mockFixedAssetsInvestmentsGet);
        when(mockFixedAssetsInvestmentsGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockFixedAssetsInvestmentsGet.execute());
        assertThrows(ServiceException.class,
            () -> fixedAssetsInvestmentsService.getFixedAssetsInvestments(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - fixedAssetsInvestments throws ServiceExcepiton due to " +
        "URIValidationException")
    void getFixedAssetsInvestmentsURIValidationException() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.get(FIXED_ASSETS_INVESTMENTS_URI))
            .thenReturn(mockFixedAssetsInvestmentsGet);
        when(mockFixedAssetsInvestmentsGet.execute()).thenThrow(URIValidationException.class);

        assertThrows(URIValidationException.class, () -> mockFixedAssetsInvestmentsGet.execute());
        assertThrows(ServiceException.class,
            () -> fixedAssetsInvestmentsService.getFixedAssetsInvestments(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - fixedAssetsInvestments successful path")
    void postFixedAssetsInvestmentsSuccess() throws Exception {

        FixedAssetsInvestments fixedAssetsInvestments = createFixedAssetsInvestments();
        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = createFixedAssetsInvestmentsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);
        setLinksWithoutFixedAssetsInvestments(smallFullApi);

        when(mockFixedAssetsInvestmentsTransformer.getFixedAssetsInvestmentsApi(fixedAssetsInvestments)).thenReturn(fixedAssetsInvestmentsApi);

        fixedAssetsInvestmentsCreate(fixedAssetsInvestmentsApi);

        List<ValidationError> validationErrors = fixedAssetsInvestmentsService.submitFixedAssetsInvestments(TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID, fixedAssetsInvestments, COMPANY_NUMBER);

        assertEquals(0, validationErrors.size());
    }

    @Test
    @DisplayName("POST - fixedAssetsInvestments throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void postFixedAssetsInvestmentsApiErrorResponseExceptionNotFound() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();

        FixedAssetsInvestments fixedAssetsInvestments = createFixedAssetsInvestments();
        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = createFixedAssetsInvestmentsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutFixedAssetsInvestments(smallFullApi);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(mockFixedAssetsInvestmentsTransformer.getFixedAssetsInvestmentsApi(fixedAssetsInvestments)).thenReturn(fixedAssetsInvestmentsApi);
        when(mockFixedAssetsInvestmentsResourceHandler.create(FIXED_ASSETS_INVESTMENTS_URI, fixedAssetsInvestmentsApi)).thenReturn(mockFixedAssetsInvestmentsCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(
            404,"Not Found",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockFixedAssetsInvestmentsCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockFixedAssetsInvestmentsCreate.execute());
        assertThrows(ServiceException.class, () -> fixedAssetsInvestmentsService.submitFixedAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            fixedAssetsInvestments,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - fixedAssetsInvestments throws ServiceExcepiton due to ApiErrorResponseException - 400 Bad Request")
    void postFixedAssetsInvestmentsApiErrorResponseExceptionBadRequest() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();

        FixedAssetsInvestments fixedAssetsInvestments = createFixedAssetsInvestments();
        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = createFixedAssetsInvestmentsApi();

        SmallFullApi smallFullApi = new SmallFullApi();
        setLinksWithoutFixedAssetsInvestments(smallFullApi);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(mockFixedAssetsInvestmentsTransformer.getFixedAssetsInvestmentsApi(fixedAssetsInvestments)).thenReturn(fixedAssetsInvestmentsApi);
        when(mockFixedAssetsInvestmentsResourceHandler.create(FIXED_ASSETS_INVESTMENTS_URI, fixedAssetsInvestmentsApi)).thenReturn(mockFixedAssetsInvestmentsCreate);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(
            400,"Bad Request",new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException =
            ApiErrorResponseException.fromHttpResponseException(httpResponseException);
        when(mockFixedAssetsInvestmentsCreate.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ApiErrorResponseException.class, () -> mockFixedAssetsInvestmentsCreate.execute());
        assertThrows(ServiceException.class, () -> fixedAssetsInvestmentsService.submitFixedAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            fixedAssetsInvestments,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - fixedAssetsInvestments throws ServiceExcepiton getting Smallfull data")
    void postFixedAssetsInvestmentsGetSmallFullDataApiResponseException() throws Exception {

        FixedAssetsInvestments fixedAssetsInvestments = createFixedAssetsInvestments();
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);

        when(mockSmallFullService.getSmallFullAccounts(mockApiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenThrow(ServiceException.class);

        assertThrows(ServiceException.class, () -> fixedAssetsInvestmentsService.submitFixedAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            fixedAssetsInvestments,
            COMPANY_NUMBER));
    }
    
    private void getMockFixedAssetsInvestmentsApi(FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi) throws Exception {
        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.get(FIXED_ASSETS_INVESTMENTS_URI)).thenReturn(mockFixedAssetsInvestmentsGet);
        when(mockFixedAssetsInvestmentsGet.execute()).thenReturn(fixedAssetsInvestmentsApi);
    }

    private void getMockFixedAssetsInvestmentsResourceHandler() {
        getMockSmallFullResourceHandler();
        when(mockSmallFullResourceHandler.fixedAssetsInvestments()).thenReturn(mockFixedAssetsInvestmentsResourceHandler);
    }

    private void getMockSmallFullResourceHandler() {
        when(mockApiClientService.getApiClient()).thenReturn(mockApiClient);
        when(mockApiClient.smallFull()).thenReturn(mockSmallFullResourceHandler);
    }

    private void fixedAssetsInvestmentsCreate(FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi) throws Exception {
        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.create(FIXED_ASSETS_INVESTMENTS_URI, fixedAssetsInvestmentsApi)).thenReturn(mockFixedAssetsInvestmentsCreate);
        when(mockFixedAssetsInvestmentsCreate.execute()).thenReturn(fixedAssetsInvestmentsApi);
    }

    private void setLinksWithoutFixedAssetsInvestments(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setFixedAssetsInvestmentsNote(null);
        smallFullApi.setLinks(links);
    }

    private FixedAssetsInvestments createFixedAssetsInvestments() {
        FixedAssetsInvestments fixedAssetsInvestments = new FixedAssetsInvestments();
        fixedAssetsInvestments.setDetails(TEST_DETAILS);
        return fixedAssetsInvestments;
    }

    private FixedAssetsInvestmentsApi createFixedAssetsInvestmentsApi() {
        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = new FixedAssetsInvestmentsApi();
        fixedAssetsInvestmentsApi.setDetails(TEST_DETAILS);
        return fixedAssetsInvestmentsApi;
    }
}
