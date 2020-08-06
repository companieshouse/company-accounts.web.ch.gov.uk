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
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.loan.LoansResourceHandler;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.loan.request.LoanCreate;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.loan.request.LoanDelete;
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.loan.request.LoanGetAll;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors.LoanTransformer;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoansServiceImplTest {

    @Mock
    private ApiClientService apiClientService;

    @Mock
    private ApiClient apiClient;

    @Mock
    private LoanApi loanApi;

    @Mock
    private LoanDelete loanDelete;

    @Mock
    private LoansResourceHandler loansResourceHandler;

    @Mock
    private SmallFullResourceHandler smallFullResourceHandler;

    @Mock
    private LoansToDirectorsResourceHandler loansToDirectorsResourceHandler;

    @Mock
    private ApiErrorResponseException apiErrorResponseException;

    @Mock
    private ServiceExceptionHandler serviceExceptionHandler;

    @Mock
    private URIValidationException uriValidationException;

    @Mock
    private ApiResponse<LoanApi[]> responseWithMultipleDirectors;

    @Mock
    private ApiResponse<LoanApi> responseWithSingleDirector;

    @Mock
    private LoanTransformer loanTransformer;

    @Mock
    private LoanToAdd loanToAdd;

    @Mock
    private LoanGetAll loanGetAll;

    @Mock
    private LoanCreate loanCreate;

    @InjectMocks
    private LoansServiceImpl loansService;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String LOAN_ID = "loanId";

    private static final String LOANS_URI = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/loans-to-directors/loans";

    private static final String LOANS_URI_WITH_ID = "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
            COMPANY_ACCOUNTS_ID + "/small-full/notes/loans-to-directors/loans/" + LOAN_ID;

    private static final String RESOURCE_NAME = "loans";

    @Test
    @DisplayName("GET - all loans - success")
    void getAllLoansSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenReturn(responseWithMultipleDirectors);
        LoanApi[] loanApi = new LoanApi[1];
        when(responseWithMultipleDirectors.getData()).thenReturn(loanApi);
        Loan[] allLoans = new Loan[1];
        when(loanTransformer.getAllLoans(loanApi)).thenReturn(allLoans);

        Loan[] response = loansService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(allLoans, response);
    }

    @Test
    @DisplayName("DELETE - loan - success")
    void deleteLoanSuccess() throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.delete(LOANS_URI_WITH_ID)).thenReturn(loanDelete);

        loansService.deleteLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, LOAN_ID);
        verify(loanDelete).execute();
    }

    @Test
    @DisplayName("GET - all loans - not found")
    void getAllLoansNotFound()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        Loan[] response = loansService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertNotNull(response);
        assertEquals(0, response.length);
    }

    @Test
    @DisplayName("GET - all loans - ApiErrorResponseException")
    void getAllLoansApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("GET - all loans - URIValidationException")
    void getAllLoansURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - loan - success")
    void createLoanSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleDirector);

        List<ValidationError> validationErrors = loansService.createLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, loanToAdd);

        assertNull(validationErrors);
    }

    @Test
    @DisplayName("POST - loan - ApiErrorResponseException")
    void createLoanApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansService.createLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, loanToAdd));
    }

    @Test
    @DisplayName("POST - loan - URIValidationException")
    void createLoanURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansService.createLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, loanToAdd));
    }

    @Test
    @DisplayName("DELETE - loan - ApiErrorResponseException")
    void deleteLoanApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.delete(LOANS_URI_WITH_ID)).thenReturn(loanDelete);
        when(loanDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansService.deleteLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, LOAN_ID));
    }

    @Test
    @DisplayName("DELETE - loan - URIValidationException")
    void deleteLoanURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.delete(LOANS_URI_WITH_ID)).thenReturn(loanDelete);
        when(loanDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler).handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class, () -> loansService.deleteLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, LOAN_ID));
    }
}
