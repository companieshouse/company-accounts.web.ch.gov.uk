package uk.gov.companieshouse.web.accounts.service.smallfull.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.api.ApiClient;
import uk.gov.companieshouse.api.error.ApiErrorResponseException;
import uk.gov.companieshouse.api.handler.exception.URIValidationException;
import uk.gov.companieshouse.api.model.ApiResponse;
import uk.gov.companieshouse.api.model.accounts.smallfull.loanstodirectors.LoanApi;
import uk.gov.companieshouse.web.accounts.api.ApiClientService;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.AddOrRemoveLoans;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.Loan;
import uk.gov.companieshouse.web.accounts.model.loanstodirectors.LoanToAdd;
import uk.gov.companieshouse.web.accounts.service.smallfull.LoanService;
import uk.gov.companieshouse.web.accounts.transformer.smallfull.loanstodirectors.LoanTransformer;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.helper.ServiceExceptionHandler;
import uk.gov.companieshouse.web.accounts.validation.smallfull.LoanValidator;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoansServiceImpl implements LoanService {

    @Autowired
    private ApiClientService apiClientService;

    @Autowired
    private LoanTransformer loanTransformer;

    @Autowired
    private ServiceExceptionHandler serviceExceptionHandler;

    @Autowired
    private ValidationContext validationContext;

    @Autowired
    private LoanValidator loanValidator;

    private static final UriTemplate LOANS_URI =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors/loans");

    private static final UriTemplate LOAN_URI_WITH_ID =
            new UriTemplate("/transactions/{transactionId}/company-accounts/{companyAccountsId}/small-full/notes/loans-to-directors/loans/{loanId}");

    private static final String RESOURCE_NAME = "loans";

    @Override
    public Loan[] getAllLoans(String transactionId, String companyAccountsId) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = LOANS_URI.expand(transactionId, companyAccountsId).toString();

        try {
            LoanApi[] loans = apiClient.smallFull().loansToDirectors().loans().getAll(uri).execute().getData();
            return loanTransformer.getAllLoans(loans);
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleRetrievalException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return new Loan[0];

    }

    @Override
    public List<ValidationError> createLoan(String transactionId, String companyAccountsId, LoanToAdd loanToAdd) throws ServiceException {

        ApiClient apiClient = apiClientService.getApiClient();

        String uri = LOANS_URI.expand(transactionId, companyAccountsId).toString();

        LoanApi loanApi = loanTransformer.getLoanApi(loanToAdd);

        List<ValidationError> validationErrors = loanValidator.validateDirectorToAdd(loanToAdd);

        if(!validationErrors.isEmpty()) {
            return validationErrors;
        }

        try {
            ApiResponse<LoanApi> apiResponse = apiClient.smallFull().loansToDirectors().loans().create(uri, loanApi).execute();
            if (apiResponse.hasErrors()) {
                    validationErrors.addAll(validationContext.getValidationErrors(apiResponse.getErrors()));
            }
        } catch (ApiErrorResponseException e) {
            serviceExceptionHandler.handleSubmissionException(e, RESOURCE_NAME);
        } catch (URIValidationException e) {
            serviceExceptionHandler.handleURIValidationException(e, RESOURCE_NAME);
        }

        return validationErrors;
    }

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

    @Override
    public List<ValidationError> submitAddOrRemoveLoans(String transactionId, String companyAccountsId, AddOrRemoveLoans addOrRemoveLoans) throws ServiceException {

        return new ArrayList<>();
    }
}
