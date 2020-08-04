package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.web.accounts.api.impl.ApiClientServiceImpl;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandlerImpl;

@Service
public class LoansServiceImpl implements LoanService {

    @Autowired
    private ApiClientServiceImpl apiClientService;

    @Autowired
    private ServiceExceptionHandlerImpl serviceExceptionHandler;

    private static final UriTemplate LOAN_URI_WITH_ID =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors/loans/{loanId}");

    private static final String RESOURCE_NAME = "loans";

    @Override
    public void deleteLoan(String transactionId, String companyAccountsId, String loanId) throws ServiceException {


        ApiClient apiClient = apiClientService.getApiClient();

        String uri = LOAN_URI_WITH_ID.expand(transactionId, companyAccountsId, loanId).toString();

        try {
            apiClient.smallFull().loansToDirectors().loans().delete(uri).execute();
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleDeletionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

    }
}
