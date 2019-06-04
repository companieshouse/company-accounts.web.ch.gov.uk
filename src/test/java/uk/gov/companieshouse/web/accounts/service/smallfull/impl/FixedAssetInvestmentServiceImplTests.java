package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

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
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.fixedassetsinvestments.FixedAssetsInvestmentsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.fixedassetsinvestments.FixedAssetsInvestments;
import uk.gov.companieshouse.web.accounts.service.smallfull.FixedAssetsInvestmentsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.FixedAssetsInvestmentsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiResponse<FixedAssetsInvestmentsApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private FixedAssetsInvestmentsService fixedAssetsInvestmentsService = new FixedAssetsInvestmentsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String FIXED_ASSETS_INVESTMENTS_URI = "/transactions/" + TRANSACTION_ID +
        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
        "/small-full/notes/fixed-assets-investments";
    
    private static final String TEST_DETAILS = "details text";

    private static final String RESOURCE_NAME = "fixed assets investments";

    @Test
    @DisplayName("GET - fixedAssetsInvestments successful path")
    void getFixedAssetsInvestmentsSuccess() throws Exception {

        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = new FixedAssetsInvestmentsApi();
        getMockFixedAssetsInvestmentsApi(fixedAssetsInvestmentsApi);

        when(mockFixedAssetsInvestmentsTransformer.getFixedAssetsInvestments(fixedAssetsInvestmentsApi)).thenReturn(createFixedAssetsInvestments());

        FixedAssetsInvestments fixedAssetsInvestments = fixedAssetsInvestmentsService.getFixedAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(fixedAssetsInvestments);
        assertEquals(TEST_DETAILS, fixedAssetsInvestments.getFixedAssetsDetails());
    }

    @Test
    @DisplayName("GET - fixedAssetsInvestments successful path when http status not found")
    void getFixedAssetsInvestmentsSuccessHttpStatusNotFound() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.get(FIXED_ASSETS_INVESTMENTS_URI)).
            thenReturn(mockFixedAssetsInvestmentsGet);
        when(mockFixedAssetsInvestmentsGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        when(mockFixedAssetsInvestmentsTransformer.getFixedAssetsInvestments(null))
            .thenReturn(createFixedAssetsInvestments());

        FixedAssetsInvestments fixedAssetsInvestments =
            fixedAssetsInvestmentsService.getFixedAssetsInvestments(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(fixedAssetsInvestments);
        assertEquals(TEST_DETAILS, fixedAssetsInvestments.getFixedAssetsDetails());
    }

    @Test
    @DisplayName("GET - fixedAssetsInvestments throws ServiceException due to " +
        "ApiErrorResponseException - 400 Bad Request")
    void getFixedAssetsInvestmentsApiResponseException() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.get(FIXED_ASSETS_INVESTMENTS_URI))
            .thenReturn(mockFixedAssetsInvestmentsGet);
        when(mockFixedAssetsInvestmentsGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
            .when(serviceExceptionHandler)
            .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

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

        when(mockFixedAssetsInvestmentsGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
            .when(serviceExceptionHandler)
            .handleURIValidationException(uriValidationException, RESOURCE_NAME);

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

        when(responseWithData.hasErrors()).thenReturn(false);

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

        when(mockFixedAssetsInvestmentsCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

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

        when(mockFixedAssetsInvestmentsCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(mockValidationContext.getValidationErrors(responseWithData.getErrors()))
            .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = fixedAssetsInvestmentsService.submitFixedAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            fixedAssetsInvestments,
            COMPANY_NUMBER);

        assertEquals(mockValidationErrors, validationErrors);
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

    @Test
    @DisplayName("DELETE - Investments successful delete path")
    void deleteInvestments() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.delete(FIXED_ASSETS_INVESTMENTS_URI)).thenReturn(mockFixedAssetsInvestmentsDelete);

        fixedAssetsInvestmentsService.deleteFixedAssetsInvestments(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        verify(mockFixedAssetsInvestmentsDelete, times(1)).execute();
    }

    @Test
    @DisplayName("DELETE - Investments throws ServiceExcepiton due to URIValidationException")
    void deleteInvestmentsUriValidationException() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.delete(FIXED_ASSETS_INVESTMENTS_URI)).thenReturn(mockFixedAssetsInvestmentsDelete);

        when(mockFixedAssetsInvestmentsDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
            .when(serviceExceptionHandler)
            .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> fixedAssetsInvestmentsService.deleteFixedAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Investments throws ServiceExcepiton due to ApiErrorResponseException - 404 Not Found")
    void deleteInvestmentsApiErrorResponseExceptionNotFound() throws Exception {

        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.delete(FIXED_ASSETS_INVESTMENTS_URI)).thenReturn(mockFixedAssetsInvestmentsDelete);

        when(mockFixedAssetsInvestmentsDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
            .when(serviceExceptionHandler)
            .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> fixedAssetsInvestmentsService.deleteFixedAssetsInvestments(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID));
    }
    
    private void getMockFixedAssetsInvestmentsApi(FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi) throws Exception {
        getMockFixedAssetsInvestmentsResourceHandler();
        when(mockFixedAssetsInvestmentsResourceHandler.get(FIXED_ASSETS_INVESTMENTS_URI)).thenReturn(mockFixedAssetsInvestmentsGet);
        when(mockFixedAssetsInvestmentsGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(fixedAssetsInvestmentsApi);
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
        when(mockFixedAssetsInvestmentsCreate.execute()).thenReturn(responseWithData);
    }

    private void setLinksWithoutFixedAssetsInvestments(SmallFullApi smallFullApi) {
        SmallFullLinks links = new SmallFullLinks();
        links.setFixedAssetsInvestmentsNote(null);
        smallFullApi.setLinks(links);
    }

    private FixedAssetsInvestments createFixedAssetsInvestments() {
        FixedAssetsInvestments fixedAssetsInvestments = new FixedAssetsInvestments();
        fixedAssetsInvestments.setFixedAssetsDetails(TEST_DETAILS);
        return fixedAssetsInvestments;
    }

    private FixedAssetsInvestmentsApi createFixedAssetsInvestmentsApi() {
        FixedAssetsInvestmentsApi fixedAssetsInvestmentsApi = new FixedAssetsInvestmentsApi();
        fixedAssetsInvestmentsApi.setDetails(TEST_DETAILS);
        return fixedAssetsInvestmentsApi;
    }
}
