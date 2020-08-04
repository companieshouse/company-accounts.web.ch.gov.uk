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
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.LoansToDirectorsResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.request.LoansToDirectorsCreate;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.request.LoansToDirectorsDelete;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.request.LoansToDirectorsGet;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullApi;
import uk.gov.companieshouse.api.model.accounts.smallfull.SmallFullLinks;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoansToDirectorsApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoansToDirectorsService;
import uk.gov.companieshouse.web.accounts.service.smallfull.SmallFullService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoansToDirectorsServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private SmallFullApi smallFullApi;

    @Mock
    private SmallFullService smallFullService;

    @Mock
    private SmallFullLinks smallFullLinks;

    @Mock
    private LoansToDirectorsApi loansToDirectorsApi;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private ApiResponse<LoansToDirectorsApi> responseWithData;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private LoansToDirectorsResourceHandler loansToDirectorsResourceHandler;

    @Mock
    private LoansToDirectorsGet loansToDirectorsGet;

    @Mock
    private LoansToDirectorsCreate loansToDirectorsCreate;

    @Mock
    private LoansToDirectorsDelete loansToDirectorsDelete;

    @InjectMocks
    private LoansToDirectorsService loansToDirectorsService = new LoansToDirectorsServiceImpl();

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String LOANS_TO_DIRECTORS_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/loans-to-directors";

    private static final String SMALL_FULL_DIRECTORS_LINK = "smallFullDirectorsLink";

    private static final String RESOURCE_NAME = "loans to directors";

    @Test
    @DisplayName("GET - loans to directors - success")
    void getLoansToDirectorsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.get(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsGet);
        when(loansToDirectorsGet.execute()).thenReturn(responseWithData);
        when(responseWithData.getData()).thenReturn(loansToDirectorsApi);

        LoansToDirectorsApi response = loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(loansToDirectorsApi, response);
    }

    @Test
    @DisplayName("GET - LoansToDirectors - not found")
    void getLoansToDirectorsNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.get(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsGet);
        when(loansToDirectorsGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        LoansToDirectorsApi response = loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNull(response);
    }

    @Test
    @DisplayName("GET - loans to directors - ApiErrorResponseException")
    void getLoansToDirectorsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.get(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsGet);
        when(loansToDirectorsGet.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("GET - loans to directors - URIValidationException")
    void getLoansToDirectorsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.get(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsGet);
        when(loansToDirectorsGet.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () ->
                loansToDirectorsService.getLoansToDirectors(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - loans to directors - success")
    void createLoansToDirectorsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.get(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsGet);
        when(loansToDirectorsGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);
        when(loansToDirectorsResourceHandler.create(eq(LOANS_TO_DIRECTORS_URI), any(LoansToDirectorsApi.class))).thenReturn(loansToDirectorsCreate);

        assertAll(() -> loansToDirectorsService.createLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(loansToDirectorsCreate).execute();
    }

    @Test
    @DisplayName("POST - loans to directors - ApiErrorResponseException")
    void createLoansToDirectorsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.get(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsGet);
        when(loansToDirectorsGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);
        when(loansToDirectorsResourceHandler.create(eq(LOANS_TO_DIRECTORS_URI), any(LoansToDirectorsApi.class))).thenReturn(loansToDirectorsCreate);
        when(loansToDirectorsCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansToDirectorsService.createLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - loans to directors - URIValidationException")
    void createLoansToDirectorsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.get(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsGet);
        when(loansToDirectorsGet.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);
        when(loansToDirectorsResourceHandler.create(eq(LOANS_TO_DIRECTORS_URI), any(LoansToDirectorsApi.class))).thenReturn(loansToDirectorsCreate);
        when(loansToDirectorsCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansToDirectorsService.createLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - loans to directors - success")
    void deleteLoansToDirectorsSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getLoansToDirectors()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.delete(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsDelete);

        assertAll(() -> loansToDirectorsService.deleteLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(loansToDirectorsDelete).execute();
    }

    @Test
    @DisplayName("DELETE - loans to directors - no small full link")
    void deleteLoansToDirectorsNoSmallFullLink() throws ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getLoansToDirectors()).thenReturn(null);

        assertAll(() -> loansToDirectorsService.deleteLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));

        verify(apiClient, never()).smallFull();
    }

    @Test
    @DisplayName("DELETE - loans to directors - ApiErrorResponseException")
    void deleteLoansToDirectorsApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getLoansToDirectors()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.delete(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsDelete);
        when(loansToDirectorsDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansToDirectorsService.deleteLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("DELETE - loans to directors - URIValidationException")
    void deleteLoansToDirectorsURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(smallFullService.getSmallFullAccounts(apiClient, TRANSACTION_ID, COMPANY_ACCOUNTS_ID)).thenReturn(smallFullApi);
        when(smallFullApi.getLinks()).thenReturn(smallFullLinks);
        when(smallFullLinks.getLoansToDirectors()).thenReturn(SMALL_FULL_DIRECTORS_LINK);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.delete(LOANS_TO_DIRECTORS_URI)).thenReturn(loansToDirectorsDelete);
        when(loansToDirectorsDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansToDirectorsService.deleteLoansToDirectors(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }
}
