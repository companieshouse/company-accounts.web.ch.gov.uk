package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponseException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.tangible.TangibleResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleCreate;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleGet;
import uk.gov.companieshouse.api.handler.smallfull.tangible.request.TangibleUpdate;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.tangible.TangibleApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.tangible.TangibleAssets;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.service.smallfull.TangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.tangible.TangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TangibleAssetsNoteServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private TangibleApi tangibleApi;

    @Mock
    private TangibleAssets tangibleAssets;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private TangibleResourceHandler tangibleResourceHandler;

    @Mock
    private TangibleAssetsTransformer tangibleAssetsTransformer;

    @Mock
    private TangibleGet tangibleGet;

    @Mock
    private TangibleCreate tangibleCreate;

    @Mock
    private TangibleUpdate tangibleUpdate;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullLinks smallFullLinks;

    @InjectMocks
    private TangibleAssetsNoteService tangibleAssetsNoteService = new TangibleAssetsNoteServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String SMALL_FULL_URI = "/transactions/transactionId/company-accounts/companyAccountsId/small-full";

    @BeforeEach
    private void setUp() {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.tangible()).thenReturn(tangibleResourceHandler);
    }

    @Test
    @DisplayName("GET - Tangible successful path")
    void getTangibleSuccess() throws Exception {

        when(tangibleResourceHandler.get(anyString())).thenReturn(tangibleGet);
        when(tangibleGet.execute()).thenReturn(tangibleApi);
        when(tangibleAssetsTransformer.getTangibleAssets(tangibleApi)).thenReturn(tangibleAssets);

        TangibleAssets testResult = tangibleAssetsNoteService
            .getTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(testResult);
    }

    @Test
    @DisplayName("GET - Tangible successful path when http status not found")
    void getTangibleSuccessHttpStatusNotFound() throws Exception {

        when(tangibleResourceHandler.get(anyString())).thenReturn(tangibleGet);

        HttpResponseException httpResponseException = new HttpResponseException.Builder(
            HttpStatus.NOT_FOUND.value(), "", new HttpHeaders()).build();
        ApiErrorResponseException apiErrorResponseException = ApiErrorResponseException
            .fromHttpResponseException(httpResponseException);

        doThrow(apiErrorResponseException).when(tangibleGet).execute();

        TangibleAssets testResult = tangibleAssetsNoteService
            .getTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(testResult);
    }

    @Test
    @DisplayName("GET - Tangible successful path when service exception is thrown")
    void getTangibleServiceException() throws Exception {

        when(tangibleResourceHandler.get(anyString())).thenReturn(tangibleGet);

        doThrow(ApiErrorResponseException.class).when(tangibleGet).execute();

        assertThrows(ServiceException.class, () -> tangibleAssetsNoteService.getTangibleAssets(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Tangible successful path create")
    void postTangibleSuccessCreate() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn(null);

        when(tangibleResourceHandler.create(
            "/transactions/transactionId/company-accounts/companyAccountsId/small-full/notes/tangible-assets",
            null)).thenReturn(tangibleCreate);

        List<ValidationError> testResult = tangibleAssetsNoteService
            .postTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets,
                COMPANY_NUMBER);

        verify(tangibleCreate, times(1)).execute();
        assertTrue(testResult.isEmpty());
        assertNotNull(testResult);
    }

    @Test
    @DisplayName("POST - Tangible successful path update")
    void postTangibleSuccessUpdate() throws Exception {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getTangibleAssetsNote()).thenReturn("");

        when(tangibleResourceHandler.update(
            "/transactions/transactionId/company-accounts/companyAccountsId/small-full/notes/tangible-assets",
            null)).thenReturn(tangibleUpdate);

        List<ValidationError> testResult = tangibleAssetsNoteService
            .postTangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, tangibleAssets,
                COMPANY_NUMBER);

        verify(tangibleUpdate, times(1)).execute();
        assertTrue(testResult.isEmpty());
        assertNotNull(testResult);
    }
}