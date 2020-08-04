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
import uk.gov.companieshouse.api.handler.smallfull.loanstodirectors.loan.request.LoanDelete;
import uk.gov.companieshouse.api.model.accounts.abridged.notes.LoansApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private LoansApi loansApi;

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
