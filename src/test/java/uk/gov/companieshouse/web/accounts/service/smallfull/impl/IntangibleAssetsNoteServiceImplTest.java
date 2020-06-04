package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.junit.jupiter.api.BeforeEach;
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
import uk.gov.companieshouse.api.handler.smallfull.intangible.IntangibleResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleCreate;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleDelete;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleGet;
import uk.gov.companieshouse.api.handler.smallfull.intangible.request.IntangibleUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.AccountingPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.intangible.IntangibleApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.intangible.IntangibleAssets;
import uk.gov.companieshouse.web.accounts.service.smallfull.IntangibleAssetsNoteService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.intangible.IntangibleAssetsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntangibleAssetsNoteServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private IntangibleApi intangibleApi;

    @Mock
    private IntangibleAssets intangibleAssets;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private IntangibleResourceHandler intangibleResourceHandler;

    @Mock
    private IntangibleAssetsTransformer intangibleAssetsTransformer;

    @Mock
    private IntangibleGet intangibleGet;

    @Mock
    private IntangibleCreate intangibleCreate;

    @Mock
    private IntangibleUpdate intangibleUpdate;

    @Mock
    private IntangibleDelete intangibleDelete;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private AccountingPeriodApi accountingPeriodApi;

    @Mock
    private AccountingPeriodApi lastAccountingPeriodApi;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiResponse<IntangibleApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private IntangibleAssetsNoteService intangibleAssetsNoteService = new IntangibleAssetsNoteServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String INTANGIBLE_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                    COMPANY_ACCOUNTS_ID + "/small-full/notes/intangible-assets";

    private static final LocalDate LAST_PERIOD_END_ON = LocalDate.parse("2017-06-30");

    private static final LocalDate NEXT_PERIOD_START_ON = LocalDate.parse("2017-07-01");

    private static final LocalDate NEXT_PERIOD_END_ON = LocalDate.parse("2018-06-30");

    private static final String RESOURCE_NAME = "intangible assets";

    @BeforeEach
    private void setUp() {
        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.intangible()).thenReturn(intangibleResourceHandler);
    }

    @Test
    @DisplayName("GET - Intangible successful path")
    void getIntangibleSuccess() throws Exception {

        when(intangibleResourceHandler.get(INTANGIBLE_URI)).thenReturn(intangibleGet);

        when(intangibleGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(intangibleApi);

        when(intangibleAssetsTransformer.getIntangibleAssets(intangibleApi)).thenReturn(new IntangibleAssets());

        smallFullServiceAccountsDate();

        IntangibleAssets testResult = intangibleAssetsNoteService
            .getIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(testResult);
        assertCompanyDatesSetOnIntangibleAssets(testResult);
    }

    @Test
    @DisplayName("GET - Intangible successful path when http status not found")
    void getIntangibleSuccessHttpStatusNotFound() throws Exception {

        when(intangibleResourceHandler.get(INTANGIBLE_URI)).thenReturn(intangibleGet);

        when(intangibleGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);


        smallFullServiceAccountsDate();

        IntangibleAssets testResult = intangibleAssetsNoteService
            .getIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, COMPANY_NUMBER);

        assertNotNull(testResult);
        assertCompanyDatesSetOnIntangibleAssets(testResult);
    }

    @Test
    @DisplayName("GET - Intangible ApiErrorResponseException is thrown")
    void getIntangibleApiErrorResponseException() throws Exception {

        when(intangibleResourceHandler.get(INTANGIBLE_URI)).thenReturn(intangibleGet);

        when(intangibleGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> intangibleAssetsNoteService.getIntangibleAssets(
            TRANSACTION_ID,
            COMPANY_ACCOUNTS_ID,
            COMPANY_NUMBER));
    }

    @Test
    @DisplayName("GET - Intangible URIValidationException is thrown")
    void getIntangibleURIValidationException() throws Exception {

        when(intangibleResourceHandler.get(INTANGIBLE_URI)).thenReturn(intangibleGet);

        when(intangibleGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> intangibleAssetsNoteService.getIntangibleAssets(
                TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID,
                COMPANY_NUMBER));
    }

    @Test
    @DisplayName("POST - Intangible successful path")
    void postIntangibleSuccess() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getIntangibleAssetsNote()).thenReturn(null);

        when(intangibleAssetsTransformer.getIntangibleApi(intangibleAssets)).thenReturn(intangibleApi);

        when(intangibleResourceHandler.create(INTANGIBLE_URI, intangibleApi)).thenReturn(intangibleCreate);

        when(intangibleCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors = intangibleAssetsNoteService
            .postIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAssets,
                COMPANY_NUMBER);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - Intangible with validation errors")
    void postIntangibleWithValidationErrors() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getIntangibleAssetsNote()).thenReturn(null);

        when(intangibleAssetsTransformer.getIntangibleApi(intangibleAssets)).thenReturn(intangibleApi);

        when(intangibleResourceHandler.create(INTANGIBLE_URI, intangibleApi)).thenReturn(intangibleCreate);

        when(intangibleCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = intangibleAssetsNoteService
                .postIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAssets,
                        COMPANY_NUMBER);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("PUT - Intangible successful path")
    void putIntangibleSuccess() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
            .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getIntangibleAssetsNote()).thenReturn("");

        when(intangibleAssetsTransformer.getIntangibleApi(intangibleAssets)).thenReturn(intangibleApi);

        when(intangibleResourceHandler.update(INTANGIBLE_URI, intangibleApi)).thenReturn(intangibleUpdate);

        when(intangibleUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors = intangibleAssetsNoteService
            .postIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAssets,
                COMPANY_NUMBER);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("PUT - Intangible with validation errors")
    void putIntangibleWithValidationErrors() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getIntangibleAssetsNote()).thenReturn("");

        when(intangibleAssetsTransformer.getIntangibleApi(intangibleAssets)).thenReturn(intangibleApi);

        when(intangibleResourceHandler.update(INTANGIBLE_URI, intangibleApi)).thenReturn(intangibleUpdate);

        when(intangibleUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors = intangibleAssetsNoteService
                .postIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAssets,
                        COMPANY_NUMBER);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("DELETE - Intangible successful path")
    void deleteIntangibleSuccess() {

        when(intangibleResourceHandler.delete(INTANGIBLE_URI)).thenReturn(intangibleDelete);

        assertAll(() -> intangibleAssetsNoteService
                .deleteIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Intangible failure path due to a thrown ApiErrorResponseException")
    void deleteIntangibleApiErrorResponseException() throws Exception {

        when(intangibleResourceHandler.delete(INTANGIBLE_URI)).thenReturn(intangibleDelete);

        when(intangibleDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> intangibleAssetsNoteService
                .deleteIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - Intangible failure path due to a thrown URIValidationException")
    void deleteIntangibleURIValidationException() throws Exception {

        when(intangibleResourceHandler.delete(INTANGIBLE_URI)).thenReturn(intangibleDelete);

        when(intangibleDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> intangibleAssetsNoteService
                .deleteIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("CREATE - Intangible failure path due to thrown URIValidationException")
    void createIntangibleURIValidationException() throws Exception {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(intangibleAssetsTransformer.getIntangibleApi(intangibleAssets)).thenReturn(intangibleApi);

        when(intangibleResourceHandler.create(INTANGIBLE_URI, intangibleApi)).thenReturn(intangibleCreate);

        when(intangibleCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> intangibleAssetsNoteService
                .postIntangibleAssets(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, intangibleAssets, COMPANY_NUMBER));
    }

    private void assertCompanyDatesSetOnIntangibleAssets(IntangibleAssets intangibleAssets) {

        assertEquals(NEXT_PERIOD_START_ON, intangibleAssets.getNextAccountsPeriodStartOn());
        assertEquals(NEXT_PERIOD_END_ON, intangibleAssets.getNextAccountsPeriodEndOn());
        assertEquals(LAST_PERIOD_END_ON, intangibleAssets.getLastAccountsPeriodEndOn());
    }

    private void smallFullServiceAccountsDate() throws ServiceException {
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getNextAccounts()).thenReturn(accountingPeriodApi);
        when(accountingPeriodApi.getPeriodStartOn()).thenReturn(NEXT_PERIOD_START_ON);
        when(accountingPeriodApi.getPeriodEndOn()).thenReturn(NEXT_PERIOD_END_ON);
        when(smallFullApi.getLastAccounts()).thenReturn(lastAccountingPeriodApi);
        when(lastAccountingPeriodApi.getPeriodEndOn()).thenReturn(LAST_PERIOD_END_ON);
    }
}