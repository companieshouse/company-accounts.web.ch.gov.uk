package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
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
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.PreviousPeriodResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.request.PreviousPeriodCreate;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.request.PreviousPeriodGet;
import uk.gov.companieshouse.api.handler.smallfull.previousperiod.request.PreviousPeriodUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.PreviousPeriodApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.PreviousPeriodService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PreviousPeriodServiceImplTests {

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private PreviousPeriodResourceHandler previousPeriodResourceHandler;

    @Mock
    private PreviousPeriodGet previousPeriodGet;

    @Mock
    private PreviousPeriodCreate previousPeriodCreate;

    @Mock
    private PreviousPeriodUpdate previousPeriodUpdate;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiResponse<PreviousPeriodApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private PreviousPeriodApi previousPeriod;

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
    private PreviousPeriodService previousPeriodService = new PreviousPeriodServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String PREVIOUS_PERIOD_URI = "/transactions/" + TRANSACTION_ID +
                                                        "/company-accounts/" + COMPANY_ACCOUNTS_ID +
                                                        "/small-full/previous-period";

    private static final String PREVIOUS_PERIOD_LINK = "previousPeriodLink";

    private static final String PREVIOUS_PERIOD_RESOURCE = "previous period";

    @BeforeEach
    private void init() {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.previousPeriod()).thenReturn(previousPeriodResourceHandler);
    }

    @Test
    @DisplayName("Get previous period - success")
    void getPreviousPeriodSuccess() throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(previousPeriodResourceHandler.get(PREVIOUS_PERIOD_URI)).thenReturn(previousPeriodGet);
        when(previousPeriodGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(previousPeriod);

        PreviousPeriodApi returnedPreviousPeriod =
                previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(returnedPreviousPeriod);
        assertEquals(previousPeriod, returnedPreviousPeriod);
    }

    @Test
    @DisplayName("Get previous period - api error response exception")
    void getPreviousPeriodApiErrorResponseException() throws ApiErrorResponseException, URIValidationException {

        when(previousPeriodResourceHandler.get(PREVIOUS_PERIOD_URI)).thenReturn(previousPeriodGet);
        when(previousPeriodGet.execute()).thenThrow(apiErrorResponseException);

        assertThrows(ServiceException.class,
                () -> previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get previous period - uri validation exception")
    void getPreviousPeriodURIValidationException() throws ApiErrorResponseException, URIValidationException {

        when(previousPeriodResourceHandler.get(PREVIOUS_PERIOD_URI)).thenReturn(previousPeriodGet);
        when(previousPeriodGet.execute()).thenThrow(uriValidationException);

        assertThrows(ServiceException.class,
                () -> previousPeriodService.getPreviousPeriod(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create previous period - success")
    void createPreviousPeriodSuccess() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(null);

        when(previousPeriodResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodCreate);
        when(previousPeriodCreate.execute()).thenReturn(responseWithData);
        when(responseWithData.hasErrors()).thenReturn(false);

        assertAll(() ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));

        verify(existingValidationErrors, never()).addAll(anyList());
    }

    @Test
    @DisplayName("Create previous period - validation errors")
    void createPreviousValidationErrors() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(null);

        when(previousPeriodResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodCreate);
        when(previousPeriodCreate.execute()).thenReturn(responseWithData);
        when(responseWithData.hasErrors()).thenReturn(true);

        when(responseWithData.getErrors()).thenReturn(apiErrors);
        when(validationContext.getValidationErrors(apiErrors)).thenReturn(
                submissionValidationErrors);

        assertAll(() ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));

        verify(existingValidationErrors).addAll(submissionValidationErrors);
    }

    @Test
    @DisplayName("Create previous period - api error response exception")
    void createPreviousPeriodApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(null);

        when(previousPeriodResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodCreate);
        when(previousPeriodCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, PREVIOUS_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));
    }

    @Test
    @DisplayName("Create previous period - uri validation exception")
    void createPreviousPeriodURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(null);

        when(previousPeriodResourceHandler.create(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodCreate);
        when(previousPeriodCreate.execute()).thenThrow(uriValidationException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, PREVIOUS_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));
    }

    @Test
    @DisplayName("Update previous period - success")
    void updatePreviousPeriodSuccess() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(PREVIOUS_PERIOD_LINK);

        when(previousPeriodResourceHandler.update(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodUpdate);
        when(previousPeriodUpdate.execute()).thenReturn(responseNoData);
        when(responseNoData.hasErrors()).thenReturn(false);

        assertAll(() ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));

        verify(existingValidationErrors, never()).addAll(anyList());
    }

    @Test
    @DisplayName("Update previous period - validation errors")
    void updatePreviousValidationErrors() throws ApiErrorResponseException, URIValidationException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(PREVIOUS_PERIOD_LINK);

        when(previousPeriodResourceHandler.update(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodUpdate);
        when(previousPeriodUpdate.execute()).thenReturn(responseNoData);
        when(responseNoData.hasErrors()).thenReturn(true);

        when(responseNoData.getErrors()).thenReturn(apiErrors);
        when(validationContext.getValidationErrors(apiErrors)).thenReturn(
                submissionValidationErrors);

        assertAll(() ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));

        verify(existingValidationErrors).addAll(submissionValidationErrors);
    }

    @Test
    @DisplayName("Update previous period - api error response exception")
    void updatePreviousPeriodApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(PREVIOUS_PERIOD_LINK);

        when(previousPeriodResourceHandler.update(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodUpdate);
        when(previousPeriodUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleSubmissionException(apiErrorResponseException, PREVIOUS_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));
    }

    @Test
    @DisplayName("Update previous period - uri validation exception")
    void updatePreviousPeriodURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(smallFull.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getPreviousPeriod()).thenReturn(PREVIOUS_PERIOD_LINK);

        when(previousPeriodResourceHandler.update(PREVIOUS_PERIOD_URI, previousPeriod)).thenReturn(previousPeriodUpdate);
        when(previousPeriodUpdate.execute()).thenThrow(uriValidationException);

        doThrow(serviceException)
                .when(serviceExceptionHandler)
                        .handleURIValidationException(uriValidationException, PREVIOUS_PERIOD_RESOURCE);

        assertThrows(ServiceException.class, () ->
                previousPeriodService
                        .submitPreviousPeriod(apiClient, smallFull, TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                                previousPeriod, existingValidationErrors));
    }
}