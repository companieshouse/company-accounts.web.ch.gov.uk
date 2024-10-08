package uk.gov.companieshouse.web.accounts.service.smallfull.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Breakdown;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors.LoanTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;
import uk.gov.companieshouse.web.accounts.validation.smallfull.LoanValidator;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoansServiceImplTest {

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
    private ApiResponse<LoanApi[]> responseWithMultipleLoans;

    @Mock
    private ApiResponse<LoanApi> responseWithSingleLoan;

    @Mock
    private LoanTransformer loanTransformer;

    @Mock
    private LoanToAdd loanToAdd;

    @Mock
    private AddOrRemoveLoans mockAddOrRemoveLoans;

    @Mock
    private LoanGetAll loanGetAll;

    @Mock
    private LoanCreate loanCreate;

    @Mock
    private LoanValidator loanValidator;

    @Mock
    private ValidationContext validationContext;

    @InjectMocks
    private LoansServiceImpl loansService;

    private static final String TRANSACTION_ID = "transactionId";

    private static final String COMPANY_ACCOUNTS_ID = "companyAccountsId";

    private static final String LOAN_ID = "loanId";

    private static final String LOANS_URI =
            "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                    COMPANY_ACCOUNTS_ID + "/small-full/notes/loans-to-directors/loans";

    private static final String LOANS_URI_WITH_ID =
            "/transactions/" + TRANSACTION_ID + "/company-accounts/" +
                    COMPANY_ACCOUNTS_ID + "/small-full/notes/loans-to-directors/loans/" + LOAN_ID;

    private static final String RESOURCE_NAME = "loans";

    private static final String LOAN_TO_ADD = "loanToAdd";

    private static final String DIRECTOR_NAME = LOAN_TO_ADD + ".directorName";

    private static final String DESCRIPTION = "description";

    @Test
    @DisplayName("GET - all loans - success")
    void getAllLoansSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenReturn(responseWithMultipleLoans);
        LoanApi[] loansApi = new LoanApi[1];
        loansApi[0] = new LoanApi();
        when(responseWithMultipleLoans.getData()).thenReturn(loansApi);
        Loan[] allLoans = new Loan[1];
        when(loanTransformer.getAllLoans(loansApi)).thenReturn(allLoans);

        Loan[] response = loansService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID);

        assertEquals(allLoans, response);
    }

    @Test
    @DisplayName("DELETE - loan - success")
    void deleteLoanSuccess()
            throws ApiErrorResponseException, URIValidationException, ServiceException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
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
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenThrow(apiErrorResponseException);
        doNothing().when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

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
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleRetrievalException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> loansService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("GET - all loans - URIValidationException")
    void getAllLoansURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.getAll(LOANS_URI)).thenReturn(loanGetAll);
        when(loanGetAll.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> loansService.getAllLoans(TRANSACTION_ID, COMPANY_ACCOUNTS_ID));
    }

    @Test
    @DisplayName("POST - loan - success")
    void createLoanSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(mockAddOrRemoveLoans.getLoanToAdd()).thenReturn(loanToAdd);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleLoan);

        List<ValidationError> validationErrors = loansService.createLoan(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, mockAddOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - loan - validation errors for multi year filer")
    void createLoanValidationForMultiYearFiler()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        mockAddOrRemoveLoans.setIsMultiYearFiler(true);

        when(loanValidator.validateLoanToAdd(loanToAdd, mockAddOrRemoveLoans.getIsMultiYearFiler(),
                false)).thenReturn(new ArrayList<>());

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(mockAddOrRemoveLoans.getLoanToAdd()).thenReturn(loanToAdd);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleLoan);
        when(responseWithSingleLoan.hasErrors()).thenReturn(true);

        ValidationError validationError = new ValidationError();
        List<ValidationError> apiValidationErrors = new ArrayList<>();
        apiValidationErrors.add(validationError);
        when(validationContext.getValidationErrors(responseWithSingleLoan.getErrors())).thenReturn(
                apiValidationErrors);

        List<ValidationError> validationErrors = loansService.createLoan(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, mockAddOrRemoveLoans);

        assertEquals(apiValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("POST - loan - validation errors for single year filer")
    void createLoanValidationForSingleYearFiler()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(loanValidator.validateLoanToAdd(loanToAdd, false, false)).thenReturn(
                new ArrayList<>());

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(mockAddOrRemoveLoans.getLoanToAdd()).thenReturn(loanToAdd);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleLoan);
        when(responseWithSingleLoan.hasErrors()).thenReturn(true);

        ValidationError validationError = new ValidationError();
        List<ValidationError> apiValidationErrors = new ArrayList<>();
        apiValidationErrors.add(validationError);
        when(validationContext.getValidationErrors(responseWithSingleLoan.getErrors())).thenReturn(
                apiValidationErrors);

        List<ValidationError> validationErrors = loansService.createLoan(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, mockAddOrRemoveLoans);

        assertEquals(apiValidationErrors, validationErrors);
    }

    @Test
    @DisplayName("POST - loan - director name validation failed for multi year filer")
    void createLoanInvalidDirectorNameForMultiYearFiler() throws ServiceException {

        ValidationError validationError = new ValidationError();
        List<ValidationError> nameValidationError = new ArrayList<>();
        nameValidationError.add(validationError);

        mockAddOrRemoveLoans.setIsMultiYearFiler(true);

        when(mockAddOrRemoveLoans.getLoanToAdd()).thenReturn(loanToAdd);

        when(loanValidator.validateLoanToAdd(loanToAdd, mockAddOrRemoveLoans.getIsMultiYearFiler(),
                false)).thenReturn(nameValidationError);

        List<ValidationError> validationErrors = loansService.createLoan(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, mockAddOrRemoveLoans);

        assertEquals(nameValidationError, validationErrors);

        verify(apiClientService, never()).getApiClient();
    }

    @Test
    @DisplayName("POST - loan - director name validation failed for single year filer")
    void createLoanInvalidDirectorNameForSingleYearFiler() throws ServiceException {

        ValidationError validationError = new ValidationError();
        List<ValidationError> nameValidationError = new ArrayList<>();
        nameValidationError.add(validationError);

        when(mockAddOrRemoveLoans.getLoanToAdd()).thenReturn(loanToAdd);

        when(loanValidator.validateLoanToAdd(loanToAdd, false, false)).thenReturn(
                nameValidationError);

        List<ValidationError> validationErrors = loansService.createLoan(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, mockAddOrRemoveLoans);

        assertEquals(nameValidationError, validationErrors);

        verify(apiClientService, never()).getApiClient();
    }

    @Test
    @DisplayName("POST - submit loan - resource is empty for multi year filer")
    void submitAddOrRemoveLoanEmptyResourceForMultiYearFiler() throws ServiceException {

        mockAddOrRemoveLoans.setIsMultiYearFiler(true);

        when(loanValidator.isEmptyResource(mockAddOrRemoveLoans.getLoanToAdd(),
                mockAddOrRemoveLoans.getIsMultiYearFiler())).thenReturn(true);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, mockAddOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
        verify(apiClientService, never()).getApiClient();
    }

    @Test
    @DisplayName("POST - submit loan - resource is empty for single year filer")
    void submitAddOrRemoveLoanEmptyResourceForSingleYearFiler() throws ServiceException {

        when(loanValidator.isEmptyResource(mockAddOrRemoveLoans.getLoanToAdd(), false)).thenReturn(
                true);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, mockAddOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
        verify(apiClientService, never()).getApiClient();
    }

    @Test
    @DisplayName("POST - submit loan - resource is empty for multi year filer - isSingleDirector True")
    void submitAddOrRemoveLoanEmptyResourceForMultiYearFilerIsSingleDirectorTrue()
            throws ServiceException {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        List<String> validNames = new ArrayList<>();
        validNames.add("valid");

        addOrRemoveLoans.setIsMultiYearFiler(true);
        addOrRemoveLoans.setValidDirectorNames(validNames);

        when(loanValidator.isSingleDirectorEmptyResource(addOrRemoveLoans.getLoanToAdd(),
                addOrRemoveLoans.getIsMultiYearFiler())).thenReturn(true);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, addOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
        verify(apiClientService, never()).getApiClient();
    }

    @Test
    @DisplayName("POST - submit loan - resource is empty for single year filer - isSingleDirector True")
    void submitAddOrRemoveLoanEmptyResourceForSingleYearFilerIsSingleDirectorTrue()
            throws ServiceException {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        List<String> validNames = new ArrayList<>();
        validNames.add("valid");

        addOrRemoveLoans.setValidDirectorNames(validNames);
        addOrRemoveLoans.setIsMultiYearFiler(false);

        when(loanValidator.isSingleDirectorEmptyResource(addOrRemoveLoans.getLoanToAdd(),
                addOrRemoveLoans.getIsMultiYearFiler())).thenReturn(true);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, addOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
        verify(apiClientService, never()).getApiClient();
    }

    @Test
    @DisplayName("POST - submit loan for multi year filer - successful with non empty resource")
    void submitAddOrRemoveLoanSuccessfulForMultiYearFilerNonEmptyResource()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);
        addOrRemoveLoans.getLoanToAdd().setDescription(DESCRIPTION);
        addOrRemoveLoans.getLoanToAdd().setBreakdown(createBreakdown(true, true));
        addOrRemoveLoans.setIsMultiYearFiler(true);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loanValidator.isEmptyResource(addOrRemoveLoans.getLoanToAdd(),
                addOrRemoveLoans.getIsMultiYearFiler())).thenReturn(false);
        when(loanTransformer.getLoanApi(addOrRemoveLoans.getLoanToAdd())).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleLoan);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, addOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - submit loan for single year filer - successful with non empty resource")
    void submitAddOrRemoveLoanSuccessfulForSingleYearFilerNonEmptyResource()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();
        addOrRemoveLoans.getLoanToAdd().setDirectorName(DIRECTOR_NAME);
        addOrRemoveLoans.getLoanToAdd().setDescription(DESCRIPTION);
        addOrRemoveLoans.getLoanToAdd().setBreakdown(createBreakdown(true, true));
        addOrRemoveLoans.setIsMultiYearFiler(false);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loanValidator.isEmptyResource(addOrRemoveLoans.getLoanToAdd(),
                addOrRemoveLoans.getIsMultiYearFiler())).thenReturn(false);
        when(loanTransformer.getLoanApi(addOrRemoveLoans.getLoanToAdd())).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleLoan);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, addOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
    }

    @Test
    @DisplayName("POST - submit loan for multi year filer - success")
    void submitAddOrRemoveLoanForMultiYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();

        addOrRemoveLoans.setIsMultiYearFiler(true);
        addOrRemoveLoans.setLoanToAdd(new LoanToAdd());

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loanTransformer.getLoanApi(addOrRemoveLoans.getLoanToAdd())).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleLoan);

        when(loanValidator.isEmptyResource(addOrRemoveLoans.getLoanToAdd(),
                addOrRemoveLoans.getIsMultiYearFiler())).thenReturn(false);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, addOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
        verify(apiClientService, times(1)).getApiClient();
    }

    @Test
    @DisplayName("POST - submit loan for single year filer - success")
    void submitAddOrRemoveLoanForSingleYearFilerSuccess()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        AddOrRemoveLoans addOrRemoveLoans = new AddOrRemoveLoans();
        addOrRemoveLoans.setLoanToAdd(new LoanToAdd());
        addOrRemoveLoans.setIsMultiYearFiler(false);

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(loanTransformer.getLoanApi(addOrRemoveLoans.getLoanToAdd())).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenReturn(responseWithSingleLoan);

        when(loanValidator.isEmptyResource(addOrRemoveLoans.getLoanToAdd(), false)).thenReturn(
                false);

        List<ValidationError> validationErrors = loansService.submitAddOrRemoveLoans(TRANSACTION_ID,
                COMPANY_ACCOUNTS_ID, addOrRemoveLoans);

        assertTrue(validationErrors.isEmpty());
        verify(apiClientService, times(1)).getApiClient();
    }

    @Test
    @DisplayName("POST - loan - ApiErrorResponseException")
    void createLoanApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(mockAddOrRemoveLoans.getLoanToAdd()).thenReturn(loanToAdd);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleSubmissionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> loansService.createLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                        mockAddOrRemoveLoans));
    }

    @Test
    @DisplayName("POST - loan - URIValidationException")
    void createLoanURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);

        when(mockAddOrRemoveLoans.getLoanToAdd()).thenReturn(loanToAdd);

        when(loanTransformer.getLoanApi(loanToAdd)).thenReturn(loanApi);

        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.create(LOANS_URI, loanApi)).thenReturn(loanCreate);
        when(loanCreate.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> loansService.createLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID,
                        mockAddOrRemoveLoans));
    }

    @Test
    @DisplayName("DELETE - loan - ApiErrorResponseException")
    void deleteLoanApiErrorResponseException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.delete(LOANS_URI_WITH_ID)).thenReturn(loanDelete);
        when(loanDelete.execute()).thenThrow(apiErrorResponseException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleDeletionException(apiErrorResponseException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> loansService.deleteLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, LOAN_ID));
    }

    @Test
    @DisplayName("DELETE - loan - URIValidationException")
    void deleteLoanURIValidationException()
            throws ServiceException, ApiErrorResponseException, URIValidationException {

        when(apiClientService.getApiClient()).thenReturn(apiClient);
        when(apiClient.smallFull()).thenReturn(smallFullResourceHandler);
        when(smallFullResourceHandler.loansToDirectors()).thenReturn(
                loansToDirectorsResourceHandler);
        when(loansToDirectorsResourceHandler.loans()).thenReturn(loansResourceHandler);
        when(loansResourceHandler.delete(LOANS_URI_WITH_ID)).thenReturn(loanDelete);
        when(loanDelete.execute()).thenThrow(uriValidationException);
        doThrow(ServiceException.class).when(serviceExceptionHandler)
                .handleURIValidationException(uriValidationException, RESOURCE_NAME);

        assertThrows(ServiceException.class,
                () -> loansService.deleteLoan(TRANSACTION_ID, COMPANY_ACCOUNTS_ID, LOAN_ID));
    }

    private Breakdown createBreakdown(boolean includePeriodStart, boolean includePeriodEnd) {

        Breakdown validBreakdown = new Breakdown();

        if (includePeriodStart) {
            validBreakdown.setBalanceAtPeriodStart(1L);
        }

        if (includePeriodEnd) {
            validBreakdown.setBalanceAtPeriodEnd(1L);
        }

        return validBreakdown;
    }
}
