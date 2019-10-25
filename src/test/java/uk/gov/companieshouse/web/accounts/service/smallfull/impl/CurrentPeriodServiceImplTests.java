package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.handler.smallfull.SmallFullResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.CurrentPeriodResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodCreate;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodGet;
import uk.gov.companieshouse.api.handler.smallfull.currentperiod.request.CurrentPeriodUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.CurrentPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.CurrentPeriodService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CurrentPeriodServiceImplTests {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private CurrentPeriodResourceHandler currentPeriodResourceHandler;

    @Mock
    private CurrentPeriodGet currentPeriodGet;

    @Mock
    private CurrentPeriodCreate currentPeriodCreate;

    @Mock
    private CurrentPeriodUpdate currentPeriodUpdate;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiResponse<CurrentPeriodApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private CurrentPeriodApi currentPeriod;

    @Mock
    private SmallFullApi smallFull;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private ServiceException serviceException;

    @Mock
    private List<ValidationError> existingValidationErrors;

    @Mock
    private List<ValidationError> submissionValidationErrors;

    @Mock
    private List<ApiError> apiErrors;

    @InjectMocks
    private CurrentPeriodService currentPeriodService = new CurrentPeriodServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String CURRENT_PERIOD_URI = "/transactions/" + TRANSACTION_ID +
                                                        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                        "/small-full/current-period";

    private static final String CURRENT_PERIOD_LINK = "currentPeriodLink";

    private static final String CURRENT_PERIOD_RESOURCE = "current period";

    @BeforeEach
    private void init() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.currentPeriod()).thenReturn(currentPeriodResourceHandler);
    }

    @Test
    @DisplayName("Get current period - success")
    void getCurrentPeriodSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(currentPeriodResourceHandler.get(CURRENT_PERIOD_URI)).thenReturn(currentPeriodGet);
        when(currentPeriodGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(currentPeriod);

        CurrentPeriodApi returnedCurrentPeriod =
                currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returnedCurrentPeriod);
        assertEquals(currentPeriod, returnedCurrentPeriod);
    }

    @Test
    @DisplayName("Get current period - not found")
    void getCurrentPeriodNotFound() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(currentPeriodResourceHandler.get(CURRENT_PERIOD_URI)).thenReturn(currentPeriodGet);
        when(currentPeriodGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, CURRENT_PERIOD_RESOURCE);

        assertNull(currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get current period - api error response exception")
    void getCurrentPeriodApiErrorResponseException() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(currentPeriodResourceHandler.get(CURRENT_PERIOD_URI)).thenReturn(currentPeriodGet);
        when(currentPeriodGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleRetrievalException(apiErrorResponseException, CURRENT_PERIOD_RESOURCE);

        assertThrows(ServiceException.class,
                () -> currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get current period - uri validation exception")
    void getCurrentPeriodURIValidationException() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(currentPeriodResourceHandler.get(CURRENT_PERIOD_URI)).thenReturn(currentPeriodGet);
        when(currentPeriodGet.execute()).thenThrow(uriValidationException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, CURRENT_PERIOD_RESOURCE);

        assertThrows(ServiceException.class,
                () -> currentPeriodService.getCurrentPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create current period - success")
    void createCurrentPeriodSuccess() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(null);

        when(currentPeriodResourceHandler.create(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodCreate);
        when(currentPeriodCreate.execute()).thenReturn(responseWithData);
        when(responseWithData.hasErrors()).thenReturn(false);

        assertAll(() ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));

        verify(existingValidationErrors, never()).addAll(anyList());
    }

    @Test
    @DisplayName("Create current period - validation errors")
    void createCurrentValidationErrors() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(null);

        when(currentPeriodResourceHandler.create(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodCreate);
        when(currentPeriodCreate.execute()).thenReturn(responseWithData);
        when(responseWithData.hasErrors()).thenReturn(true);

        when(responseWithData.getErrors()).thenReturn(apiErrors);
        when(validationContext.getValidationErrors(apiErrors)).thenReturn(
                submissionValidationErrors);

        assertAll(() ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));

        verify(existingValidationErrors).addAll(submissionValidationErrors);
    }

    @Test
    @DisplayName("Create current period - api error response exception")
    void createCurrentPeriodApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(null);

        when(currentPeriodResourceHandler.create(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodCreate);
        when(currentPeriodCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, CURRENT_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));
    }

    @Test
    @DisplayName("Create current period - uri validation exception")
    void createCurrentPeriodURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(null);

        when(currentPeriodResourceHandler.create(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodCreate);
        when(currentPeriodCreate.execute()).thenThrow(uriValidationException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, CURRENT_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));
    }

    @Test
    @DisplayName("Update current period - success")
    void updateCurrentPeriodSuccess() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(CURRENT_PERIOD_LINK);

        when(currentPeriodResourceHandler.update(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodUpdate);
        when(currentPeriodUpdate.execute()).thenReturn(responseNoData);
        when(responseNoData.hasErrors()).thenReturn(false);

        assertAll(() ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));

        verify(existingValidationErrors, never()).addAll(anyList());
    }

    @Test
    @DisplayName("Update current period - validation errors")
    void updateCurrentValidationErrors() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(CURRENT_PERIOD_LINK);

        when(currentPeriodResourceHandler.update(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodUpdate);
        when(currentPeriodUpdate.execute()).thenReturn(responseNoData);
        when(responseNoData.hasErrors()).thenReturn(true);

        when(responseNoData.getErrors()).thenReturn(apiErrors);
        when(validationContext.getValidationErrors(apiErrors)).thenReturn(
                submissionValidationErrors);

        assertAll(() ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));

        verify(existingValidationErrors).addAll(submissionValidationErrors);
    }

    @Test
    @DisplayName("Update current period - api error response exception")
    void updateCurrentPeriodApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(CURRENT_PERIOD_LINK);

        when(currentPeriodResourceHandler.update(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodUpdate);
        when(currentPeriodUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, CURRENT_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));
    }

    @Test
    @DisplayName("Update current period - uri validation exception")
    void updateCurrentPeriodURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getCurrentPeriod()).thenReturn(CURRENT_PERIOD_LINK);

        when(currentPeriodResourceHandler.update(CURRENT_PERIOD_URI, currentPeriod)).thenReturn(currentPeriodUpdate);
        when(currentPeriodUpdate.execute()).thenThrow(uriValidationException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, CURRENT_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                currentPeriodService
                        .submitCurrentPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                currentPeriod, existingValidationErrors));
    }
}