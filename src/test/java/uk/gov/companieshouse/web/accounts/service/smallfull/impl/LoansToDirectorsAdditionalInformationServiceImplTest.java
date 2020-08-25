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
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.LoansToDirectorsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.additionalinformation.AdditionalInformationResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.additionalinformation.request.AdditionalInformationCreate;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.additionalinformation.request.AdditionalInformationDelete;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.additionalinformation.request.AdditionalInformationGet;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.additionalinformation.request.AdditionalInformationUpdate;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.AdditionalInformationApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsAdditionalInformationService;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoansToDirectorsAdditionalInformationServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ValidationContext validationContext;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private LoansToDirectorsResourceHandler loansToDirectorsResourceHandler;

    @Mock
    private AdditionalInformationResourceHandler additionalInformationResourceHandler;

    @Mock
    private AdditionalInformationCreate additionalInformationCreate;

    @Mock
    private AdditionalInformationUpdate additionalInformationUpdate;

    @Mock
    private AdditionalInformationGet additionalInformationGet;

    @Mock
    private AdditionalInformationDelete additionalInformationDelete;

    @Mock
    private ApiResponse<AdditionalInformationApi> responseWithData;

    @Mock
    private ApiResponse<Void> responseNoData;

    @Mock
    private AdditionalInformationApi additionalInformationApi;

    @Mock
    private List<ValidationError> mockValidationErrors;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @InjectMocks
    private LoansToDirectorsAdditionalInformationService loansToDirectorsAdditionalInformationService = new LoansToDirectorsAdditionalInformationServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String ADDITIONAL_INFORMATION_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/loans-to-directors/additional-information";

    private static final String RESOURCE_NAME = "loans to directors additional information";

    @BeforeEach
    void setUp() {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.additionalInformation()).thenReturn(additionalInformationResourceHandler);
    }

    @Test
    @DisplayName("Get loans to directors additional information - success")
    void getLoansToDirectorsAdditionalInformationSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler.get(ADDITIONAL_INFORMATION_URI)).thenReturn(
                additionalInformationGet);

        when(additionalInformationGet.execute()).thenReturn(responseWithData);

        when(responseWithData.getData()).thenReturn(additionalInformationApi);

        AdditionalInformationApi returnedAdditionalInformation =
                loansToDirectorsAdditionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(additionalInformationApi, returnedAdditionalInformation);
    }

    @Test
    @DisplayName("Get loans to directors additional information - throws ApiErrorResponseException")
    void getLoansToDirectorsAdditionalInformationThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler.get(ADDITIONAL_INFORMATION_URI)).thenReturn(
                additionalInformationGet);

        when(additionalInformationGet.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get loans to directors additional information - resource not found")
    void getLoansToDirectorsAdditionalInformationResourceNotFound()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler.get(ADDITIONAL_INFORMATION_URI)).thenReturn(
                additionalInformationGet);

        when(additionalInformationGet.execute()).thenThrow(apiErrorResponseException);

        doNothing()
                .when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertNull(loansToDirectorsAdditionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Get loans to directors additional information - throws URIValidationException")
    void getLoansToDirectorsAdditionalInformationThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler.get(ADDITIONAL_INFORMATION_URI)).thenReturn(
                additionalInformationGet);

        when(additionalInformationGet.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.getAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Create loans to directors additional information - success")
    void createLoansToDirectorsAdditionalInformationSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .create(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationCreate);

        when(additionalInformationCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                loansToDirectorsAdditionalInformationService
                        .createAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Create loans to directors additional information - throws ApiErrorResponseException")
    void createLoansToDirectorsAdditionalInformationThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .create(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationCreate);

        when(additionalInformationCreate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.createAdditionalInformation(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi));
    }

    @Test
    @DisplayName("Create loans to directors additional information - validation errors")
    void createLoansToDirectorsAdditionalInformationValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .create(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationCreate);

        when(additionalInformationCreate.execute()).thenReturn(responseWithData);

        when(responseWithData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseWithData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                loansToDirectorsAdditionalInformationService
                        .createAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Create loans to directors additional information - throws URIValidationException")
    void createLoansToDirectorsAdditionalInformationThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .create(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationCreate);

        when(additionalInformationCreate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.createAdditionalInformation(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi));
    }

    @Test
    @DisplayName("Update loans to directors additional information - success")
    void updateLoansToDirectorsAdditionalInformationSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .update(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationUpdate);

        when(additionalInformationUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(false);

        List<ValidationError> validationErrors =
                loansToDirectorsAdditionalInformationService
                        .updateAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("Update loans to directors additional information - throws ApiErrorResponseException")
    void updateLoansToDirectorsAdditionalInformationThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .update(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationUpdate);

        when(additionalInformationUpdate.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.updateAdditionalInformation(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi));
    }

    @Test
    @DisplayName("Update loans to directors additional information - validation errors")
    void updateLoansToDirectorsAdditionalInformationValidationErrors()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .update(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationUpdate);

        when(additionalInformationUpdate.execute()).thenReturn(responseNoData);

        when(responseNoData.hasErrors()).thenReturn(true);

        when(validationContext.getValidationErrors(responseNoData.getErrors()))
                .thenReturn(mockValidationErrors);

        List<ValidationError> validationErrors =
                loansToDirectorsAdditionalInformationService.updateAdditionalInformation(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi);

        assertEquals(mockValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("Update loans to directors additional information - throws URIValidationException")
    void updateLoansToDirectorsAdditionalInformationThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler
                .update(ADDITIONAL_INFORMATION_URI, additionalInformationApi))
                .thenReturn(additionalInformationUpdate);

        when(additionalInformationUpdate.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.updateAdditionalInformation(
                        TRANSACTION_ID, COMPANY_ACCOUNTS_ID, additionalInformationApi));
    }

    @Test
    @DisplayName("Delete loans to directors additional information - success")
    void deleteLoansToDirectorsAdditionalInformationSuccess()
            throws ApiErrorResponseException, URIValidationException {

        when(additionalInformationResourceHandler.delete(ADDITIONAL_INFORMATION_URI))
                .thenReturn(additionalInformationDelete);

        assertAll(() ->
                loansToDirectorsAdditionalInformationService.deleteAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(additionalInformationDelete).execute();
    }

    @Test
    @DisplayName("Delete loans to directors additional informations - throws ApiErrorResponseException")
    void deleteLoansToDirectorsAdditionalInformationThrowsApiErrorResponseException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler.delete(ADDITIONAL_INFORMATION_URI))
                .thenReturn(additionalInformationDelete);

        when(additionalInformationDelete.execute()).thenThrow(apiErrorResponseException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.deleteAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("Delete loans to directors additional information - throws URIValidationException")
    void deleteLoansToDirectorsAdditionalInformationThrowsURIValidationException()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(additionalInformationResourceHandler.delete(ADDITIONAL_INFORMATION_URI))
                .thenReturn(additionalInformationDelete);

        when(additionalInformationDelete.execute()).thenThrow(uriValidationException);

        doThrow(ServiceException.class)
                .when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsAdditionalInformationService.deleteAdditionalInformation(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}