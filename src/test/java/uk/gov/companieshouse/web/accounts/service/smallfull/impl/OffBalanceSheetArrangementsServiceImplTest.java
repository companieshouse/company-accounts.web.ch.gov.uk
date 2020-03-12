package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.OffBalanceSheetResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetCreate;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetDelete;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetGet;
import uk.gov.companieshouse.api.handler.smallfull.offBalanceSheet.request.OffBalanceSheetUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.offBalanceSheet.OffBalanceSheetApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.smallfull.notes.offbalancesheetarrangements.OffBalanceSheetArrangements;
import uk.gov.companieshouse.web.accounts.service.smallfull.OffBalanceSheetArrangementsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.OffBalanceSheetArrangementsTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OffBalanceSheetArrangementsServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private OffBalanceSheetApi offBalanceSheetApi;

    @Mock
    private OffBalanceSheetArrangements offBalanceSheetArrangements;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private OffBalanceSheetResourceHandler offBalanceSheetResourceHandler;

    @Mock
    private OffBalanceSheetArrangementsTransformer offBalanceSheetArrangementsTransformer;

    @Mock
    private OffBalanceSheetGet offBalanceSheetGet;

    @Mock
    private OffBalanceSheetCreate offBalanceSheetCreate;

    @Mock
    private OffBalanceSheetUpdate offBalanceSheetUpdate;

    @Mock
    private OffBalanceSheetDelete offBalanceSheetDelete;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiResponse<OffBalanceSheetApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private List<ValidationError> validationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private OffBalanceSheetArrangementsService offBalanceSheetArrangementsService = new OffBalanceSheetArrangementsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                                                                     COMPANY_ACCOUNTS_ID + "/small-full/notes/off-balance-sheet-arrangements";

    private static final String RESOURCE_NAME = "off balance sheet arrangements";

    private static final String OFF_BALANCE_SHEET_ARRANGEMENTS_LINK = "offBalanceSheetArrangementsLink";

    @BeforeEach
    private void setUp() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.offBalanceSheet()).thenReturn(offBalanceSheetResourceHandler);
    }

    @Test
    @DisplayName("Get off balance sheet arrangements - success")
    void getOffBalanceSheetArrangementsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(offBalanceSheetResourceHandler.get(OFF_BALANCE_SHEET_ARRANGEMENTS_URI))
                .thenReturn(offBalanceSheetGet);

        when(offBalanceSheetGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(offBalanceSheetApi);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangements(offBalanceSheetApi))
                .thenReturn(offBalanceSheetArrangements);

        OffBalanceSheetArrangements returned =
                offBalanceSheetArrangementsService
                        .getOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(offBalanceSheetArrangements, returned);
    }

    @Test
    @DisplayName("Get off balance sheet arrangements - ApiErrorResponseException")
    void getOffBalanceSheetArrangementsThrowsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(offBalanceSheetResourceHandler.get(OFF_BALANCE_SHEET_ARRANGEMENTS_URI))
                .thenReturn(offBalanceSheetGet);

        when(offBalanceSheetGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .getOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get off balance sheet arrangements - URIValidationException")
    void getOffBalanceSheetArrangementsThrowsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(offBalanceSheetResourceHandler.get(OFF_BALANCE_SHEET_ARRANGEMENTS_URI))
                .thenReturn(offBalanceSheetGet);

        when(offBalanceSheetGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .getOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - create - success")
    void submitOffBalanceSheetArrangementsCreateSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(null);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.create(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetCreate);

        when(offBalanceSheetCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> returned =
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements);

        assertTrue(returned.isEmpty());
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - create - validation errors")
    void submitOffBalanceSheetArrangementsCreateValidationErrors()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(null);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.create(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetCreate);

        when(offBalanceSheetCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(validationErrors);

        List<ValidationError> returned =
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements);

        assertEquals(validationErrors, returned);
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - create - ApiErrorResponseException")
    void submitOffBalanceSheetArrangementsCreateThrowsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(null);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.create(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetCreate);

        when(offBalanceSheetCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - create - URIValidationException")
    void submitOffBalanceSheetArrangementsCreateThrowsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(null);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.create(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetCreate);

        when(offBalanceSheetCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - update - success")
    void submitOffBalanceSheetArrangementsUpdateSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_LINK);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.update(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetUpdate);

        when(offBalanceSheetUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> returned =
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements);

        assertTrue(returned.isEmpty());
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - update - validation errors")
    void submitOffBalanceSheetArrangementsUpdateValidationErrors()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_LINK);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.update(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetUpdate);

        when(offBalanceSheetUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(validationErrors);

        List<ValidationError> returned =
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements);

        assertEquals(validationErrors, returned);
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - update - ApiErrorResponseException")
    void submitOffBalanceSheetArrangementsUpdateThrowsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_LINK);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.update(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetUpdate);

        when(offBalanceSheetUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements));
    }

    @Test
    @DisplayName("Submit off balance sheet arrangements - update - URIValidationException")
    void submitOffBalanceSheetArrangementsUpdateThrowsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_LINK);

        when(offBalanceSheetArrangementsTransformer.getOffBalanceSheetArrangementsApi(offBalanceSheetArrangements))
                .thenReturn(offBalanceSheetApi);

        when(offBalanceSheetResourceHandler.update(OFF_BALANCE_SHEET_ARRANGEMENTS_URI, offBalanceSheetApi))
                .thenReturn(offBalanceSheetUpdate);

        when(offBalanceSheetUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .submitOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, offBalanceSheetArrangements));
    }

    @Test
    @DisplayName("Delete off balance sheet arrangements - success")
    void deleteOffBalanceSheetArrangementsSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_LINK);

        when(offBalanceSheetResourceHandler.delete(OFF_BALANCE_SHEET_ARRANGEMENTS_URI))
                .thenReturn(offBalanceSheetDelete);

        assertAll(() ->
                offBalanceSheetArrangementsService
                        .deleteOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(offBalanceSheetDelete).execute();
    }

    @Test
    @DisplayName("Delete off balance sheet arrangements - ApiErrorResponseException")
    void deleteOffBalanceSheetArrangementsThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_LINK);

        when(offBalanceSheetResourceHandler.delete(OFF_BALANCE_SHEET_ARRANGEMENTS_URI))
                .thenReturn(offBalanceSheetDelete);

        when(offBalanceSheetDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .deleteOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Delete off balance sheet arrangements - URIValidationException")
    void deleteOffBalanceSheetArrangementsThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID))
                .thenReturn(smallFullApi);

        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);

        when(smallFullLinks.getOffBalanceSheetArrangements()).thenReturn(OFF_BALANCE_SHEET_ARRANGEMENTS_LINK);

        when(offBalanceSheetResourceHandler.delete(OFF_BALANCE_SHEET_ARRANGEMENTS_URI))
                .thenReturn(offBalanceSheetDelete);

        when(offBalanceSheetDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                offBalanceSheetArrangementsService
                        .deleteOffBalanceSheetArrangements(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
